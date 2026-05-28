package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.advanced.fix_sentence.view_model

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.myapplication.R
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.generation.loader.FixSentenceFactory
import com.example.myapplication.data.model.WordType
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackGiveAnswerSubTitleCorrect
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FixTheSentenceViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private var startTimeMs = System.currentTimeMillis()

    private val _uiState = MutableStateFlow(FixTheSentenceUiState())
    val uiState: StateFlow<FixTheSentenceUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val questions = withContext(Dispatchers.Default) {
                FixSentenceFactory.generateQuestions(context).shuffled().take(10)
            }
            _uiState.value = FixTheSentenceUiState(isLoading = false, questions = questions)
        }
    }

    fun selectOption(option: String) {
        if (_uiState.value.isAnswerSubmitted) return
        _uiState.update { it.copy(selectedOption = option) }
        submitAnswer()
    }

    private fun submitAnswer() {
        val state   = _uiState.value
        val q       = state.currentQuestion ?: return
        val selected = state.selectedOption ?: return
        val correct = selected.equals(q.correctWord, ignoreCase = true)

        if (correct) AudioPlayerManager.playSoundCorrectAnswer()
        else         AudioPlayerManager.playSoundWrongAnswer()

        _uiState.update {
            it.copy(
                isAnswerSubmitted = true,
                isAnswerCorrect   = correct,
                feedbackTitleRes  = if (correct) feedbackTitles.random() else R.string.its_wrong,
                feedbackSubTitleRes  = if (correct) feedbackGiveAnswerSubTitleCorrect.random() else null,
                score             = if (correct) it.score + 1 else it.score
            )
        }
        if (_uiState.value.isLastIndex) {
            val state = _uiState.value
            val durationSec = ((System.currentTimeMillis() - startTimeMs) / 1000).toInt()
            sessionRepository.record(
                LearningSession(
                    moduleId = ModuleID.GRAMMAR_CHALLENGE_ADVANCED,
                    ageGroup = AgeGroup.SIX_TO_EIGHT,
                    durationSeconds = durationSec,
                    score = state.score,
                    totalQuestions = state.questions.size,
                    subConfig = "",
                    lessonTitle = null,
                    chapterTitle = "Fix the Sentence"
                )
            )
        }
    }

    fun moveToNext() {
        val state = _uiState.value
        if (state.isLastIndex) {
            _uiState.update { it.copy(isCompleted = true) }
        } else {
            _uiState.update {
                it.copy(
                    currentIndex      = it.currentIndex + 1,
                    selectedOption    = null,
                    isAnswerSubmitted  = false,
                    isAnswerCorrect   = false,
                    feedbackTitleRes  = null
                )
            }
        }
    }

    fun restart() {
        startTimeMs = System.currentTimeMillis()
        load()
    }

    fun optionState(option: String): FixOptionState {
        val state = _uiState.value
        val q     = state.currentQuestion ?: return FixOptionState.NORMAL
        if (!state.isAnswerSubmitted) {
            return if (option == state.selectedOption) FixOptionState.SELECTED else FixOptionState.NORMAL
        }
        return when {
            option.equals(q.correctWord, ignoreCase = true) -> FixOptionState.CORRECT
            option == state.selectedOption                  -> FixOptionState.WRONG
            else                                            -> FixOptionState.NORMAL
        }
    }

    fun typeColor(type: WordType): Color = when (type) {
        WordType.NOUN      -> Color(0xFF00838F)
        WordType.VERB      -> Color(0xFF6A1B9A)
        WordType.ADJECTIVE -> Color(0xFF1565C0)
        WordType.PRONOUN   -> Color(0xFFAD1457)
    }

    fun typeLabel(type: WordType): String = when (type) {
        WordType.NOUN      -> "Noun"
        WordType.VERB      -> "Verb"
        WordType.ADJECTIVE -> "Adjective"
        WordType.PRONOUN   -> "Pronoun"
    }

    fun typeIcon(type: WordType): ImageVector = when (type) {
        WordType.NOUN      -> Icons.Filled.Image
        WordType.VERB      -> Icons.Filled.DirectionsRun
        WordType.ADJECTIVE -> Icons.Filled.Palette
        WordType.PRONOUN   -> Icons.Filled.Person
    }
}
