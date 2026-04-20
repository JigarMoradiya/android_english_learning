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
