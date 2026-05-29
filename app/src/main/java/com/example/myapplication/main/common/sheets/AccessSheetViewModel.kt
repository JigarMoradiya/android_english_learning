package com.example.myapplication.main.common.sheets

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.access.AccessManager
import com.example.myapplication.data.access.AccessResult
import com.example.myapplication.data.access.UserAccessState
import com.example.myapplication.data.auth.AuthManager
import com.example.myapplication.data.auth.AuthResult
import com.example.myapplication.data.purchase.PurchaseManager
import com.revenuecat.purchases.Package
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
    private val purchaseManager: PurchaseManager
) : ViewModel() {

    // ── User access state (observable by UI) ─────────────────────────
    val userState: StateFlow<UserAccessState> = accessManager.userState

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

    // ── Pending purchase (set when guest taps Subscribe) ──────────────

    private var pendingPurchasePackage: Package? = null
    private var pendingPurchaseModuleId: String = ""

    // ── Pending restore (set when guest taps Restore) ─────────────────

    private var pendingRestoreAfterLogin = false

    fun requestLoginForRestore() {
        pendingRestoreAfterLogin = true
        requestState(AccessSheetState.Login("restore_from_settings"))
    }

    // ── Access check entry point ──────────────────────────────────────

    /**
     * Called before navigating to a module.
     * Returns true if allowed (caller should navigate).
     * Returns false if a sheet was shown (caller should NOT navigate).
     */
    suspend fun checkAccess(moduleId: String): Boolean {
        Log.d(TAG, "checkAccess → moduleId='$moduleId'")
        return when (val result = accessManager.checkAccess(moduleId)) {
            is AccessResult.Allowed -> {
                accessManager.recordAttempt(moduleId)
                true
            }
            is AccessResult.DailyLimitReached -> {
                _sheetState.value = AccessSheetState.DailyLimit(
                    moduleId = moduleId,
                    canUnlockWithLogin = result.canUnlockWithLogin
                )
                false
            }
            is AccessResult.LoginRequired -> {
                requestState(AccessSheetState.Login(moduleId))
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
        pendingPurchasePackage = null
        pendingRestoreAfterLogin = false
    }

    // ── Google Sign-In ────────────────────────────────────────────────

    fun signInWithGoogle(activity: Activity) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = authManager.signInWithGoogle(activity)) {
                is AuthResult.Success  -> onSignInSuccess(activity)
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
                is AuthResult.Success  -> onSignInSuccess(activity)
                is AuthResult.Cancelled -> Unit
                is AuthResult.Error    -> _message.emit(result.message)
                is AuthResult.NeedsAccountLinking ->
                    _message.emit("Account already exists with ${result.existingProvider}. Please use that to sign in.")
            }
            _isLoading.value = false
        }
    }

    // ── Purchase ──────────────────────────────────────────────────────

    /**
     * If the user is a guest, save the package and show Login first.
     * After login succeeds [onSignInSuccess] will transition to the paywall.
     * If already logged in, proceed with purchase directly.
     */
    fun purchase(activity: Activity, packageToPurchase: Package) {
        if (accessManager.userState.value is UserAccessState.Guest) {
            Log.d(TAG, "purchase → guest detected, requiring login first")
            pendingPurchasePackage = packageToPurchase
            val moduleId = (_sheetState.value as? AccessSheetState.Paywall)?.moduleId ?: pendingPurchaseModuleId
            pendingPurchaseModuleId = moduleId
            requestState(AccessSheetState.Login(moduleId))
            return
        }
        doPurchase(activity, packageToPurchase)
    }

    private fun doPurchase(activity: Activity, packageToPurchase: Package) {
        viewModelScope.launch {
            Log.d(TAG, "doPurchase → package=${packageToPurchase.identifier}")
            _isLoading.value = true
            when (val result = purchaseManager.purchase(activity, packageToPurchase)) {
                is PurchaseManager.PurchaseResult.Success   -> dismiss()
                is PurchaseManager.PurchaseResult.Cancelled -> Unit
                is PurchaseManager.PurchaseResult.Error     -> _message.emit(result.message)
            }
            _isLoading.value = false
        }
    }

    // ── Restore purchases ─────────────────────────────────────────────

    fun restorePurchases() {
        if (accessManager.userState.value is UserAccessState.Guest) {
            pendingRestoreAfterLogin = true
            requestState(AccessSheetState.Login("restore_from_paywall"))
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            val restored = purchaseManager.restorePurchases()
            if (restored) dismiss() else _message.emit("No previous purchases found.")
            _isLoading.value = false
        }
    }

    // ── Private helpers ───────────────────────────────────────────────

    /**
     * After sign-in succeeds:
     * - If there is a pending purchase → go back to paywall (user taps Subscribe again).
     * - Otherwise re-check the original module.
     */
    private suspend fun onSignInSuccess(activity: Activity) {
        // Pending purchase flow: auto-trigger purchase (user already tapped Subscribe before login)
        val pending = pendingPurchasePackage
        if (pending != null) {
            pendingPurchasePackage = null
            _sheetState.value = AccessSheetState.Paywall(pendingPurchaseModuleId)
            loadOfferings()
            // Sync RevenueCat after login — detects existing subscriptions before purchase
            purchaseManager.restorePurchases()
            if (accessManager.userState.value is UserAccessState.PremiumUser) {
                dismiss()
                return
            }
            doPurchase(activity, pending)
            return
        }

        // Pending restore flow: run restore now that user is logged in
        if (pendingRestoreAfterLogin) {
            pendingRestoreAfterLogin = false
            _isLoading.value = true
            val restored = purchaseManager.restorePurchases()
            _isLoading.value = false
            if (restored) dismiss() else _message.emit("No previous purchases found.")
            return
        }

        val currentState = _sheetState.value
        val moduleId = when (currentState) {
            is AccessSheetState.Login      -> currentState.moduleId
            is AccessSheetState.DailyLimit -> currentState.moduleId
            else -> { dismiss(); return }
        }
        when (accessManager.checkAccess(moduleId)) {
            is AccessResult.Allowed          -> dismiss()
            is AccessResult.SubscribeRequired -> {
                _sheetState.value = AccessSheetState.Paywall(moduleId)
                loadOfferings()
            }
            else -> dismiss()
        }
    }

    private fun loadOfferings() {
        viewModelScope.launch {
            val offerings = purchaseManager.getOfferings()
            _packages.value = offerings?.current?.availablePackages ?: emptyList()
        }
    }
}
