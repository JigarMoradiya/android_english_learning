package com.example.myapplication.utilities

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.util.Log
import androidx.core.content.edit
import com.example.myapplication.utilities.pref.AppPreferencesHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BGMusicManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("vedaaviEnglish", Context.MODE_PRIVATE)

    private var player: MediaPlayer? = null

    fun start() {
        if (player != null) {
            resume()
            return
        }
        try {
            val vol = getVolume()
            val afd = context.assets.openFd("bg_music_sand_castle.mp3")
            player = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                isLooping = true
                setVolume(vol, vol)
                prepare()
                start()
            }
        } catch (e: Exception) {
            Log.e("BGMusicManager", "Failed to start: ${e.message}")
        }
    }

    fun resume() {
        player?.let { if (!it.isPlaying) it.start() }
    }

    fun pause() {
        player?.let { if (it.isPlaying) it.pause() }
    }

    fun stop() {
        try {
            player?.stop()
            player?.release()
        } catch (_: Exception) {}
        player = null
    }

    fun setVolume(volume: Float) {
        prefs.edit { putFloat(AppPreferencesHelper.KEY_BG_MUSIC_VOLUME, volume) }
        player?.setVolume(volume, volume)
    }

    fun getVolume(): Float = prefs.getFloat(AppPreferencesHelper.KEY_BG_MUSIC_VOLUME, 0.05f)
}
