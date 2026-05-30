package com.example.myapplication.main.common.sheets

/**
 * Represents which bottom sheet is currently visible (or none).
 */
sealed class AccessSheetState {

    /** No sheet visible */
    object Hidden : AccessSheetState()

    /**
     * User hit their daily attempt limit for [moduleId].
     * [canUnlockWithLogin] = true  → they're a Guest; logging in gives more attempts.
     * [canUnlockWithLogin] = false → they're a FreeUser; only premium removes the limit.
     */
    data class DailyLimit(
        val moduleId: String,
        val canUnlockWithLogin: Boolean
    ) : AccessSheetState()

    /** Content requires a login (FreeUser tier). */
    data class Login(val moduleId: String) : AccessSheetState()

    /** Content requires a premium subscription. */
    data class Paywall(val moduleId: String) : AccessSheetState()

    /** Shown immediately after a successful premium purchase. */
    object PremiumCelebration : AccessSheetState()
}
