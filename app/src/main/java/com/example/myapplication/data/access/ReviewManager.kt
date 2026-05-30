package com.example.myapplication.data.access

import android.app.Activity
import android.util.Log
import com.example.myapplication.utilities.pref.AppPreferencesHelper
import android.content.pm.ApplicationInfo
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

private const val TAG = "ReviewManager"

/**
 * Single source of truth for review/rating prompt logic.
 *
 * Rules:
 *   - Each milestone fires only once (stored permanently in AppPreferencesHelper)
 *   - Pending trigger: milestone that hit during cooldown waits here
 *   - 7-day cooldown between any two asks — no asks during this window
 *   - hasPendingReview is checked when ParentProgressScreen appears
 *
 * To add new milestones — just add a number to the list:
 *   streakMilestones   = listOf(3, 7, 14, 30, 60)
 *   activityMilestones = listOf(5, 10, 20, 50, 75, 100)
 *
 * Called by:
 *   SessionRepository → onActivityCompleted(totalCount)
 *   PurchaseManager   → onPremiumPurchased()
 */
@Singleton
class ReviewManager @Inject constructor(
    private val prefs: AppPreferencesHelper
) {

    // ── Milestones (add/remove numbers freely) ────────────────────────

    private val streakMilestones   = listOf(3, 7, 14, 30)
    private val activityMilestones = listOf(5, 10, 20, 50, 100, 150, 200)

    private val cooldownDays = 7

    // ── Called once on app start to silently mark already-exceeded milestones ──

    fun bootstrapActivityMilestones(totalCount: Int) {
        val bootstrapKey = "review_activity_bootstrap_done"
        if (prefs.getCustomParamBoolean(bootstrapKey, false)) return
        for (count in activityMilestones) {
            val key = AppPreferencesHelper.reviewActivityKey(count)
            if (totalCount >= count && !prefs.getCustomParamBoolean(key, false)) {
                prefs.setCustomParamBoolean(key, true) // mark done, no trigger
                Log.d(TAG, "bootstrap: activity milestone $count already exceeded, marked silently")
            }
        }
        prefs.setCustomParamBoolean(bootstrapKey, true)
    }

    fun bootstrapStreakMilestones(currentStreak: Int) {
        val bootstrapKey = "review_streak_bootstrap_done"
        if (prefs.getCustomParamBoolean(bootstrapKey, false)) return
        for (days in streakMilestones) {
            val key = AppPreferencesHelper.reviewStreakKey(days)
            if (currentStreak >= days && !prefs.getCustomParamBoolean(key, false)) {
                prefs.setCustomParamBoolean(key, true) // mark done, no trigger
                Log.d(TAG, "bootstrap: streak milestone $days already exceeded, marked silently")
            }
        }
        prefs.setCustomParamBoolean(bootstrapKey, true)
    }

    // ── Dev only ──────────────────────────────────────────────────────────────

    fun clearStreakMilestone(days: Int) {
        prefs.setCustomParamBoolean(AppPreferencesHelper.reviewStreakKey(days), false)
        prefs.setCustomParam(AppPreferencesHelper.KEY_REVIEW_PENDING_TRIGGER, "")
        prefs.setCustomParam(AppPreferencesHelper.KEY_REVIEW_LAST_ASK_DATE, "")
    }

    fun clearActivityMilestone(count: Int) {
        prefs.setCustomParamBoolean(AppPreferencesHelper.reviewActivityKey(count), false)
        prefs.setCustomParam(AppPreferencesHelper.KEY_REVIEW_PENDING_TRIGGER, "")
        prefs.setCustomParam(AppPreferencesHelper.KEY_REVIEW_LAST_ASK_DATE, "")
    }

    // ── Clears any trigger queued during bootstrap (first-install artifact) ──

    fun clearStaleBootstrapTrigger() {
        val bootstrapKey = "review_bootstrap_done"
        if (!prefs.getCustomParamBoolean(bootstrapKey, false)) {
            prefs.setCustomParam(AppPreferencesHelper.KEY_REVIEW_PENDING_TRIGGER, "")
            prefs.setCustomParamBoolean(bootstrapKey, true)
            Log.d(TAG, "bootstrap: cleared stale pending trigger from first install")
        }
    }

    // ── Called by SessionRepository ───────────────────────────────────

    fun onActivityCompleted(totalCount: Int) {
        for (count in activityMilestones) {
            val key = AppPreferencesHelper.reviewActivityKey(count)
            if (totalCount >= count && !prefs.getCustomParamBoolean(key, false)) {
                prefs.setCustomParamBoolean(key, true)
                queueTrigger(key)
                Log.d(TAG, "activity milestone reached: $count")
            }
        }
    }

    // ── Called by StreakRepository (when implemented on Android) ──────

    fun onStreakUpdated(streak: Int) {
        for (days in streakMilestones) {
            val key = AppPreferencesHelper.reviewStreakKey(days)
            if (streak >= days && !prefs.getCustomParamBoolean(key, false)) {
                prefs.setCustomParamBoolean(key, true)
                queueTrigger(key)
                Log.d(TAG, "streak milestone reached: $days days")
            }
        }
    }

    // ── Called by PurchaseManager ─────────────────────────────────────

    fun onPremiumPurchased() {
        prefs.setCustomParamBoolean(AppPreferencesHelper.KEY_REVIEW_PREMIUM_PENDING, true)
        Log.d(TAG, "premium purchased — review pending")
    }

    // ── Banner content (derived from pending trigger key) ─────────────

    val bannerTitle: String
        get() {
            if (hasPremiumReviewPending) return "Welcome to Premium! 🎉"
            val key = prefs.getCustomParam(AppPreferencesHelper.KEY_REVIEW_PENDING_TRIGGER, "")
            return when {
                "streak_" in key   -> "🔥 ${key.substringAfterLast("_")}-Day Streak!"
                "activity_" in key -> "⭐ ${key.substringAfterLast("_")} Activities Done!"
                else               -> "Enjoying the app? 🌟"
            }
        }

    val bannerSubtitle: String
        get() {
            if (hasPremiumReviewPending) return "You're helping your child grow 🌱\nMind leaving a quick review?"
            val key = prefs.getCustomParam(AppPreferencesHelper.KEY_REVIEW_PENDING_TRIGGER, "")
            if ("activity_" in key) return "Your child is crushing it! 🚀\nMind leaving a quick review?"
            return "Is your child enjoying the app?"
        }

    // ── ParentProgressScreen checks these ─────────────────────────────

    val hasPendingReview: Boolean
        get() = prefs.getCustomParam(AppPreferencesHelper.KEY_REVIEW_PENDING_TRIGGER, "").isNotEmpty()
                && cooldownPassed()

    val hasPremiumReviewPending: Boolean
        get() = prefs.getCustomParamBoolean(AppPreferencesHelper.KEY_REVIEW_PREMIUM_PENDING, false)

    // ── Call after showing the banner ─────────────────────────────────

    fun markBannerShown() {
        prefs.setCustomParam(AppPreferencesHelper.KEY_REVIEW_PENDING_TRIGGER, "")
        prefs.setCustomParamBoolean(AppPreferencesHelper.KEY_REVIEW_PREMIUM_PENDING, false)
        prefs.setCustomParam(AppPreferencesHelper.KEY_REVIEW_LAST_ASK_DATE, System.currentTimeMillis().toString())
        Log.d(TAG, "banner shown — cooldown started")
    }

    // ── Call when parent taps "Yes, Love it!" ─────────────────────────

    suspend fun requestNativeReview(activity: Activity) {
//        val isDebug = activity.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
//        if (isDebug) {
//            // FakeReviewManager in Play Core 2.x completes silently on physical devices.
//            // Show a Toast so debug builds get visual confirmation the flow ran correctly.
//            android.widget.Toast.makeText(
//                activity,
//                "✅ Review flow triggered (debug — no dialog on physical device)",
//                android.widget.Toast.LENGTH_LONG
//            ).show()
//            Log.d(TAG, "debug: review flow simulated via toast")
//            return
//        }
        try {
            val manager = ReviewManagerFactory.create(activity)
            val reviewInfo = suspendCancellableCoroutine<ReviewInfo?> { cont ->
                manager.requestReviewFlow().addOnCompleteListener { task ->
                    if (task.isSuccessful) cont.resume(task.result)
                    else cont.resume(null)
                }
            } ?: run {
                Log.w(TAG, "requestReviewFlow failed — Play Store may throttle")
                return
            }
            suspendCancellableCoroutine { cont ->
                manager.launchReviewFlow(activity, reviewInfo)
                    .addOnCompleteListener { cont.resume(Unit) }
            }
            Log.d(TAG, "native review flow launched")
        } catch (e: Exception) {
            Log.e(TAG, "requestNativeReview failed: ${e.message}")
        }
    }

    // ── Private ───────────────────────────────────────────────────────

    private fun queueTrigger(key: String) {
        val existing = prefs.getCustomParam(AppPreferencesHelper.KEY_REVIEW_PENDING_TRIGGER, "")
        // Streak has priority — never let an activity trigger overwrite a pending streak trigger
        if ("streak_" in existing && "activity_" in key) return
        // Within same category, last write wins (loops lowest→highest, so highest milestone wins)
        prefs.setCustomParam(AppPreferencesHelper.KEY_REVIEW_PENDING_TRIGGER, key)
    }

    private fun cooldownPassed(): Boolean {
        val lastAskStr = prefs.getCustomParam(AppPreferencesHelper.KEY_REVIEW_LAST_ASK_DATE, "")
        val lastAsk = lastAskStr.toLongOrNull() ?: return true
        val diffDays = (System.currentTimeMillis() - lastAsk) / (1000L * 60 * 60 * 24)
        return diffDays >= cooldownDays
    }
}
