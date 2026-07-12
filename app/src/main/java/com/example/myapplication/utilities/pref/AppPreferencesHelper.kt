package com.example.myapplication.utilities.pref

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject


class AppPreferencesHelper @Inject constructor(
    context: Context,
    @param:PreferenceInfo private val prefFileName: String
) : PreferencesHelper {
    companion object {

        const val KEY_DEFAULT_TTS_LANGUAGE = "KEY_DEFAULT_TTS_LANGUAGE"
        const val DEFAULT_TTS_LANGUAGE_VALUE = "en_IN" // hi_IN, en_US

        const val KEY_DEFAULT_TTS_PITCH = "KEY_DEFAULT_TTS_PITCH"
        const val KEY_DEFAULT_TTS_SPEECH = "KEY_DEFAULT_TTS_SPEECH"

        const val KEY_BG_MUSIC_VOLUME = "KEY_BG_MUSIC_VOLUME"
        const val KEY_SIGHT_WORD_INDEX = "sight_word_index"

        // trial offer tracking
        const val KEY_TRIAL_OFFER_SHOWN_FIRST_LAUNCH  = "trial_offer_shown_first_launch"
        const val KEY_TRIAL_OFFER_SHOWN_AFTER_LOGIN    = "trial_offer_shown_after_first_login"

        // review / rating
        const val KEY_REVIEW_LAST_ASK_DATE    = "review_last_ask_date"
        const val KEY_REVIEW_PENDING_TRIGGER  = "review_pending_trigger"
        const val KEY_REVIEW_PREMIUM_PENDING  = "review_premium_pending"
        fun reviewStreakKey(days: Int)         = "review_milestone_streak_$days"
        fun reviewActivityKey(count: Int)     = "review_milestone_activity_$count"

        // phonics listen — one progress index per level
        fun phonicsListenIndexKey(level: String) = "phonics_listen_${level}_index"
    }

    private val mPrefs: SharedPreferences =
        context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)


    override fun getCustomParam(paramName: String, defaultValue: String): String =
        mPrefs.getString(paramName, defaultValue).toString()

    override fun setCustomParam(paramName: String, paramValue: String) = mPrefs.edit {
        putString(paramName, paramValue)
    }

    override fun getCustomParamInt(paramName: String, defaultValue: Int): Int =
        mPrefs.getInt(paramName, defaultValue)

    override fun setCustomParamInt(paramName: String, paramValue: Int) = mPrefs.edit {
        putInt(paramName, paramValue)
    }

    override fun getCustomParamBoolean(paramName: String, defaultValue: Boolean): Boolean =
        mPrefs.getBoolean(paramName, defaultValue)

    override fun setCustomParamBoolean(paramName: String, paramValue: Boolean) = mPrefs.edit {
        putBoolean(paramName, paramValue)
    }

    override fun getCustomParamFloat(paramName: String, defaultValue: Float): Float =
        mPrefs.getFloat(paramName, defaultValue)

    override fun setCustomParamFloat(paramName: String, paramValue: Float) = mPrefs.edit {
        putFloat(paramName, paramValue)
    }


    fun isTrialOfferShownOnFirstLaunch(): Boolean = getCustomParamBoolean(KEY_TRIAL_OFFER_SHOWN_FIRST_LAUNCH, false)
    fun setTrialOfferShownOnFirstLaunch(shown: Boolean) = setCustomParamBoolean(KEY_TRIAL_OFFER_SHOWN_FIRST_LAUNCH, shown)

    fun isTrialOfferShownAfterFirstLogin(): Boolean = getCustomParamBoolean(KEY_TRIAL_OFFER_SHOWN_AFTER_LOGIN, false)
    fun setTrialOfferShownAfterFirstLogin(shown: Boolean) = setCustomParamBoolean(KEY_TRIAL_OFFER_SHOWN_AFTER_LOGIN, shown)

    fun clearTrialOfferFlags() {
        setTrialOfferShownOnFirstLaunch(false)
        setTrialOfferShownAfterFirstLogin(false)
    }

    fun getMusicVolume(): Float = getCustomParamFloat(KEY_BG_MUSIC_VOLUME, 0.05f)
    fun setMusicVolume(volume: Float) = setCustomParamFloat(KEY_BG_MUSIC_VOLUME, volume)

    fun getDefaultTTSPitch(): Float {
        return getCustomParamFloat(KEY_DEFAULT_TTS_PITCH, 10f)
    }

    fun getDefaultTTSSpeed(): Float {
        return getCustomParamFloat(KEY_DEFAULT_TTS_SPEECH, 9f)
    }

    fun clearPref(){
        mPrefs.edit { clear() }
    }
}
