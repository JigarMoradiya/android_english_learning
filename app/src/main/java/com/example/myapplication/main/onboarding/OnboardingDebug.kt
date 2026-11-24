package com.example.myapplication.main.onboarding

/**
 * Review-only switch for debug builds: lets a tester open the onboarding at a given page
 * (optionally already solved) via launch extras, instead of replaying the whole flow.
 * Set from AgeCategoryActivity only when the app is debuggable; never used in release.
 */
object OnboardingDebug {
    var force = false
    var startPage = 0
    var forceDone = false
}
