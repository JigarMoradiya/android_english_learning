package com.example.myapplication.utilities

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.example.myapplication.utilities.pref.AppPreferencesHelper
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

class TextToSpeechManager @Inject constructor(
    context: Context,
    private val prefs: AppPreferencesHelper
) {

    private var tts: TextToSpeech? = null
    private var ready = false
    private var pending: Pair<String, String>? = null
    var onWordSpoken: ((start: Int) -> Unit)? = null

    // 🔹 Optional callback (NOT always used)
    private val utteranceCallbacks =
        mutableMapOf<String, (String?) -> Unit>()

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ready = true
                setupListener()
                pending?.let { (text, id) ->
                    speak(text, id)
                    pending = null
                }
            }
        }
    }
    private fun setupListener() {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {

            override fun onStart(utteranceId: String?) = Unit

            override fun onDone(utteranceId: String?) {
                utteranceId?.let { id ->
                    utteranceCallbacks[id]?.invoke(id)
                    utteranceCallbacks.remove(id)
                }
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) = Unit

            override fun onError(utteranceId: String?, errorCode: Int) = Unit

            override fun onRangeStart(
                utteranceId: String?,
                start: Int,
                end: Int,
                frame: Int
            ) {
                if (utteranceId == "joined") {
                    onWordSpoken?.invoke(start)
                }
            }
        })
    }


    fun speak(
        text: String,
        utteranceId: String = "",
        isAddInQueue : Boolean = false,
        onDone: (() -> Unit)? = null
    ) {
        if (!ready) {
            pending = text to utteranceId
            return
        }

        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        }

        onDone?.let {
            utteranceCallbacks[utteranceId] = { it() }
        }

        tts?.speak(
            text,
            if (isAddInQueue)TextToSpeech.QUEUE_ADD else TextToSpeech.QUEUE_FLUSH,
            params,
            utteranceId
        )
    }


    fun applySettings() {
        val currentLanguageJson = prefs.getCustomParam(AppPreferencesHelper.KEY_DEFAULT_TTS_LANGUAGE, "")

        val locale = if (currentLanguageJson.isEmpty()) {
            Locale.forLanguageTag(AppPreferencesHelper.DEFAULT_TTS_LANGUAGE_VALUE)
        } else {
            Gson().fromJson(currentLanguageJson, Locale::class.java)
        }
        val speed = prefs.getDefaultTTSSpeed()
        val pitch = prefs.getDefaultTTSPitch()

        // Set language
        val langResult = tts?.setLanguage(locale)

        if (langResult == TextToSpeech.LANG_MISSING_DATA ||
            langResult == TextToSpeech.LANG_NOT_SUPPORTED
        ) {
            Log.e("TTS", "Language not supported: $locale")
            tts?.language = Locale.forLanguageTag(AppPreferencesHelper.DEFAULT_TTS_LANGUAGE_VALUE)
            return
        }

        tts?.setSpeechRate(speed/10f)
        tts?.setPitch(pitch/10f)
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }

    fun stop() {
        tts?.stop()
        utteranceCallbacks.clear()
    }
}

