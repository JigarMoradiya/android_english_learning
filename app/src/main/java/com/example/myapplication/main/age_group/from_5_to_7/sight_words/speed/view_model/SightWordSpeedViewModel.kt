package com.example.myapplication.main.age_group.from_5_to_7.sight_words.speed.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.main.age_group.from_5_to_7.sight_words.data.sightWordsAgeGroup5_7
import com.example.myapplication.utils.SpeedSchedule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SightWordSpeedUiState(
    val words: List<String> = emptyList(),
    val index: Int = 0,
    val progress: Float = 1f,
    val isPaused: Boolean = false,
    val showComplete: Boolean = false,
) {
    val currentWord: String get() = words.getOrElse(index) { "" }
}

private const val WORDS_PER_GAME = 10

private fun pickWords(): List<String> =
    sightWordsAgeGroup5_7.map { it.word }.shuffled().take(WORDS_PER_GAME)

@HiltViewModel
class SightWordSpeedViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    var uiState by mutableStateOf(SightWordSpeedUiState(words = pickWords()))
        private set

    private var tickerJob: Job? = null
    private var remainingMs: Long = 0
    private var startMs = System.currentTimeMillis()
    private val tickMs = 50L

    val currentDurationLabel: String
        get() = "%.2fs".format(SpeedSchedule.durationMillis(uiState.index) / 1000f)

    init {
        start()
    }

    fun start() {
        startMs = System.currentTimeMillis()
        startWord()
    }

    fun togglePause() {
        uiState = uiState.copy(isPaused = !uiState.isPaused)
    }

    fun restart() {
        uiState = SightWordSpeedUiState(words = pickWords())
        start()
    }

    private fun startWord() {
        tickerJob?.cancel()
        remainingMs = SpeedSchedule.durationMillis(uiState.index)
        uiState = uiState.copy(progress = 1f)
        tickerJob = viewModelScope.launch {
            while (true) {
                delay(tickMs)
                if (uiState.isPaused || uiState.showComplete) continue
                remainingMs -= tickMs
                val total = SpeedSchedule.durationMillis(uiState.index).toFloat()
                uiState = uiState.copy(progress = (remainingMs / total).coerceAtLeast(0f))
                if (remainingMs <= 0) {
                    advance()
                    break
                }
            }
        }
    }

    private fun advance() {
        if (uiState.index >= uiState.words.size - 1) {
            tickerJob?.cancel()
            uiState = uiState.copy(showComplete = true)
            recordSession()
        } else {
            uiState = uiState.copy(index = uiState.index + 1)
            startWord()
        }
    }

    private fun recordSession() {
        sessionRepository.record(
            LearningSession(
                moduleId = ModuleID.SIGHT_WORDS,
                ageGroup = AgeGroup.FIVE_TO_SEVEN,
                durationSeconds = ((System.currentTimeMillis() - startMs) / 1000).toInt(),
                score = uiState.words.size,
                totalQuestions = uiState.words.size,
                wrongItems = emptyList(),
                correctItems = uiState.words.sorted(),
                subConfig = "SPEED_MODE"
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        tickerJob?.cancel()
    }
}
