package com.example.myapplication.main.common.sheets

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.access.AccessManager
import com.example.myapplication.data.access.AccessResult
import com.example.myapplication.data.access.DailyLimitManager
import com.example.myapplication.data.access.ReviewManager
import com.example.myapplication.data.access.UserAccessState
import com.example.myapplication.data.auth.AuthManager
import com.example.myapplication.data.auth.AuthResult
import com.example.myapplication.data.purchase.PurchaseManager
import com.example.myapplication.data.purchase.RevenueCatManager
import com.example.myapplication.utilities.pref.AppPreferencesHelper
import com.google.gson.Gson
import com.revenuecat.purchases.Package
import kotlinx.coroutines.delay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AccessSheetVM"

@HiltViewModel
class AccessSheetViewModel @Inject constructor(
    private val accessManager: AccessManager,
    private val authManager: AuthManager,
    private val purchaseManager: PurchaseManager,
    private val reviewManager: ReviewManager,
    private val revenueCatManager: RevenueCatManager,
    private val appPrefs: AppPreferencesHelper,
    private val dailyLimitManager: DailyLimitManager,
) : ViewModel() {

    // ── User access state (observable by UI) ─────────────────────────
    val userState: StateFlow<UserAccessState> = accessManager.userState

    /** Exposed so screens can hide lock badges while DEV_MODE bypasses gating. */
    val isDevMode: Boolean get() = accessManager.isDevMode

    // ── Sheet visibility state ────────────────────────────────────────

    private val _sheetState = MutableStateFlow<AccessSheetState>(AccessSheetState.Hidden)
    val sheetState: StateFlow<AccessSheetState> = _sheetState.asStateFlow()

    // ── Parental gate ─────────────────────────────────────────────────

    private val _showParentalGate = MutableStateFlow(false)
    val showParentalGate: StateFlow<Boolean> = _showParentalGate.asStateFlow()

    /** State waiting to be applied after parental gate passes. */
    private var pendingStateAfterGate: AccessSheetState = AccessSheetState.Hidden

    /**
     * Use this instead of setting _sheetState directly for Login and Paywall.
     * Intercepts with the parental gate; other states set directly.
     */
    fun requestState(newState: AccessSheetState) {
        if (accessManager.isDevMode) return
        when (newState) {
            is AccessSheetState.Login, is AccessSheetState.Paywall -> {
                pendingStateAfterGate = newState
                _showParentalGate.value = true
            }
            else -> _sheetState.value = newState
        }
    }

    fun parentalGatePassed() {
        _showParentalGate.value = false
        _sheetState.value = pendingStateAfterGate
        if (pendingStateAfterGate is AccessSheetState.Paywall) loadOfferings()
        pendingStateAfterGate = AccessSheetState.Hidden
    }

    fun parentalGateCancelled() {
        _showParentalGate.value = false
        pendingStateAfterGate = AccessSheetState.Hidden
    }

    // ── Loading (purchase / sign-in in progress) ──────────────────────

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // ── One-shot toast messages ───────────────────────────────────────

    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message.asSharedFlow()

    // ── Available RevenueCat packages (loaded when paywall opens) ─────

    private val _packages = MutableStateFlow<List<Package>>(emptyList())
    val packages: StateFlow<List<Package>> = _packages.asStateFlow()

    private val _isTrialEligible = MutableStateFlow(false)
    val isTrialEligible: StateFlow<Boolean> = _isTrialEligible.asStateFlow()

    init {
        viewModelScope.launch {
            _isTrialEligible.value = revenueCatManager.isEligibleForIntroOffer()
        }
    }

    // ── Access check entry point ──────────────────────────────────────

    /**
     * Called before navigating to a module.
     * Returns true if allowed (caller should navigate).
     * Returns false if a sheet was shown (caller should NOT navigate).
     */
    suspend fun checkAccess(moduleId: String): Boolean {
        if (accessManager.isDevMode) return true
        Log.d(TAG, "checkAccess → moduleId='$moduleId'")

        // Empty moduleId = unregistered free activity, still enforce daily limit for non-premium users
        if (moduleId.isEmpty()) {
            val state = accessManager.userState.value
            if (!state.isPremium) {
                if (!dailyLimitManager.hasAttemptsLeft()) {
                    _sheetState.value = AccessSheetState.DailyLimit(moduleId = moduleId)
                    return false
                }
                dailyLimitManager.recordModulePlayed(moduleId)
            }
            return true
        }

        val hasAccessResult = accessManager.checkAccess(moduleId)
        Log.d(TAG, "checkAccess → hasAccessResult='${Gson().toJson(hasAccessResult)}'")
        return when (hasAccessResult) {
            is AccessResult.Allowed -> true
            is AccessResult.DailyLimitReached -> {
                _sheetState.value = AccessSheetState.DailyLimit(moduleId = moduleId)
                false
            }
            is AccessResult.SubscribeRequired -> {
                requestState(AccessSheetState.Paywall(moduleId))
                false
            }
        }
    }

    fun dismiss() {
        _sheetState.value = AccessSheetState.Hidden
    }

    // ── Google Sign-In ────────────────────────────────────────────────

    fun signInWithGoogle(activity: Activity) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = authManager.signInWithGoogle(activity)) {
                is AuthResult.Success  -> onSignInSuccess()
                is AuthResult.Cancelled -> Unit
                is AuthResult.Error    -> _message.emit(result.message)
                is AuthResult.NeedsAccountLinking ->
                    _message.emit("Account already exists with ${result.existingProvider}. Please use that to sign in.")
            }
            _isLoading.value = false
        }
    }

    // ── Apple Sign-In ─────────────────────────────────────────────────

    fun signInWithApple(activity: Activity) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = authManager.signInWithApple(activity)) {
                is AuthResult.Success  -> onSignInSuccess()
                is AuthResult.Cancelled -> Unit
                is AuthResult.Error    -> _message.emit(result.message)
                is AuthResult.NeedsAccountLinking ->
                    _message.emit("Account already exists with ${result.existingProvider}. Please use that to sign in.")
            }
            _isLoading.value = false
        }
    }

    // ── Purchase ──────────────────────────────────────────────────────

    fun purchase(activity: Activity, packageToPurchase: Package) {
        doPurchase(activity, packageToPurchase)
    }

    private fun doPurchase(activity: Activity, packageToPurchase: Package) {
        viewModelScope.launch {
            Log.d(TAG, "doPurchase → package=${packageToPurchase.identifier}")
            _isLoading.value = true
            when (val result = purchaseManager.purchase(activity, packageToPurchase)) {
                is PurchaseManager.PurchaseResult.Success   -> _sheetState.value = AccessSheetState.PremiumCelebration
                is PurchaseManager.PurchaseResult.Cancelled -> Unit
                is PurchaseManager.PurchaseResult.Error     -> _message.emit(result.message)
            }
            _isLoading.value = false
        }
    }

    // ── Premium celebration actions ───────────────────────────────────

    suspend fun rateNowAndDismiss(activity: Activity) {
        reviewManager.markBannerShown()
        dismiss()
        reviewManager.requestNativeReview(activity)
    }

    fun openFeedbackAndDismiss(context: android.content.Context) {
        reviewManager.markBannerShown()
        dismiss()
        reviewManager.openFeedbackEmail(context)
    }

    // ── Restore purchases ─────────────────────────────────────────────

    fun restorePurchases() {
        viewModelScope.launch {
            _isLoading.value = true
            val restored = purchaseManager.restorePurchases()
            if (restored) dismiss() else _message.emit("No previous purchases found.")
            _isLoading.value = false
        }
    }

    // ── Private helpers ───────────────────────────────────────────────

    /** After sign-in succeeds, re-check the original module (if any). */
    private suspend fun onSignInSuccess() {
        val currentState = _sheetState.value
        val moduleId = when (currentState) {
            is AccessSheetState.Login      -> currentState.moduleId
            is AccessSheetState.DailyLimit -> currentState.moduleId
            else -> { dismissOrShowTrialAfterFirstLogin(); return }
        }
        when (accessManager.checkAccess(moduleId)) {
            is AccessResult.Allowed          -> dismissOrShowTrialAfterFirstLogin()
            is AccessResult.SubscribeRequired -> {
                _sheetState.value = AccessSheetState.Paywall(moduleId)
                loadOfferings()
            }
            else -> dismissOrShowTrialAfterFirstLogin()
        }
    }

    private fun loadOfferings() {
        viewModelScope.launch {
            val offerings = purchaseManager.getOfferings()
            _packages.value = offerings?.current?.availablePackages ?: emptyList()
            _isTrialEligible.value = revenueCatManager.isEligibleForIntroOffer()
        }
    }

    // ── Trial offer proactive triggers ────────────────────────────────

    fun showTrialOfferIfNeededOnFirstLaunch() {
        if (!_isTrialEligible.value) return
        if (accessManager.userState.value !is UserAccessState.Guest) return
        if (appPrefs.isTrialOfferShownOnFirstLaunch()) return
        appPrefs.setTrialOfferShownOnFirstLaunch(true)
        viewModelScope.launch {
            delay(500)
            requestState(AccessSheetState.Paywall("first_launch"))
        }
    }

    private fun dismissOrShowTrialAfterFirstLogin() {
        if (accessManager.userState.value.isPremium ||
            appPrefs.isTrialOfferShownAfterFirstLogin() ||
            !_isTrialEligible.value) {
            dismiss()
            return
        }
        appPrefs.setTrialOfferShownAfterFirstLogin(true)
        _sheetState.value = AccessSheetState.Paywall("after_first_login")
        loadOfferings()
    }
}
