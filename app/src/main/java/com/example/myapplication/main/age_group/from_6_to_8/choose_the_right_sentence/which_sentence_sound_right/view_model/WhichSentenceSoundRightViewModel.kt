package com.example.myapplication.main.age_group.from_6_to_8.choose_the_right_sentence.which_sentence_sound_right.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.generation.loader.SentenceBuilderLogic
import com.example.myapplication.data.generation.loader.SoundCorrectLoader
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceUnit
import com.example.myapplication.data.model.displayTitle
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WhichSentenceSoundRightViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private var startTimeMs = System.currentTimeMillis()

    private val _uiState = MutableStateFlow(WhichSentenceSoundRightUiState())
    val uiState: StateFlow<WhichSentenceSoundRightUiState> = _uiState

    // Timed mode (item 4.3) — 30s per batch; timeout ends the round.
    private val batchDurationMs = 30_000L
    private var timerJob: Job? = null
    private var hasRecorded = false

    fun setData(unit: SentenceUnit, level: SentenceLevel) {
        _uiState.update {
            it.copy(unit = unit, level = level)
        }
        loadQuestions()
    }

    // Load + filter + shuffle + take 5
    private fun loadQuestions() {
        val state = _uiState.value

        val allQuestions = SoundCorrectLoader.load(
            context = context,
            unit = state.unit,
            level = state.level
        )
        val filtered = allQuestions
            .shuffled()
            .take(5)

        _uiState.update {
            it.copy(
                questions = filtered,
                currentIndex = 0,
                selectedAnswer = null,
                score = 0,
                showResult = false
            )
        }

        generateOptions()

        hasRecorded = false
        startTimerIfNeeded()
    }

    // MARK: - Timed mode (item 4.3)
    fun toggleTimedMode() {
        val newVal = !_uiState.value.timedMode
        _uiState.update { it.copy(timedMode = newVal) }
        if (newVal) {
            startTimerIfNeeded()
        } else {
            stopTimer()
            _uiState.update { it.copy(timerProgress = 1f) }
        }
    }

    private fun startTimerIfNeeded() {
        stopTimer()
        if (!_uiState.value.timedMode) return
        startTimeMs = System.currentTimeMillis()
        _uiState.update { it.copy(timerProgress = 1f) }
        timerJob = viewModelScope.launch {
            var remainingMs = batchDurationMs
            while (remainingMs > 0) {
                delay(100)
                remainingMs -= 100
                _uiState.update {
                    it.copy(timerProgress = (remainingMs.toFloat() / batchDurationMs).coerceAtLeast(0f))
                }
            }
            recordSessionIfNeeded()
            _uiState.update { it.copy(showResult = true) }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun recordSessionIfNeeded() {
        if (hasRecorded) return
        hasRecorded = true
        val state = _uiState.value
        val durationSec = ((System.currentTimeMillis() - startTimeMs) / 1000).toInt()
        sessionRepository.record(
            LearningSession(
                moduleId = ModuleID.WHICH_SENTENCE_RIGHT,
                ageGroup = AgeGroup.SIX_TO_EIGHT,
                durationSeconds = durationSec,
                score = state.score,
                totalQuestions = state.questions.size,
                correctItems = emptyList(),
                wrongItems = emptyList(),
                subConfig = if (state.timedMode) "TIMED" else "",
                lessonTitle = null,
                chapterTitle = state.unit.displayTitle
            )
        )
    }

    // Generate options per question — level-scaled count (easy 3 / medium 4). (item C.2)
    private fun generateOptions() {
        val state = _uiState.value
        val question = state.currentQuestion ?: return

        _uiState.update {
            it.copy(
                options = SentenceBuilderLogic.buildSoundOptions(
                    correct = question.correctSentence,
                    wrongOptions = question.wrongOptions,
                    level = state.level
                )
            )
        }
    }

    // Select answer

    fun selectAnswer(answer: String) {
        val state = _uiState.value

        val isCorrect = answer == state.correctAnswer

        if (isCorrect) {
            AudioPlayerManager.playSoundCorrectAnswer()
        } else {
            AudioPlayerManager.playSoundWrongAnswer()
        }

        _uiState.update {
            it.copy(
                selectedAnswer = answer,
                score = if (isCorrect) it.score + 1 else it.score
            )
        }
        if (_uiState.value.currentIndex == _uiState.value.questions.size - 1) {
            stopTimer()
            recordSessionIfNeeded()
        }
    }

    // Next question
    fun next() {
        val state = _uiState.value
        if (state.currentIndex < state.questions.size - 1) {

            _uiState.update {
                it.copy(
                    currentIndex = it.currentIndex + 1,
                    selectedAnswer = null,
                    showResult = false
                )
            }

            generateOptions()

        } else {
            _uiState.update { it.copy(showResult = true) }
        }
    }

    // Restart
    fun restart() {
        startTimeMs = System.currentTimeMillis()
        _uiState.update {
            it.copy(
                currentIndex = 0,
                score = 0,
                selectedAnswer = null,
                showResult = false
            )
        }

        loadQuestions()
    }

    // UI helper (same as iOS)
    fun backgroundType(option: String): ButtonType {
        val state = _uiState.value
        val selected = state.selectedAnswer ?: return ButtonType.OPTIONS

        // Correct answer
        if (option == state.correctAnswer) {
            return ButtonType.GREEN
        }

        // Selected wrong answer
        if (option == selected) {
            return ButtonType.RED
        }

        return ButtonType.OPTIONS
    }
}