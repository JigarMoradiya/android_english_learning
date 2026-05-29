package com.example.myapplication.data.access

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "AccessManager"

/**
 * ⚠️ DEVELOPER MODE — set to true to bypass all access gates during development.
 * Every module behaves as if the user has an active premium subscription.
 * MUST be false before releasing to production.
 */
private const val DEV_MODE = false

/**
 * The single source of truth for access control.
 *
 * Every ViewModel calls [checkAccess] — no screen ever talks to
 * Firebase or RevenueCat directly.
 *
 * Usage in ViewModel:
 *   val result = accessManager.checkAccess(ModuleID.COLORING_ALPHABETS)
 *   when (result) {
 *       is AccessResult.Allowed           -> launch activity
 *       is AccessResult.DailyLimitReached -> show "come back tomorrow" dialog
 *       is AccessResult.LoginRequired     -> show login prompt
 *       is AccessResult.SubscribeRequired -> show paywall
 *   }
 */
@Singleton
class AccessManager @Inject constructor(
    private val dailyLimitManager: DailyLimitManager
) {

    // ── User state ────────────────────────────────────────────────────

    private val _userState = MutableStateFlow<UserAccessState>(UserAccessState.Guest)
    val userState: StateFlow<UserAccessState> = _userState.asStateFlow()

    /** Called by AuthManager after login/logout/subscription change. */
    fun updateUserState(state: UserAccessState) {
        Log.d(TAG, "updateUserState → $state")
        _userState.value = state
    }

    // ── Access check ──────────────────────────────────────────────────

    /**
     * Checks whether the current user can access [moduleId].
     *
     * This is a suspend function because FREE_LIMITED modules require
     * reading the daily counter from DataStore.
     */
    suspend fun checkAccess(moduleId: String): AccessResult {
        val config = AccessConfig.get(moduleId)
            ?: run {
                Log.w(TAG, "checkAccess → moduleId='$moduleId' not in AccessConfig, allowing by default")
                return AccessResult.Allowed
            }

        val state = _userState.value
        Log.d(TAG, "checkAccess → moduleId='$moduleId' | level=${config.accessLevel} | userState=$state")

        // Developer mode — treat everything as premium
        if (DEV_MODE) {
            Log.w(TAG, "checkAccess → ALLOWED (DEV_MODE is ON — disable before release!)")
            return AccessResult.Allowed
        }

        // Premium users bypass all restrictions
        if (state.isPremium) {
            Log.d(TAG, "checkAccess → ALLOWED (premium user)")
            return AccessResult.Allowed
        }

        val result = when (config.accessLevel) {

            AccessLevel.FREE -> AccessResult.Allowed

            AccessLevel.FREE_LIMITED -> {
                if (!dailyLimitManager.hasAttemptsLeft(state.isLoggedIn)) {
                    return AccessResult.DailyLimitReached(remaining = 0, canUnlockWithLogin = !state.isLoggedIn)
                }
                AccessResult.Allowed
            }

            AccessLevel.LOGIN_REQUIRED -> {
                if (state.isLoggedIn) AccessResult.Allowed
                else AccessResult.LoginRequired
            }

            AccessLevel.PREMIUM -> AccessResult.SubscribeRequired
        }

        Log.d(TAG, "checkAccess → RESULT: $result")
        return result
    }

    /** Call this when the user actually starts a FREE_LIMITED activity. */
    suspend fun recordAttempt(moduleId: String) {
        val config = AccessConfig.get(moduleId) ?: return
        if (config.accessLevel == AccessLevel.FREE_LIMITED) {
            dailyLimitManager.recordModulePlayed(moduleId)
            Log.d(TAG, "recordAttempt → moduleId='$moduleId'")
        }
    }

    /** Returns global remaining activity slots today for FREE_LIMITED modules. */
    suspend fun remainingAttempts(moduleId: String): Int {
        val config = AccessConfig.get(moduleId) ?: return Int.MAX_VALUE
        if (config.accessLevel != AccessLevel.FREE_LIMITED) return Int.MAX_VALUE
        val state = _userState.value
        if (state.isPremium) return Int.MAX_VALUE
        return dailyLimitManager.remainingAttempts(state.isLoggedIn)
    }
}

// ── Result types ──────────────────────────────────────────────────────────────

sealed class AccessResult {

    /** User can proceed. */
    object Allowed : AccessResult()

    /**
     * User has hit their daily free limit.
     *
     * @param remaining          Always 0 — for future use.
     * @param canUnlockWithLogin If true, logging in gives a higher daily limit.
     */
    data class DailyLimitReached(
        val remaining: Int,
        val canUnlockWithLogin: Boolean
    ) : AccessResult()

    /** Module requires login. Show sign-in prompt. */
    object LoginRequired : AccessResult()

    /** Module requires an active subscription. Show paywall. */
    object SubscribeRequired : AccessResult()
}
