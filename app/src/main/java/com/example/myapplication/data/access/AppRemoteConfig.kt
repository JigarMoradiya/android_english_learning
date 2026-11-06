package com.example.myapplication.data.access

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

/**
 * Single owner of the app's Firebase Remote Config defaults.
 *
 * `setDefaultsAsync` replaces the WHOLE defaults map rather than merging per key,
 * so every key the app reads from Remote Config (force-update's "android_version"
 * plus the daily free-activity limit) must be registered in this one call — a
 * second independent `setDefaultsAsync` call elsewhere would silently wipe
 * whichever key it doesn't mention. Call [registerDefaults] once, as early as
 * possible (MyApplication.onCreate), before anything reads a Remote Config value.
 */
object AppRemoteConfig {

    const val KEY_ANDROID_VERSION = "android_version"
    const val KEY_DAILY_ACTIVITY_LIMIT = "daily_free_activity_limit"
    const val DEFAULT_DAILY_ACTIVITY_LIMIT = 3L

    private const val MIN_FETCH_INTERVAL_SECONDS = 3600L

    fun registerDefaults() {
        FirebaseRemoteConfig.getInstance().apply {
            setConfigSettingsAsync(
                remoteConfigSettings {
                    minimumFetchIntervalInSeconds = MIN_FETCH_INTERVAL_SECONDS
                }
            )
            setDefaultsAsync(
                mapOf(
                    KEY_ANDROID_VERSION to 0L,
                    KEY_DAILY_ACTIVITY_LIMIT to DEFAULT_DAILY_ACTIVITY_LIMIT,
                )
            )
        }
    }

    /** Same daily free-activity limit for every user, guest or signed-in. */
    val dailyActivityLimit: Int
        get() {
            val value = FirebaseRemoteConfig.getInstance().getLong(KEY_DAILY_ACTIVITY_LIMIT)
            return if (value > 0) value.toInt() else DEFAULT_DAILY_ACTIVITY_LIMIT.toInt()
        }
}
