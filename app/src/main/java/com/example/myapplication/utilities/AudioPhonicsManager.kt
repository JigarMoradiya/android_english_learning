package com.example.myapplication.utilities

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioPhonicsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var player: MediaPlayer? = null
    var onAudioCompleted: (() -> Unit)? = null

    // Audio files are named without apostrophes and fully lowercase
    // ("don't" → dont.mp3, "I'm" → im.mp3) — normalize every lookup.
    private fun sanitized(fileName: String): String =
        fileName.replace("'", "").replace("’", "").lowercase()

    fun audioExists(fileName: String): Boolean {
        return try {
            context.assets.open("${sanitized(fileName)}.mp3").close()
            true
        } catch (_: Exception) {
            false
        }
    }

    fun playPhonicsSound(fileName: String) {
        try {
            stop()
            val afd = context.assets.openFd("${sanitized(fileName)}.mp3")
            player = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                setOnCompletionListener {
                    onAudioCompleted?.invoke()
                }
                prepare()
                start()
            }
        } catch (e: Exception) {
            Log.e("AudioPhonicsManager", "Failed to play: $fileName — ${e.message}")
        }
    }

    fun stop() {
        try {
            player?.stop()
            player?.release()
        } catch (_: Exception) {}
        player = null
        onAudioCompleted = null
    }
}
