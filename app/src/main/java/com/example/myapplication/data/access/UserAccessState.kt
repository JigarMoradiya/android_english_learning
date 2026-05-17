package com.example.myapplication.data.access

/**
 * Represents the current user's authentication and subscription state.
 *
 * Guest       → not logged in
 * FreeUser    → logged in, no active subscription
 * PremiumUser → logged in + active subscription
 *
 * Every screen/ViewModel reads this state from [AccessManager].
 * No screen should check Firebase or RevenueCat directly.
 */
sealed class UserAccessState {

    /** Not logged in. Limited free access + daily limits apply. */
    object Guest : UserAccessState()

    /**
     * Logged in but no active subscription.
     * Same content access as Guest but higher daily limits
     * and ability to restore a previous purchase.
     *
     * @param userId Firebase UID — used to identify the user in RevenueCat.
     */
    data class FreeUser(override val userId: String) : UserAccessState()

    /**
     * Logged in with an active subscription. Full access to all content.
     *
     * @param userId Firebase UID.
     */
    data class PremiumUser(override val userId: String) : UserAccessState()

    // ── Convenience helpers ──────────────────────────────────────────

    val isLoggedIn: Boolean
        get() = this is FreeUser || this is PremiumUser

    val isPremium: Boolean
        get() = this is PremiumUser

    // Declared open so FreeUser/PremiumUser can override with a non-null String.
    // Guest falls back to the default null.
    open val userId: String?
        get() = null
}
