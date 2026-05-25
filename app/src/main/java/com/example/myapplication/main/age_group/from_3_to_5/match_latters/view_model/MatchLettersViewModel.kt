package com.example.myapplication.main.age_group.from_3_to_5.match_latters.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.ModuleProgressRepository
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.data.progress.models.MatchUpperLowerProgress
import com.example.myapplication.utilities.TextToSpeechManager
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackMatchLetterSubtitles
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchLettersViewModel @Inject constructor(
    private val ttsManager: TextToSpeechManager,
    private val sessionRepository: SessionRepository,
    private val moduleProgressRepository: ModuleProgressRepository
) : ViewModel() {

    private val allLetters = ('A'..'Z').toList()
    private val batchSize = 5

    private var didSpeakThisTurn = false
    private var wrongAttemptsInBatch = mutableSetOf<Char>()
    private var batchStartMs = System.currentTimeMillis()

    var uiState by mutableStateOf(MatchLettersUiState())
        private set

    init {
        loadNewBatch()
    }

    // MARK: - Load

    fun loadNewBatch() {
        val batch = allLetters.shuffled().take(batchSize)
        wrongAttemptsInBatch = mutableSetOf()
        batchStartMs = System.currentTimeMillis()
        didSpeakThisTurn = false

        uiState = uiState.copy(
            currentBatch = batch,
            shuffledLowercase = batch.shuffled(),
            selectedUpper = null,
            selectedLower = null,
            matchedPairs = emptySet()
        )
    }

    // MARK: - Select

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
        ttsManager.speak("$letter.")
        didSpeakThisTurn = true
    }

    // MARK: - Match Logic

    private fun checkMatch() {
        val u = uiState.selectedUpper
        val l = uiState.selectedLower
        if (u == null || l == null) return

        if (u.lowercaseChar() == l.lowercaseChar()) {
            val newMatched = uiState.matchedPairs + u

            if (newMatched.size == uiState.currentBatch.size) {
                viewModelScope.launch {
                    delay(300)
                    val score = batchSize - wrongAttemptsInBatch.size
                    val stars = computeStars(score, batchSize)
                    recordSession(score)
                    AudioPlayerManager.playSoundClap()
                    uiState = uiState.copy(
                        matchedPairs = newMatched,
                        selectedUpper = null,
                        selectedLower = null,
                        batchScore = score,
                        earnedStars = stars,
                        scoreLabel = if (score == batchSize) "perfect! 🎯" else "first try 🎯",
                        feedbackTextRes = feedbackTitles.random(),
                        feedbackSubTextRes = feedbackMatchLetterSubtitles.random(),
                        showPopup = true
                    )
                }
                didSpeakThisTurn = false
                return
            } else {
                AudioPlayerManager.playSoundCorrectAnswer()
                uiState = uiState.copy(matchedPairs = newMatched)
            }
        } else {
            wrongAttemptsInBatch.add(u)
            AudioPlayerManager.playSoundWrongAnswer()
        }

        didSpeakThisTurn = false
        uiState = uiState.copy(selectedUpper = null, selectedLower = null)
    }

    // MARK: - Next Round

    fun playAgain() {
        uiState = uiState.copy(round = uiState.round + 1, showPopup = false)
        loadNewBatch()
    }

    fun closePopup() {
        uiState = uiState.copy(showPopup = false)
    }

    // MARK: - Session Recording

    private fun recordSession(score: Int) {
        val duration = ((System.currentTimeMillis() - batchStartMs) / 1000).toInt()
        val correctItems = uiState.currentBatch
            .filter { it !in wrongAttemptsInBatch }
            .map { it.toString() }
        sessionRepository.record(
            LearningSession(
                moduleId = ModuleID.MATCH_UPPER_LOWER,
                ageGroup = AgeGroup.THREE_TO_FIVE,
                durationSeconds = duration,
                score = score,
                totalQuestions = batchSize,
                wrongItems = wrongAttemptsInBatch.map { it.toString() },
                correctItems = correctItems
            )
        )

        val progress = moduleProgressRepository.load(ModuleID.MATCH_UPPER_LOWER, MatchUpperLowerProgress::class.java)
            ?: MatchUpperLowerProgress()

        val alphabet = ('A'..'Z').toList()
        val newWeakIndices = wrongAttemptsInBatch
            .mapNotNull { ch -> alphabet.indexOf(ch.uppercaseChar()).takeIf { it >= 0 } }
            .filter { it !in progress.weakPairIndices }

        moduleProgressRepository.save(
            progress.copy(
                totalRoundsCompleted = progress.totalRoundsCompleted + 1,
                weakPairIndices = progress.weakPairIndices + newWeakIndices
            ),
            ModuleID.MATCH_UPPER_LOWER
        )
    }

    // MARK: - Helpers

    private fun computeStars(score: Int, total: Int): Int {
        val ratio = score.toDouble() / total
        return when {
            ratio >= 1.0 -> 3
            ratio >= 0.6 -> 2
            else -> 1
        }
    }
}
