package com.example.myapplication.main.age_group.from_5_to_7.singular_plural.match_form.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.generation.loader.SingularPluralData
import com.example.myapplication.utils.AudioPlayerManager
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
class MatchSingularPluralViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchSingularPluralUiState())
    val uiState: StateFlow<MatchSingularPluralUiState> = _uiState.asStateFlow()

    init { loadPairs() }

    fun loadPairs() {
        val pairs = SingularPluralData.allPairs.shuffled().take(6)
        _uiState.update {
            it.copy(
                pairs = pairs,
                leftWords = pairs.map { p -> p.singular },
                rightWords = pairs.map { p -> p.plural }.shuffled(),
                selectedLeft = null,
                selectedRight = null,
                matchedKeys = emptySet(),
                wrongFlashLeft = null,
                wrongFlashRight = null,
                isCompleted = false
            )
        }
    }

    fun selectLeft(word: String) {
        if (_uiState.value.matchedKeys.contains(word)) return
        _uiState.update { it.copy(selectedLeft = word) }
        checkMatch()
    }

    fun selectRight(word: String) {
        val alreadyMatched = _uiState.value.matchedKeys.any { key ->
            _uiState.value.pairs.find { p -> p.singular == key }?.plural == word
        }
        if (alreadyMatched) return
        _uiState.update { it.copy(selectedRight = word) }
        checkMatch()
    }

    private fun checkMatch() {
        val state = _uiState.value
        val left = state.selectedLeft ?: return
        val right = state.selectedRight ?: return
        val pair = state.pairs.find { it.singular == left }
        val isMatch = pair?.plural == right

        if (isMatch) {
            AudioPlayerManager.playSoundCorrectAnswer()
            val newMatched = state.matchedKeys + left
            _uiState.update {
                it.copy(
                    matchedKeys = newMatched,
                    selectedLeft = null,
                    selectedRight = null,
                    isCompleted = newMatched.size == state.pairs.size
                )
            }
        } else {
            AudioPlayerManager.playSoundWrongAnswer()
            _uiState.update { it.copy(wrongFlashLeft = left, wrongFlashRight = right) }
            viewModelScope.launch {
                delay(600)
                _uiState.update {
                    it.copy(
                        selectedLeft = null,
                        selectedRight = null,
                        wrongFlashLeft = null,
                        wrongFlashRight = null
                    )
                }
            }
        }
    }
}
