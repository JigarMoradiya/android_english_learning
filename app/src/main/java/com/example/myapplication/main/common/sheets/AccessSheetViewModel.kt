package com.example.myapplication.main.common.sheets

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.access.AccessManager
import com.example.myapplication.data.access.AccessResult
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

    // ── Sheet visibility state ────────────────────────────────────────

    private val _sheetState = MutableStateFlow<AccessSheetState>(AccessSheetState.Hidden)
    val sheetState: StateFlow<AccessSheetState> = _sheetState.asStateFlow()

    // ── Loading (purchase / sign-in in progress) ──────────────────────

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // ── One-shot toast messages ───────────────────────────────────────

    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message.asSharedFlow()

    // ── Available RevenueCat packages (loaded when paywall opens) ─────

    private val _packages = MutableStateFlow<List<Package>>(emptyList())
    val packages: StateFlow<List<Package>> = _packages.asStateFlow()

    // ── Access check entry point ──────────────────────────────────────

    /**
     * Called before navigating to a module.
     * Returns true if the module is allowed (caller should proceed with navigation).
     * Returns false if a sheet was shown (caller should NOT navigate).
     */
    suspend fun checkAccess(moduleId: String): Boolean {
        Log.d(TAG, "checkAccess called → moduleId='$moduleId'")
        return when (val result = accessManager.checkAccess(moduleId)) {
            is AccessResult.Allowed -> {
                Log.d(TAG, "checkAccess → Allowed, recording attempt and proceeding to navigate")
                accessManager.recordAttempt(moduleId)   // ← increment daily counter
                true
            }

            is AccessResult.DailyLimitReached -> {
                Log.d(TAG, "checkAccess → DailyLimitReached | canUnlockWithLogin=${result.canUnlockWithLogin}")
                _sheetState.value = AccessSheetState.DailyLimit(
                    moduleId = moduleId,
                    canUnlockWithLogin = result.canUnlockWithLogin
                )
                false
            }

            is AccessResult.LoginRequired -> {
                Log.d(TAG, "checkAccess → LoginRequired, showing Login sheet")
                _sheetState.value = AccessSheetState.Login(moduleId)
                false
            }

            is AccessResult.SubscribeRequired -> {
                Log.d(TAG, "checkAccess → SubscribeRequired, showing Paywall sheet")
                _sheetState.value = AccessSheetState.Paywall(moduleId)
                loadOfferings()
                false
            }
        }
    }

    fun dismiss() {
        Log.d(TAG, "dismiss → hiding sheet")
        _sheetState.value = AccessSheetState.Hidden
    }

    // ── Google Sign-In ────────────────────────────────────────────────

    fun signInWithGoogle(activity: Activity) {
        viewModelScope.launch {
            Log.d(TAG, "signInWithGoogle → starting")
            _isLoading.value = true
            when (val result = authManager.signInWithGoogle(activity)) {
                is AuthResult.Success  -> { Log.d(TAG, "signInWithGoogle → SUCCESS uid=${result.userId}"); onSignInSuccess() }
                is AuthResult.Cancelled -> Log.d(TAG, "signInWithGoogle → Cancelled by user")
                is AuthResult.Error    -> { Log.e(TAG, "signInWithGoogle → Error: ${result.message}"); _message.emit(result.message) }
                is AuthResult.NeedsAccountLinking -> {
                    Log.w(TAG, "signInWithGoogle → NeedsAccountLinking with ${result.existingProvider}")
                    _message.emit("Account already exists with ${result.existingProvider}. Please use that to sign in.")
                }
            }
            _isLoading.value = false
        }
    }

    // ── Apple Sign-In ─────────────────────────────────────────────────

    fun signInWithApple(activity: Activity) {
        viewModelScope.launch {
            Log.d(TAG, "signInWithApple → starting")
            _isLoading.value = true
            when (val result = authManager.signInWithApple(activity)) {
                is AuthResult.Success  -> { Log.d(TAG, "signInWithApple → SUCCESS uid=${result.userId}"); onSignInSuccess() }
                is AuthResult.Cancelled -> Log.d(TAG, "signInWithApple → Cancelled by user")
                is AuthResult.Error    -> { Log.e(TAG, "signInWithApple → Error: ${result.message}"); _message.emit(result.message) }
                is AuthResult.NeedsAccountLinking -> {
                    Log.w(TAG, "signInWithApple → NeedsAccountLinking with ${result.existingProvider}")
                    _message.emit("Account already exists with ${result.existingProvider}. Please use that to sign in.")
                }
            }
            _isLoading.value = false
        }
    }

    // ── Purchase ──────────────────────────────────────────────────────

    fun purchase(activity: Activity, packageToPurchase: Package) {
        viewModelScope.launch {
            Log.d(TAG, "purchase → starting | package=${packageToPurchase.identifier}")
            _isLoading.value = true
            when (val result = purchaseManager.purchase(activity, packageToPurchase)) {
                is PurchaseManager.PurchaseResult.Success   -> { Log.d(TAG, "purchase → SUCCESS"); dismiss() }
                is PurchaseManager.PurchaseResult.Cancelled -> Log.d(TAG, "purchase → Cancelled by user")
                is PurchaseManager.PurchaseResult.Error     -> { Log.e(TAG, "purchase → Error: ${result.message}"); _message.emit(result.message) }
            }
            _isLoading.value = false
        }
    }

    // ── Restore purchases ─────────────────────────────────────────────

    fun restorePurchases() {
        viewModelScope.launch {
            Log.d(TAG, "restorePurchases → starting")
            _isLoading.value = true
            val restored = purchaseManager.restorePurchases()
            Log.d(TAG, "restorePurchases → restored=$restored")
            if (restored) dismiss()
            else _message.emit("No previous purchases found.")
            _isLoading.value = false
        }
    }

    // ── Private helpers ───────────────────────────────────────────────

    /**
     * After sign-in succeeds, re-check the original module.
     * If access is now allowed → dismiss the sheet.
     * If premium is still required → transition to the Paywall sheet.
     */
    private suspend fun onSignInSuccess() {
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
