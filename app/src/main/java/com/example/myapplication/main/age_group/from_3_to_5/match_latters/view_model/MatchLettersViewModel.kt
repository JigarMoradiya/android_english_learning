package com.example.myapplication.main.age_group.from_3_to_5.match_latters.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.utilities.TextToSpeechManager
import com.example.myapplication.utils.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MatchLettersViewModel @Inject constructor(
    private val ttsManager: TextToSpeechManager
) : ViewModel() {

    private val allLetters = ('A'..'Z').toList()
    private val batchSize = 5

    private var didSpeakThisTurn = false

    var uiState by mutableStateOf(MatchLettersUiState())
        private set

    init {
        loadNewBatch()
    }

    // LOAD
    fun loadNewBatch() {
        val batch = allLetters.shuffled().take(batchSize)

        uiState = uiState.copy(
            currentBatch = batch,
            shuffledLowercase = batch.shuffled(),
            selectedUpper = null,
            selectedLower = null,
            matchedPairs = emptySet()
        )

        didSpeakThisTurn = false
    }

    // SELECT
    fun selectUpper(letter: Char) {
        uiState = uiState.copy(selectedUpper = letter)
        speakOnce(letter)
        checkMatch()
    }

    fun selectLower(letter: Char) {
        uiState = uiState.copy(selectedLower = letter)
        speakOnce(letter)
        checkMatch()
    }

    private fun speakOnce(letter: Char) {
        if (didSpeakThisTurn) return
        ttsManager.speak("${letter}.")
        didSpeakThisTurn = true
    }

    // MATCH LOGIC
    private fun checkMatch() {
        val u = uiState.selectedUpper
        val l = uiState.selectedLower

        if (u != null && l != null) {

            if (u.lowercaseChar() == l.lowercaseChar()) {

                val newMatched = uiState.matchedPairs + u

                uiState = if (newMatched.size == uiState.currentBatch.size) {
                    // COMPLETE
                    AudioPlayerManager.playSoundClap()
                    uiState.copy(
                        matchedPairs = newMatched,
                        showPopup = true,
                        feedbackText = "Great Job!",
                        feedbackSubText = "You matched all letters!"
                    )
                } else {
                    AudioPlayerManager.playSoundCorrectAnswer()
                    uiState.copy(matchedPairs = newMatched)

                }

            } else {
                AudioPlayerManager.playSoundWrongAnswer()
                // WRONG (you can play sound here)
            }

            // RESET
            didSpeakThisTurn = false
            uiState = uiState.copy(
                selectedUpper = null,
                selectedLower = null
            )
        }
    }

    // NEXT ROUND
    fun playAgain() {
        uiState = uiState.copy(
            round = uiState.round + 1,
            showPopup = false
        )
        loadNewBatch()
    }

    fun closePopup() {
        uiState = uiState.copy(
            showPopup = false
        )
    }

}