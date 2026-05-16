package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.beginner.multiple_choice.view_model

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.data.generation.loader.BeginnerActivityType
import com.example.myapplication.data.generation.loader.MixedGrammarBeginnerQuestionFactory
import com.example.myapplication.data.model.WordType
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackGiveAnswerSubTitleCorrect
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import com.example.myapplication.utils.FeedbackConstant.feedbackWrong
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GrammarMultipleChoiceViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(GrammarMultipleChoiceUiState())
    val uiState: StateFlow<GrammarMultipleChoiceUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        val all = MixedGrammarBeginnerQuestionFactory.generateQuestions(context, BeginnerActivityType.MULTIPLE_CHOICE)
        val questions = all.shuffled().take(10)
        _uiState.value = GrammarMultipleChoiceUiState(
            questionsAll = all,
            questions = questions
        )
    }

    fun restart() {
        val all = _uiState.value.questionsAll
        _uiState.value = GrammarMultipleChoiceUiState(
            questionsAll = all,
            questions = all.shuffled().take(10)
        )
    }

    fun selectAnswer(option: String) {
        val state = _uiState.value
        if (state.selectedWord != null) return
        val q = state.currentQuestion ?: return

        val selected = option.lowercase()
        // Accept any word of the correct type (mirrors iOS allCorrectWords check)
        val isCorrect = q.allCorrectWords.contains(selected)

        if (isCorrect) AudioPlayerManager.playSoundCorrectAnswer()
        else AudioPlayerManager.playSoundWrongAnswer()

        _uiState.update {
            it.copy(
                selectedWord = selected,
                isAnswerCorrect = isCorrect,
                feedbackTitleRes = if (isCorrect) feedbackTitles.random() else feedbackWrong.random(),
                feedbackSubTitle = if (isCorrect) feedbackGiveAnswerSubTitleCorrect.random() else null,
                showNext = true,
                score = if (isCorrect) it.score + 1 else it.score
            )
        }
    }

    fun moveToNextQuestion() {
        val state = _uiState.value
        if (state.currentIndex < state.questions.size - 1) {
            _uiState.update {
                it.copy(
                    currentIndex = it.currentIndex + 1,
                    selectedWord = null,
                    isAnswerCorrect = false,
                    feedbackTitleRes = null,
                    feedbackSubTitle = null,
                    showNext = false
                )
            }
        } else {
            _uiState.update { it.copy(isCompleted = true) }
        }
    }

    // ── Button type for option chips ─────────────────────────────────────────

    fun optionType(option: String): ButtonType {
        val state = _uiState.value
        val selected = state.selectedWord ?: return ButtonType.OPTIONS
        val q = state.currentQuestion ?: return ButtonType.OPTIONS
        return when {
            q.allCorrectWords.contains(option.lowercase()) -> ButtonType.GREEN
            option.lowercase() == selected                 -> ButtonType.RED
            else                                           -> ButtonType.OPTIONS
        }
    }

    // ── Type label / color / icon ────────────────────────────────────────────

    fun typeLabel(type: WordType): String = when (type) {
        WordType.NOUN      -> "Noun"
        WordType.VERB      -> "Verb"
        WordType.ADJECTIVE -> "Adjective"
        WordType.PRONOUN   -> "Pronoun"
    }

    fun typeColor(type: WordType): Color = when (type) {
        WordType.NOUN      -> Color(0xFF4CAF50)  // green
        WordType.VERB      -> Color(0xFF9C27B0)  // purple
        WordType.ADJECTIVE -> Color(0xFFFF9800)  // orange
        WordType.PRONOUN   -> Color(0xFFF44336)  // red
    }

    fun typeIcon(type: WordType): ImageVector = when (type) {
        WordType.NOUN      -> Icons.Filled.Image
        WordType.VERB      -> Icons.Filled.Bolt
        WordType.ADJECTIVE -> Icons.Filled.Palette
        WordType.PRONOUN   -> Icons.Filled.Person
    }
}
