package com.example.myapplication.main.age_group.from_5_to_7.article_choice.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.main.age_group.from_5_to_7.article_choice.ArticleRule
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackGiveAnswerSubTitleCorrect
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleChoiceViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    var uiState by mutableStateOf(ArticleChoiceUiState())
        private set

    private val allWords: List<String> by lazy {
        LetterRepository.all.flatMap { listOf(it.mainWord) + it.altWords }.distinct()
    }

    private var batchWords: List<String> = emptyList()
    private var batchIndex: Int = 0
    private var wrongAttemptsInBatch = mutableSetOf<String>()
    private var correctAttemptsInBatch = mutableSetOf<String>()
    private var batchStartMs = System.currentTimeMillis()
    private var countdownJob: Job? = null

    init {
        loadNewBatch()
    }

    fun loadNewBatch() {
        countdownJob?.cancel()
        batchWords = allWords.shuffled().take(uiState.totalQuestions)
        // ⚠️ TEMP TEST ONLY — force U-words to verify the a/an sound-rule fix. REMOVE AFTER TESTING.
        batchWords = listOf("Unicorn", "Uniform", "Utensil", "UFO", "Umbrella")
        batchIndex = 0
        wrongAttemptsInBatch.clear()
        correctAttemptsInBatch.clear()
        batchStartMs = System.currentTimeMillis()
        uiState = uiState.copy(showBatchPopup = false)
        loadWord()
    }

    private fun loadWord() {
        countdownJob?.cancel()
        if (batchWords.isEmpty()) return
        val word = batchWords[batchIndex]
        uiState = uiState.copy(
            currentWord = word,
            currentImageName = word.lowercase(),
            selectedAnswer = null,
            isAnswerCorrect = false,
            feedbackTextCorrect = null,
            feedbackTextWrong = null,
            countdown = 3,
            questionIndex = batchIndex
        )
    }

    fun checkAnswer(choice: String) {
        if (uiState.selectedAnswer != null) return
        val correct = articleFor()
        val isCorrect = choice == correct

        if (isCorrect) {
            if (!wrongAttemptsInBatch.contains(uiState.currentWord)) {
                correctAttemptsInBatch.add(uiState.currentWord)
            }
        } else {
            wrongAttemptsInBatch.add(uiState.currentWord)
        }

        uiState = uiState.copy(
            selectedAnswer = choice,
            isAnswerCorrect = isCorrect,
            countdown = 3,
            feedbackTextCorrect = if (isCorrect) feedbackTitles.random() else null,
            feedbackTextWrong = if (isCorrect) null else "Wrong! Correct answer : $correct"
        )

        if (isCorrect) {
            AudioPlayerManager.playSoundCorrectAnswer()
        } else {
            AudioPlayerManager.playSoundWrongAnswer()
        }

        batchIndex++

        if (batchIndex >= uiState.totalQuestions) {
            viewModelScope.launch {
                delay(500)
                showBatchComplete()
            }
        } else {
            startCountdown()
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            for (i in 2 downTo 1) {
                delay(1000)
                uiState = uiState.copy(countdown = i)
            }
            delay(1000)
            loadWord()
        }
    }

    private fun showBatchComplete() {
        countdownJob?.cancel()
        val score = correctAttemptsInBatch.size
        recordSession(score)
        uiState = uiState.copy(
            lastScore = score,
            scoreLabel = if (score == uiState.totalQuestions) "perfect! 🎯" else "first try 🎯",
            feedbackBatchTextRes = feedbackTitles.random(),
            feedbackBatchSubTextRes = feedbackGiveAnswerSubTitleCorrect.random(),
            showBatchPopup = true
        )
    }

    private fun recordSession(score: Int) {
        val duration = ((System.currentTimeMillis() - batchStartMs) / 1000).toInt()
        sessionRepository.record(
            LearningSession(
                moduleId = ModuleID.ARTICLES_CHOICE,
                ageGroup = AgeGroup.FIVE_TO_SEVEN,
                durationSeconds = duration,
                score = score,
                totalQuestions = uiState.totalQuestions,
                wrongItems = wrongAttemptsInBatch.sorted(),
                correctItems = correctAttemptsInBatch.sorted()
            )
        )
    }

    fun articleFor(): String {
        return if (ArticleRule.needsAn(uiState.currentWord)) "an" else "a"
    }
}
