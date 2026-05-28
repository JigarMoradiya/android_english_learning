package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.medium.drag_to_bucket.view_model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.generation.loader.MixedGrammarMediumQuestions
import com.example.myapplication.data.generation.loader.WordDragItem
import com.example.myapplication.data.generation.loader.WrongCorrection
import com.example.myapplication.data.model.WordType
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackGiveAnswerSubTitleCorrect
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DragToGrammarBucketViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private var startTimeMs = System.currentTimeMillis()

    private val _uiState = MutableStateFlow(DragToGrammarBucketUiState())
    val uiState: StateFlow<DragToGrammarBucketUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        val questions = MixedGrammarMediumQuestions.generate().take(10)
        _uiState.update { DragToGrammarBucketUiState(questions = questions) }
        setupCurrentQuestion()
    }

    private fun setupCurrentQuestion() {
        val q = _uiState.value.currentQuestion ?: return
        _uiState.update {
            it.copy(
                wordPool = q.wordPool.shuffled(),
                buckets = q.bucketTypes.associateWith { emptyList() },
                isQuestionComplete = false,
                isAnswerCorrect = false,
                wrongCorrections = emptyList(),
                feedbackTextRes = null,
                feedbackSubTextRes = null,
                itemFrames = emptyMap(),
                bucketFrames = emptyMap(),
                draggingItem = null,
                dragStartCenter = null,
                dragOffset = Offset.Zero
            )
        }
    }

    // ── Frame registration (called from Page's onGloballyPositioned) ─────────

    fun updateItemFrame(id: UUID, rect: Rect) {
        _uiState.update { it.copy(itemFrames = it.itemFrames + (id to rect)) }
    }

    fun updateBucketFrame(type: WordType, rect: Rect) {
        _uiState.update { it.copy(bucketFrames = it.bucketFrames + (type to rect)) }
    }

    // ── Drag ─────────────────────────────────────────────────────────────────

    fun startDrag(item: WordDragItem) {
        val rect = _uiState.value.itemFrames[item.id]
        val center = rect?.let { Offset(it.center.x, it.center.y) }
        _uiState.update {
            it.copy(
                draggingItem = item,
                dragStartCenter = center,
                dragOffset = Offset.Zero
            )
        }
    }

    fun updateDrag(delta: Offset) {
        _uiState.update { it.copy(dragOffset = it.dragOffset + delta) }
    }

    fun endDrag() {
        val state = _uiState.value
        val item = state.draggingItem ?: run { clearDragState(); return }

        val dropCenter = state.dragStartCenter?.let {
            Offset(it.x + state.dragOffset.x, it.y + state.dragOffset.y)
        } ?: run { clearDragState(); return }

        // Find which bucket contains the drop point
        val targetBucket = state.bucketFrames.entries
            .firstOrNull { it.value.contains(dropCenter) }?.key

        if (targetBucket != null) {
            val updatedBuckets = state.buckets.toMutableMap().apply {
                this[targetBucket] = (this[targetBucket] ?: emptyList()) + item
            }
            val updatedPool = state.wordPool.filter { it.id != item.id }

            _uiState.update {
                it.copy(
                    wordPool = updatedPool,
                    buckets = updatedBuckets
                )
            }
            AudioPlayerManager.playSoundDragItem()
            checkComplete()
        }

        clearDragState()
    }

    private fun checkComplete() {
        val state = _uiState.value
        if (state.wordPool.isNotEmpty()) return

        val allCorrect = state.buckets.all { (bucketType, words) ->
            words.all { it.type == bucketType }
        }

        val corrections = if (!allCorrect) {
            state.buckets.flatMap { (bucketType, words) ->
                words.filter { it.type != bucketType }
                    .map { WrongCorrection(word = it.word, correctType = it.type) }
            }
        } else emptyList()

        if (allCorrect) AudioPlayerManager.playSoundCorrectAnswer()
        else AudioPlayerManager.playSoundWrongAnswer()

        _uiState.update {
            it.copy(
                isQuestionComplete = true,
                isAnswerCorrect = allCorrect,
                wrongCorrections = corrections,
                score = if (allCorrect) it.score + 1 else it.score,
                feedbackTextRes = if (allCorrect) feedbackTitles.random() else R.string.some_word_wrong_bucket,
                feedbackSubTextRes = if (allCorrect) feedbackGiveAnswerSubTitleCorrect.random() else null,
            )
        }
    }

    private fun clearDragState() {
        _uiState.update {
            it.copy(draggingItem = null, dragStartCenter = null, dragOffset = Offset.Zero)
        }
    }

    // ── Navigation ───────────────────────────────────────────────────────────

    fun moveToNext() {
        val state = _uiState.value
        if (state.isLastIndex) {
            val durationSec = ((System.currentTimeMillis() - startTimeMs) / 1000).toInt()
            sessionRepository.record(
                LearningSession(
                    moduleId = ModuleID.GRAMMAR_CHALLENGE_MEDIUM,
                    ageGroup = AgeGroup.SIX_TO_EIGHT,
                    durationSeconds = durationSec,
                    score = state.score,
                    totalQuestions = state.questions.size,
                    subConfig = "",
                    lessonTitle = null,
                    chapterTitle = "Sort Words"
                )
            )
            _uiState.update { it.copy(isCompleted = true) }
        } else {
            _uiState.update { it.copy(currentIndex = it.currentIndex + 1) }
            setupCurrentQuestion()
        }
    }

    fun restart() {
        startTimeMs = System.currentTimeMillis()
        load()
    }

    // ── Type helpers ─────────────────────────────────────────────────────────

    fun typeLabel(type: WordType) = when (type) {
        WordType.NOUN      -> "Noun"
        WordType.VERB      -> "Verb"
        WordType.ADJECTIVE -> "Adjective"
        WordType.PRONOUN   -> "Pronoun"
    }

    fun typeColor(type: WordType) = when (type) {
        WordType.NOUN      -> androidx.compose.ui.graphics.Color(0xFF00838F)  // teal
        WordType.VERB      -> androidx.compose.ui.graphics.Color(0xFF6A1B9A)  // purple
        WordType.ADJECTIVE -> androidx.compose.ui.graphics.Color(0xFF1565C0)  // deep blue
        WordType.PRONOUN   -> androidx.compose.ui.graphics.Color(0xFFAD1457)  // dark pink
    }

    fun isCorrectlyPlaced(item: WordDragItem, bucketType: WordType) = item.type == bucketType
}
