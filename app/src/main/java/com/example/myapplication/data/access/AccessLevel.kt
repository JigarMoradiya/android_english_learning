package com.example.myapplication.data.access

/**
 * Defines who can access a module and how.
 *
 * FREE            → always accessible, no limit
 * FREE_LIMITED    → accessible to all; counts toward the global daily activity limit
 *                   (same limit for every user, sourced from Remote Config).
 * PREMIUM         → active subscription required
 */
enum class AccessLevel {
    FREE,
    FREE_LIMITED,
    PREMIUM
}

/** Describes a single module's access rules. */
data class ModuleAccess(
    val moduleId: String,
    val accessLevel: AccessLevel
)
