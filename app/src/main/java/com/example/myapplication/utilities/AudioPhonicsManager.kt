package com.example.myapplication.utilities

import android.content.Context
import android.media.MediaMetadataRetriever
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
            context.assets.open("${sanitized(fileName)}.opus").close()
            true
        } catch (_: Exception) {
            false
        }
    }

    /**
     * How long a clip runs, in milliseconds, WITHOUT playing it.
     *
     * A screen that walks a highlight along a spoken sentence has to know the real length
     * of the recording. Read Your First Sentences stepped a fixed 380ms per word, so a
     * short line ran the highlight off the end and a long one left it stranded on the last
     * word — the longer the line, the worse the drift.
     *
     * Cached: a replay asks for the same file every time, and a retriever open per replay
     * is not free.
     */
    private val durationCache = mutableMapOf<String, Long>()

    fun durationMs(fileName: String): Long? {
        val key = sanitized(fileName)
        durationCache[key]?.let { return it }
        return try {
            context.assets.openFd("$key.opus").use { afd ->
                MediaMetadataRetriever().use { mmr ->
                    mmr.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                    val ms = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull()
                    ms?.also { durationCache[key] = it }
                }
            }
        } catch (_: Exception) {
            null
        }
    }

    fun playPhonicsSound(fileName: String) {
        try {
            stop()
            val afd = context.assets.openFd("${sanitized(fileName)}.opus")
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
