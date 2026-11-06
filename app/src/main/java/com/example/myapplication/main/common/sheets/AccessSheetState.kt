package com.example.myapplication.main.common.sheets

/**
 * Represents which bottom sheet is currently visible (or none).
 */
sealed class AccessSheetState {

    /** No sheet visible */
    object Hidden : AccessSheetState()

    /** User hit their daily attempt limit for [moduleId]. Only premium removes it. */
    data class DailyLimit(
        val moduleId: String
    ) : AccessSheetState()

    /** Optional sign-in, reached only from Settings — nothing requires it. */
    data class Login(val moduleId: String) : AccessSheetState()

    /** Content requires a premium subscription. */
    data class Paywall(val moduleId: String) : AccessSheetState()

    /** Shown immediately after a successful premium purchase. */
    object PremiumCelebration : AccessSheetState()
}
