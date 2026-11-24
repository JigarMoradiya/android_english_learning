package com.example.myapplication.data.access

/**
 * Represents the current user's subscription state. There is no sign-in:
 * RevenueCat (anonymous ID + the Play account) is the only source of truth.
 *
 * Free    → no active subscription, daily limits apply
 * Premium → active "premium" entitlement, full access
 *
 * Every screen/ViewModel reads this state from [AccessManager].
 * No screen should check RevenueCat directly.
 */
sealed class UserAccessState {

    /** No active subscription. Limited free access + daily limits apply. */
    object Free : UserAccessState()

    /** Active subscription. Full access to all content. */
    object Premium : UserAccessState()

    // ── Convenience helpers ──────────────────────────────────────────

    val isPremium: Boolean
        get() = this is Premium
}
