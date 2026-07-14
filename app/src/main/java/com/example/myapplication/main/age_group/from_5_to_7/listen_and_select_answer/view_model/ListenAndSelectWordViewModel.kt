package com.example.myapplication.main.age_group.from_5_to_7.listen_and_select_answer.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.data.generation.letter.LetterRepository.vocabularyCategoryAllForListenAndSelect
import com.example.myapplication.data.generation.loader.ListenQuestion
import com.example.myapplication.data.generation.loader.ListenQuestionFactory
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.utilities.TextToSpeechManager
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackGiveAnswerSubTitleCorrect
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import com.example.myapplication.utils.FeedbackConstant.feedbackWrong
import com.example.myapplication.ui.theme.ButtonType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListenAndSelectWordViewModel @Inject constructor(
    private val ttsManager: TextToSpeechManager,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    var uiState by mutableStateOf(ListenAndSelectWordUiState())
        private set

    private val allWords: List<String> by lazy {
        (LetterRepository.all.flatMap { listOf(it.mainWord) + it.altWords }
                + vocabularyCategoryAllForListenAndSelect).distinct()
    }

    private var batchQuestions: List<ListenQuestion> = emptyList()
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
        batchQuestions = ListenQuestionFactory.buildBatch(allWords, uiState.totalQuestions)
        batchIndex = 0
        wrongAttemptsInBatch.clear()
        correctAttemptsInBatch.clear()
        batchStartMs = System.currentTimeMillis()
        uiState = uiState.copy(
            showBatchPopup = false
        )
        loadCurrentWord()
    }

    private fun loadCurrentWord() {
        countdownJob?.cancel()
        val question = batchQuestions[batchIndex]
        uiState = uiState.copy(
            currentWord = question.answer,
            optionsWord = question.options,
            spokenText = question.spokenText,
            isHomophone = question.isHomophone,
            hintSentence = question.hintSentence,
            selectedAnswer = null,
            showSuccess = false,
            showError = false,
            countdown = 3,
            questionIndex = batchIndex
        )
        viewModelScope.launch {
            delay(500)
            speakWord()
        }
    }

    fun optionType(word: String): ButtonType {
        if (uiState.selectedAnswer != word) return ButtonType.OPTIONS
        return if (uiState.showSuccess) ButtonType.GREEN else ButtonType.RED
    }

    fun speakWord() {
        // Homophone questions speak a whole sentence — the meaning picks the word
        ttsManager.speak(uiState.spokenText)
    }

    fun checkCorrectOrWrong(word: String) {
        if (uiState.showSuccess) return
        val isCorrect = word.equals(uiState.currentWord, ignoreCase = true)
        uiState = uiState.copy(selectedAnswer = word)

        if (isCorrect) {
            if (!wrongAttemptsInBatch.contains(uiState.currentWord)) {
                correctAttemptsInBatch.add(uiState.currentWord)
            }
            uiState = uiState.copy(
                feedbackTextRes = feedbackTitles.random(),
                feedbackSubTextRes = feedbackGiveAnswerSubTitleCorrect.random(),
                showSuccess = true,
                showError = false,
                countdown = 3
            )
            AudioPlayerManager.playSoundCorrectAnswer()
            batchIndex++

            if (batchIndex >= uiState.totalQuestions) {
                viewModelScope.launch {
                    delay(500)
                    showBatchComplete()
                }
            } else {
                startCountdown()
            }
        } else {
            wrongAttemptsInBatch.add(uiState.currentWord)
            uiState = uiState.copy(
                feedbackTextRes = feedbackWrong.random(),
                feedbackSubTextError = uiState.hintSentence?.let { "Listen again: \u201C$it\u201D" }
                    ?: "Oops! The word is not $word",
                showError = true,
                showSuccess = false
            )
            AudioPlayerManager.playSoundWrongAnswer()

            // Clear the red highlight after 2s so the child can retry cleanly
            viewModelScope.launch {
                delay(2000)
                if (uiState.selectedAnswer == word && !uiState.showSuccess) {
                    uiState = uiState.copy(selectedAnswer = null)
                }
            }
        }
    }

    private fun showBatchComplete() {
        countdownJob?.cancel()
        val score = maxOf(0, uiState.totalQuestions - wrongAttemptsInBatch.size)
        recordSession(score)
        uiState = uiState.copy(
            lastScore = score,
            scoreLabel = if (score == uiState.totalQuestions) "perfect! 🎯" else "first try 🎯",
            feedbackBatchTextRes = feedbackTitles.random(),
            feedbackBatchSubTextRes = feedbackGiveAnswerSubTitleCorrect.random(),
            showBatchPopup = true
        )
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            for (i in 2 downTo 1) {
                delay(1000)
                uiState = uiState.copy(countdown = i)
            }
            delay(1000)
            loadCurrentWord()
        }
    }

    private fun recordSession(score: Int) {
        val duration = ((System.currentTimeMillis() - batchStartMs) / 1000).toInt()
        sessionRepository.record(
            LearningSession(
                moduleId = ModuleID.LISTEN_AND_SELECT,
                ageGroup = AgeGroup.FIVE_TO_SEVEN,
                durationSeconds = duration,
                score = score,
                totalQuestions = uiState.totalQuestions,
                wrongItems = wrongAttemptsInBatch.sorted(),
                correctItems = correctAttemptsInBatch.sorted()
            )
        )
    }

}
