package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.medium.drag_to_bucket.view_model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.example.myapplication.R
import com.example.myapplication.data.generation.loader.MixedMediumQuestion
import com.example.myapplication.data.generation.loader.WordDragItem
import com.example.myapplication.data.generation.loader.WrongCorrection
import com.example.myapplication.data.model.WordType
import java.util.UUID

data class DragToGrammarBucketUiState(
    // ── Questions ────────────────────────────────────────────────────────────
    val questions: List<MixedMediumQuestion> = emptyList(),
    val currentIndex: Int = 0,

    // ── Per-question state ───────────────────────────────────────────────────
    val wordPool: List<WordDragItem> = emptyList(),
    val buckets: Map<WordType, List<WordDragItem>> = emptyMap(),

    // ── Evaluation ───────────────────────────────────────────────────────────
    val isQuestionComplete: Boolean = false,
    val isAnswerCorrect: Boolean = false,
    val wrongCorrections: List<WrongCorrection> = emptyList(),
    val score: Int = 0,

    // ── Completion ───────────────────────────────────────────────────────────
    val isCompleted: Boolean = false,
    val feedbackTextRes: Int? = R.string.feedbackPhrases_1,
    val feedbackSubTextRes: Int? = R.string.feedbackGiveAnswerSubTitleCorrect_1,

    // ── Drag state (frame positions stored here, updated from Page) ──────────
    val itemFrames: Map<UUID, Rect> = emptyMap(),
    val bucketFrames: Map<WordType, Rect> = emptyMap(),
    val draggingItem: WordDragItem? = null,
    val dragStartCenter: Offset? = null,
    val dragOffset: Offset = Offset.Zero,
) {
    val currentQuestion: MixedMediumQuestion?
        get() = questions.getOrNull(currentIndex)

    val isLastIndex: Boolean
        get() = currentIndex >= questions.size - 1

    val showNext: Boolean
        get() = isQuestionComplete
}
