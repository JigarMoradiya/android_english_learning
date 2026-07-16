package com.example.myapplication.main.age_group.from_6_to_8.grammar_drill.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.generation.loader.AgreementBank
import com.example.myapplication.data.generation.loader.GrammarDrillLogic
import com.example.myapplication.data.generation.loader.GrammarDrillType
import com.example.myapplication.data.generation.loader.HasHaveBank
import com.example.myapplication.data.generation.loader.LessonLoader
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GrammarDrillViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private var startTimeMs = System.currentTimeMillis()

    private val _uiState = MutableStateFlow(GrammarDrillUiState())
    val uiState: StateFlow<GrammarDrillUiState> = _uiState

    fun setType(type: GrammarDrillType) {
        _uiState.update { it.copy(type = type) }
        load()
    }

    private fun load() {
        // Both drills use curated banks (clean, kid-friendly sentences) — not the pool.
        val qs = when (_uiState.value.type) {
            GrammarDrillType.HAS_HAVE -> GrammarDrillLogic.makeHasHaveQuestions(emptyList(), HasHaveBank.sentences, 10)
            GrammarDrillType.SUBJECT_VERB_AGREEMENT -> GrammarDrillLogic.makeAgreementFromBank(AgreementBank.items, 10)
        }
        startTimeMs = System.currentTimeMillis()
        _uiState.update {
            it.copy(questions = qs, currentIndex = 0, selectedAnswer = null, score = 0, showResult = false)
        }
    }

    fun selectAnswer(answer: String) {
        val state = _uiState.value
        if (state.selectedAnswer != null) return
        val isCorrect = answer == state.correctAnswer
        if (isCorrect) AudioPlayerManager.playSoundCorrectAnswer() else AudioPlayerManager.playSoundWrongAnswer()
        _uiState.update {
            it.copy(selectedAnswer = answer, score = if (isCorrect) it.score + 1 else it.score)
        }
        if (_uiState.value.currentIndex == _uiState.value.questions.size - 1) recordSession()
    }

    fun backgroundType(option: String): ButtonType {
        val state = _uiState.value
        val selected = state.selectedAnswer ?: return ButtonType.OPTIONS
        return when {
            option == state.correctAnswer -> ButtonType.GREEN
            option == selected -> ButtonType.RED
            else -> ButtonType.OPTIONS
        }
    }

    fun next() {
        val state = _uiState.value
        if (state.currentIndex < state.questions.size - 1) {
            _uiState.update { it.copy(currentIndex = it.currentIndex + 1, selectedAnswer = null) }
        } else {
            _uiState.update { it.copy(showResult = true) }
        }
    }

    fun restart() {
        load()
    }

    private fun recordSession() {
        val state = _uiState.value
        val moduleId = if (state.type == GrammarDrillType.HAS_HAVE)
            ModuleID.GRAMMAR_DRILL_HAS_HAVE else ModuleID.GRAMMAR_DRILL_AGREEMENT
        sessionRepository.record(
            LearningSession(
                moduleId = moduleId,
                ageGroup = AgeGroup.SIX_TO_EIGHT,
                durationSeconds = ((System.currentTimeMillis() - startTimeMs) / 1000).toInt(),
                score = state.score,
                totalQuestions = state.questions.size,
                correctItems = emptyList(),
                wrongItems = emptyList(),
                subConfig = "",
                lessonTitle = null,
                chapterTitle = null   // "Practice" convention so the report detail popup matches
            )
        )
    }
}
