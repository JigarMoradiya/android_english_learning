package com.example.myapplication.main.age_group.from_5_to_7.opposite_words.match_opposites.view_model

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.data.generation.loader.OppositeDifficulty
import com.example.myapplication.data.generation.loader.OppositeWordsData
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchOppositesViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchOppositesUiState())
    val uiState: StateFlow<MatchOppositesUiState> = _uiState.asStateFlow()

    // ── Drag state — mutableStateOf so Canvas redraws without full recomposition ──
    var dragStart by mutableStateOf<Offset?>(null)
        private set
    var dragEnd by mutableStateOf<Offset?>(null)
        private set
    var draggingWord by mutableStateOf<String?>(null)
        private set
    private var totalDrag = Offset.Zero

    private var currentDifficulty = OppositeDifficulty.EASY

    fun loadDifficulty(difficulty: OppositeDifficulty) {
        currentDifficulty = difficulty
        generateNewBatch()
    }

    fun generateNewBatch() {
        val pairs = OppositeWordsData.getPairsForDifficulty(currentDifficulty).shuffled().take(5)
        _uiState.update {
            it.copy(
                round = it.round + 1,
                leftWords = pairs,
                rightWords = pairs.shuffled(),
                matchedWords = emptySet(),
                matchedOrder = emptyList(),
                showPopup = false
            )
        }
    }

    // ── Drag API ─────────────────────────────────────────────────────────────────

    fun startDrag(word: String, start: Offset) {
        if (_uiState.value.matchedWords.contains(word)) return
        totalDrag = Offset.Zero
        draggingWord = word
        dragStart = start
        dragEnd = start
    }

    fun updateDrag(delta: Offset) {
        totalDrag += delta
        dragEnd = dragStart!! + totalDrag
    }

    /** Call with the opposite key that the drag ended over, or null if missed */
    fun endDrag(target: String?) {
        val word = draggingWord
        if (word != null && target != null && !isOppositeMatched(target)) {
            if (isCorrectMatch(word, target)) {
                markWordAsMatched(word)
            } else {
                AudioPlayerManager.playSoundWrongAnswer()
            }
        }
        draggingWord = null
        dragStart = null
        dragEnd = null
        totalDrag = Offset.Zero
    }

    /** Returns true if the given word and opposite are a correct pair */
    fun isCorrectMatch(word: String, opposite: String): Boolean {
        val pair = _uiState.value.leftWords.find { it.word == word }
        return pair?.opposite == opposite
    }

    /** Returns true if this opposite has already been matched */
    fun isOppositeMatched(opposite: String): Boolean {
        val state = _uiState.value
        return state.matchedWords.any { key ->
            state.leftWords.find { it.word == key }?.opposite == opposite
        }
    }

    /** Marks a word as successfully matched; triggers popup when all done */
    fun markWordAsMatched(word: String) {
        val state = _uiState.value
        if (state.matchedWords.contains(word)) return
        val newMatched = state.matchedWords + word
        val newOrder = state.matchedOrder + word
        _uiState.update {
            it.copy(
                matchedWords = newMatched,
                matchedOrder = newOrder
            )
        }
        if (newMatched.size == state.leftWords.size) {
            viewModelScope.launch {
                delay(300)
                AudioPlayerManager.playSoundClap()
                _uiState.update {
                    it.copy(
                        showPopup = true,
                        feedbackTitleRes = feedbackTitles.random(),
                        feedbackSubTitleRes = R.string.you_matched_all_pairs
                    )
                }
            }
        } else {
            AudioPlayerManager.playSoundCorrectAnswer()
        }
    }

    fun closePopup() {
        _uiState.update { it.copy(showPopup = false) }
    }
}
