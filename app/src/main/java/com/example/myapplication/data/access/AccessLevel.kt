package com.example.myapplication.data.access

/**
 * Defines who can access a module and how.
 *
 * FREE            → always accessible, no login, no limit
 * FREE_LIMITED    → accessible to all, but capped per day
 *                   (guest: guestDailyLimit, logged-in: loginDailyLimit)
 * LOGIN_REQUIRED  → must be logged in; no subscription needed
 * PREMIUM         → active subscription required
 */
enum class AccessLevel {
    FREE,
    FREE_LIMITED,
    LOGIN_REQUIRED,
    PREMIUM
}

/**
 * Describes a single module's access rules.
 *
 * @param moduleId         Matches a constant in [ModuleID]
 * @param accessLevel      The base access rule for this module
 * @param guestDailyLimit  Max attempts/day for a guest (only for FREE_LIMITED)
 * @param loginDailyLimit  Max attempts/day for a logged-in free user (only for FREE_LIMITED)
 */
data class ModuleAccess(
    val moduleId: String,
    val accessLevel: AccessLevel,
    val guestDailyLimit: Int = 0,
    val loginDailyLimit: Int = 0
)
