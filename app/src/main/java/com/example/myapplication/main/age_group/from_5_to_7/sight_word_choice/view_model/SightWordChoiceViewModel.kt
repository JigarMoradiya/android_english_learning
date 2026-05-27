package com.example.myapplication.main.age_group.from_5_to_7.sight_word_choice.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.main.age_group.from_5_to_7.sight_word_choice.data.sightWordsAgeGroup_5_7Example
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackGiveAnswerSubTitleCorrect
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SightWordChoiceViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    var uiState by mutableStateOf(SightWordChoiceUiState())
        private set

    private val allWords: List<com.example.myapplication.main.age_group.from_5_to_7.sight_word_choice.data.SightWordMultipleExample> by lazy {
        sightWordsAgeGroup_5_7Example.shuffled()
    }

    private var batchWords: List<com.example.myapplication.main.age_group.from_5_to_7.sight_word_choice.data.SightWordMultipleExample> = emptyList()
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

        val currentWord = batchWords[batchIndex]
        val example = currentWord.examples.random()
        val (prefix, suffix) = splitSentence(example, currentWord.word)

        val wrongs = allWords
            .filter { !it.word.equals(currentWord.word, true) }
            .shuffled()
            .take(2)
            .map { it.word }

        val options = (listOf(currentWord.word) + wrongs).shuffled()

        uiState = uiState.copy(
            currentWord = currentWord,
            currentExample = example,
            sentencePrefix = prefix,
            sentenceSuffix = suffix,
            options = options,
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
        val isCorrect = choice.equals(uiState.currentWord.word, true)

        if (isCorrect) {
            if (!wrongAttemptsInBatch.contains(uiState.currentWord.word)) {
                correctAttemptsInBatch.add(uiState.currentWord.word)
            }
        } else {
            wrongAttemptsInBatch.add(uiState.currentWord.word)
        }

        uiState = uiState.copy(
            selectedAnswer = choice,
            isAnswerCorrect = isCorrect,
            countdown = 3,
            feedbackTextCorrect = if (isCorrect) feedbackTitles.random() else null,
            feedbackTextWrong = if (isCorrect) null else {
                if (uiState.sentencePrefix.isEmpty()) {
                    "Wrong! Correct answer : ${uiState.currentWord.word.replaceFirstChar { it.uppercase() }}"
                } else {
                    "Wrong! Correct answer : ${uiState.currentWord.word.lowercase()}"
                }
            }
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
        AudioPlayerManager.playSoundClap()
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
                moduleId = ModuleID.SIGHT_WORD_CHOICE,
                ageGroup = AgeGroup.FIVE_TO_SEVEN,
                durationSeconds = duration,
                score = score,
                totalQuestions = uiState.totalQuestions,
                wrongItems = wrongAttemptsInBatch.sorted(),
                correctItems = correctAttemptsInBatch.sorted()
            )
        )
    }

    private fun splitSentence(text: String, word: String): Pair<String, String> {
        val regex = "\\b${Regex.escape(word)}\\b".toRegex(RegexOption.IGNORE_CASE)
        val match = regex.find(text)

        return if (match != null) {
            val prefix = text.substring(0, match.range.first).trim()
            val suffix = text.substring(match.range.last + 1).trim()
            prefix to suffix
        } else {
            text to ""
        }
    }
}
