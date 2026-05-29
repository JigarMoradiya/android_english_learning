package com.example.myapplication.data.access

/**
 * Defines who can access a module and how.
 *
 * FREE            → always accessible, no login, no limit
 * FREE_LIMITED    → accessible to all; counts toward the global daily activity limit
 *                   (guest: 1/day, free login: 3/day). Same module cannot be replayed same day.
 * LOGIN_REQUIRED  → must be logged in; no subscription needed
 * PREMIUM         → active subscription required
 */
enum class AccessLevel {
    FREE,
    FREE_LIMITED,
    LOGIN_REQUIRED,
    PREMIUM
}

/** Describes a single module's access rules. */
data class ModuleAccess(
    val moduleId: String,
    val accessLevel: AccessLevel
)
