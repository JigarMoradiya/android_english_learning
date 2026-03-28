package com.example.myapplication.utils

import android.content.Context
import android.media.MediaPlayer
import android.media.SoundPool
import java.io.IOException

object AudioPlayerManager {

    private var soundPool: SoundPool? = null
    private val soundMap = mutableMapOf<String, Int>()

    fun init(context: Context) {
        if (soundPool != null) return

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .build()

        soundMap["correct_ans"] = load(context, "correct_ans.mp3")
        soundMap["wrong_ans"] = load(context, "wrong_ans.mp3")
        soundMap["drag"] = load(context, "drag.mp3")
        soundMap["menu_click"] = load(context, "menu_click.mp3")
        soundMap["back_click"] = load(context, "back_click.mp3")
        soundMap["game_complete"] = load(context, "number_puzzle_complete.wav")
        soundMap["clap"] = load(context, "clap.wav")
    }

    private fun load(context: Context, fileName: String): Int {
        val afd = context.assets.openFd(fileName)
        return soundPool!!.load(afd, 1)
    }

    fun playSoundCorrectAnswer() {
        play("correct_ans")
    }

    fun playSoundWrongAnswer() {
        play("wrong_ans")
    }

    fun playSoundDragItem() {
        play("drag")
    }

    fun playSoundMenuClick() {
        play("menu_click")
    }

    fun playSoundBtnBack() {
        play("back_click")
    }

    fun playSoundGameComplete() {
        play("game_complete")
    }

    fun playSoundClap() {
        play("clap")
    }

    private fun play(key: String) {
        val soundId = soundMap[key] ?: return
        soundPool?.play(soundId, 1f, 1f, 1, 0, 1f)
    }

    fun stop() {
        soundPool?.autoPause()
    }
}