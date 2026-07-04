package com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.myapplication.R
import com.example.myapplication.common.AppToolbarDropDownOnRight
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.helper.distance
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.helper.getStrokesForLetter
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.helper.sampleStroke
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.BottomTracingControls
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.CenterLearningLayout
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.utilities.AudioPhonicsManager
import com.example.myapplication.utilities.TextToSpeechManager
import com.example.myapplication.utilities.pref.AppPreferencesHelper
import com.example.myapplication.utils.extensions.OtherEx.safeAction
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AlphabetTracingViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val prefs: AppPreferencesHelper,
    private val ttsManager: TextToSpeechManager,
    private val audioPhonicsManager: AudioPhonicsManager
) : ViewModel() {

    var uiState by mutableStateOf(LetterTracingUiState())
        private set

    var isPreviewMode: Boolean = false

    private val letters = ('A'..'Z').toList()
    val lettersData: List<Pair<String, String>> =
        LetterRepository.all.map { data ->
            data.letter to data.mainWord
        }

    private val tolerance = 70f
    private val spacing = 6f

    private var cachedGuides: List<List<Offset>> = emptyList()
    private var cachedFrame: Rect? = null

    private var completedLettersInSession = mutableSetOf<String>()
    private var startTimeMs: Long = System.currentTimeMillis()

    init {
        val savedModeStr = prefs.getCustomParam("alphabetTracing_mode", LetterMode.UPPERCASE.name)
        val savedMode = runCatching { LetterMode.valueOf(savedModeStr) }.getOrDefault(LetterMode.UPPERCASE)
        val savedIndex = prefs.getCustomParamInt("alphabetTracing_index_${savedMode.name.lowercase()}", 0).coerceIn(0, letters.lastIndex)
        uiState = uiState.copy(mode = savedMode, currentIndex = savedIndex)
    }


    // -------------------------------
    // 🎨 COLORS – 5 per letter, chosen to contrast with each letter's image
    // -------------------------------


    private val selectedColors: List<Color> = letterPalettes.map { it.random() }

    fun getLetterColor(): Color {
        return selectedColors[uiState.currentIndex % selectedColors.size]
    }

    fun previewAllStrokes(guides: List<List<Offset>>) {
        if (uiState.finishedStrokes.isEmpty() && guides.isNotEmpty()) {
            uiState = uiState.copy(
                finishedStrokes = guides,
                strokeIndex = guides.size
            )
        }
    }

    val currentLetter: Char
        get() {
            val base = letters[uiState.currentIndex]
            return if (uiState.mode == LetterMode.UPPERCASE) base else base.lowercaseChar()
        }

    val phonicsSound: String
        get() = LetterRepository.all.getOrNull(uiState.currentIndex)
            ?.phonicsSound?.split(" ")?.firstOrNull() ?: ""

    // -------------------------------
    // 📐 GUIDES
    // -------------------------------
    fun getGuides(frame: Rect): List<List<Offset>> {

        if (cachedFrame == frame && cachedGuides.isNotEmpty()) {
            return cachedGuides
        }

        cachedFrame = frame

        val strokes = getStrokesForLetter(currentLetter, uiState.mode)

        cachedGuides = strokes.map {
            sampleStroke(it, frame, spacing)
        }

        return cachedGuides
    }

    var isPhonicsHighlighted by mutableStateOf(false)
        private set

    // -------------------------------
    // 🔊 PHONICS SOUND
    // -------------------------------
    fun playPhonicsSound() {
        val letter = currentLetter.lowercaseChar()
        audioPhonicsManager.playPhonicsSound("phonics_letter/sound_$letter")
        isPhonicsHighlighted = true
        viewModelScope.launch {
            delay(1500)
            isPhonicsHighlighted = false
        }
    }


    // -------------------------------
    // ✋ START STROKE
    // -------------------------------
    fun startStroke(touch: Offset) {

        val guide = cachedGuides.getOrNull(uiState.strokeIndex) ?: return
        val startPoint = guide.first()

        if (distance(touch, startPoint) <= tolerance) {

            uiState = uiState.copy(
                isOnStroke = true,
                drawnPoints = listOf(startPoint),
                progressIndex = 0
            )
            playPhonicsSound()
        }
    }

    // -------------------------------
    // 👉 TAP (DOT SUPPORT)
    // -------------------------------
    fun onTap(position: Offset) {

        val guide = cachedGuides.getOrNull(uiState.strokeIndex) ?: return

        if (guide.size <= 2) {
            val target = guide.first()
            if (distance(position, target) <= tolerance) {
                completeCurrentStroke()
            }
        }
    }

    private fun completeCurrentStroke() {
        uiState = uiState.copy(
            finishedStrokes = uiState.finishedStrokes + listOf(cachedGuides[uiState.strokeIndex]),
            strokeIndex = uiState.strokeIndex + 1,
            drawnPoints = emptyList(),
            progressIndex = 0,
            isOnStroke = false,
            isWaitingForNextStroke = true
        )
        if (uiState.strokeIndex >= cachedGuides.size) markCurrentLetterCompleted()
    }

    private val beginnerPraise = listOf(
        "Good start!", "Nice try!", "You are awesome!", "Keep going!",
        "That's a good effort!", "Well done!", "Great job!", "Very nice!"
    )
    private val advancedPraise = listOf(
        "You are amazing!", "You are doing great!", "Wow! That was fantastic!",
        "Superstar work!", "You nailed it!", "You're doing great!",
        "That was brilliant!", "You are a superstar!", "That was perfect!",
        "Keep going!", "Outstanding performance!", "You're doing incredible!"
    )

    private fun markCurrentLetterCompleted() {
        completedLettersInSession.add(currentLetter.toString())
        val praise = if (uiState.currentIndex < 3) beginnerPraise.random() else advancedPraise.random()
        ttsManager.speak(praise)
    }

    // -------------------------------
    // 🧲 FIND NEAREST POINT
    // -------------------------------
    private fun findNearestIndex(
        touch: Offset,
        guide: List<Offset>,
        start: Int,
        end: Int
    ): Int {

        var bestIndex = -1
        var minDist = Float.MAX_VALUE

        for (i in start..end) {
            val d = distance(touch, guide[i])
            if (d < minDist) {
                minDist = d
                bestIndex = i
            }
        }

        return if (bestIndex != -1 && minDist <= tolerance) bestIndex else -1
    }

    // -------------------------------
    // ✍️ UPDATE STROKE
    // -------------------------------
    fun updateStroke(touch: Offset) {

        val guides = cachedGuides

        // -------------------------------
        // 🔥 WAIT FOR NEXT STROKE
        // -------------------------------
        if (uiState.isWaitingForNextStroke) {

            val guide = guides.getOrNull(uiState.strokeIndex) ?: return
            val startPoint = guide.first()

            if (distance(touch, startPoint) <= tolerance) {

                uiState = uiState.copy(
                    isOnStroke = true,
                    isWaitingForNextStroke = false,
                    drawnPoints = listOf(startPoint),
                    progressIndex = 0
                )
                playPhonicsSound()
            }

            return
        }

        // -------------------------------
        // NORMAL DRAWING
        // -------------------------------
        if (!uiState.isOnStroke) return

        val guide = guides.getOrNull(uiState.strokeIndex) ?: return

        val currentIndex = uiState.progressIndex
        val searchEnd = (currentIndex + 20).coerceAtMost(guide.lastIndex)

        val foundIndex = findNearestIndex(
            touch,
            guide,
            currentIndex + 1,
            searchEnd
        )

        if (foundIndex == -1) return
        if (foundIndex < currentIndex) return

        // -------------------------------
        // SNAP TO GUIDE (REAL SNAPPING)
        // -------------------------------
        val newPoints = uiState.drawnPoints + guide.subList(
            currentIndex + 1,
            foundIndex + 1
        )

        // -------------------------------
        // ✅ COMPLETE STROKE
        // -------------------------------
        if (foundIndex == guide.lastIndex) {

            val nextStrokeIndex = uiState.strokeIndex + 1

            uiState = uiState.copy(
                finishedStrokes = uiState.finishedStrokes + listOf(newPoints),
                drawnPoints = emptyList(),
                strokeIndex = nextStrokeIndex,
                progressIndex = 0,
                isOnStroke = false,
                isWaitingForNextStroke = true
            )

            if (nextStrokeIndex >= cachedGuides.size) markCurrentLetterCompleted()

        } else {

            uiState = uiState.copy(
                drawnPoints = newPoints,
                progressIndex = foundIndex
            )
        }
    }

    // -------------------------------
    // 🔁 NAVIGATION
    // -------------------------------
    fun next() {
        val nextIndex = (uiState.currentIndex + 1) % letters.size
        prefs.setCustomParamInt("alphabetTracing_index_${uiState.mode.name.lowercase()}", nextIndex)
        resetForIndex(nextIndex)
    }

    fun previous() {
        val prevIndex = if (uiState.currentIndex == 0) letters.lastIndex else uiState.currentIndex - 1
        prefs.setCustomParamInt("alphabetTracing_index_${uiState.mode.name.lowercase()}", prevIndex)
        resetForIndex(prevIndex)
    }

    fun changeMode(mode: LetterMode) {
        if (mode == uiState.mode) return
        recordSession()
        prefs.setCustomParamInt("alphabetTracing_index_${uiState.mode.name.lowercase()}", uiState.currentIndex)
        val savedIndex = prefs.getCustomParamInt("alphabetTracing_index_${mode.name.lowercase()}", 0).coerceIn(0, letters.lastIndex)
        prefs.setCustomParam("alphabetTracing_mode", mode.name)
        uiState = uiState.copy(mode = mode, currentIndex = savedIndex)
        resetState()
    }

    private fun recordSession() {
        if (completedLettersInSession.isEmpty()) return
        val duration = ((System.currentTimeMillis() - startTimeMs) / 1000).toInt()
        sessionRepository.record(LearningSession(
            moduleId = ModuleID.ALPHABET_TRACING,
            ageGroup = AgeGroup.THREE_TO_FIVE,
            durationSeconds = duration,
            score = completedLettersInSession.size,
            totalQuestions = 0,
            correctItems = completedLettersInSession.sorted(),
            subConfig = uiState.mode.name
        ))
        completedLettersInSession = mutableSetOf()
        startTimeMs = System.currentTimeMillis()
    }

    override fun onCleared() {
        super.onCleared()
        recordSession()
    }

    fun clear() {
        if (uiState.drawnPoints.isNotEmpty() || uiState.finishedStrokes.isNotEmpty()) {
            resetState()
        }
    }

    private fun resetForIndex(index: Int) {
        uiState = uiState.copy(currentIndex = index)
        resetState()
    }

    private fun resetState() {
        uiState = uiState.copy(
            strokeIndex = 0,
            progressIndex = 0,
            drawnPoints = emptyList(),
            finishedStrokes = emptyList(),
            isOnStroke = false,
            isWaitingForNextStroke = false
        )

        cachedGuides = emptyList()
        cachedFrame = null
    }

}