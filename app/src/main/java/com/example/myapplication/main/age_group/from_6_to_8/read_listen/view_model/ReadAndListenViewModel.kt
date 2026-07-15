package com.example.myapplication.main.age_group.from_6_to_8.read_listen.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.generation.loader.SentenceBuilderLogic
import com.example.myapplication.data.model.ReadSentenceItemNew
import com.example.myapplication.data.model.UnitSelectionScreen
import com.example.myapplication.data.model.displayTitle
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.main.age_group.from_6_to_8.common.unit.data.SentenceProgressManager
import com.example.myapplication.utilities.TextToSpeechManager
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

@HiltViewModel
class ReadAndListenViewModel @Inject constructor(
    private val progressManager: SentenceProgressManager,
    private val ttsManager: TextToSpeechManager,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private var speakingJob: Job? = null
    private val startTimeMs = System.currentTimeMillis()

    private val _uiState = MutableStateFlow(ReadAndListenUiState())
    val uiState: StateFlow<ReadAndListenUiState> = _uiState

    // user when joined sentences
    private var wordRanges: List<IntRange> = emptyList()

    fun setScreenTypeAndLessonData(
        screenType: UnitSelectionScreen,
        lessonData: ReadSentenceItemNew
    ) {
        Log.e("jigarLogs","lessonData = "+ Gson().toJson(lessonData))
        Log.e("jigarLogs","lessonData title = "+ lessonData.level?.title)
        _uiState.update {
            it.copy(screenType = screenType, lessonData = lessonData)
        }
    }

    // Current sentence
    val currentSentence: String
        get() = _uiState.value.lessonData
            ?.sentences
            ?.getOrNull(_uiState.value.currentSentenceIndex)
            ?.text
            ?: ""

    // Words split
    val words: List<String>
        get() = currentSentence.split(" ")

    // First
    val isAtFirst: Boolean
        get() = _uiState.value.currentSentenceIndex == 0

    // Last
    val isAtLast: Boolean
        get() = _uiState.value.lessonData?.let {
            _uiState.value.currentSentenceIndex == it.sentences.size - 1
        } ?: false

    // Next
    fun nextSentence() {
        val state = _uiState.value
        val lesson = state.lessonData ?: return

        if (state.currentSentenceIndex < lesson.sentences.size - 1) {
            stopSpeaking()
            _uiState.update {
                it.copy(currentSentenceIndex = it.currentSentenceIndex + 1)
            }
            checkIfCompleted()
        }
    }

    // Previous
    fun previousSentence() {
        val state = _uiState.value

        if (state.currentSentenceIndex > 0) {
            stopSpeaking()
            _uiState.update {
                it.copy(currentSentenceIndex = it.currentSentenceIndex - 1)
            }
        }
    }

    // Toggle join
    fun toggleJoinWords() {
        stopSpeaking()
        _uiState.update {
            it.copy(isSentenceJoined = !it.isSentenceJoined)
        }
    }

    // MARK: - Auto Completion
    private fun checkIfCompleted() {

        val state = _uiState.value
        val lesson = state.lessonData ?: return
        if (state.hasMarkedComplete) return

        if (state.currentSentenceIndex == lesson.sentences.size - 1) {
            progressManager.markCompleted(
                type = state.screenType,
                lessonId = lesson.id
            )
            sessionRepository.record(
                LearningSession(
                    moduleId = ModuleID.READ_LISTEN,
                    ageGroup = AgeGroup.SIX_TO_EIGHT,
                    durationSeconds = ((System.currentTimeMillis() - startTimeMs) / 1000).toInt(),
                    score = 0,
                    totalQuestions = 0,
                    correctItems = listOf(lesson.title),
                    wrongItems = emptyList(),
                    subConfig = lesson.level?.title ?: "Short Sentence",
                    lessonTitle = lesson.title,
                    chapterTitle = lesson.unit?.displayTitle
                )
            )
            _uiState.update {
                it.copy(hasMarkedComplete = true)
            }
        }
    }

    fun speak() {
        val words = words
        if (words.isEmpty()) return

        stopSpeaking()

        if (_uiState.value.isSentenceJoined) {
            speakJoined(words)
        } else {
            speakSplit(words)
        }
    }

    private fun prepareWordRanges(sentence: String, words: List<String>) {
        val ranges = mutableListOf<IntRange>()
        var currentIndex = 0

        words.forEach { word ->
            val start = sentence.indexOf(word, currentIndex)
            val end = start + word.length
            ranges.add(start until end)
            currentIndex = end
        }

        wordRanges = ranges
    }

    private fun getWordIndex(charIndex: Int): Int {
        return wordRanges.indexOfFirst { charIndex in it }
            .takeIf { it != -1 } ?: 0
    }

    private fun speakJoined(words: List<String>) {

        speakingJob?.cancel()
        ttsManager.stop()

        val sentence = words.joinToString(" ")

        prepareWordRanges(sentence, words)

        speakingJob = viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isSpeaking = true,
                    joinSentenceSpeakingIndex = 0
                )
            }

            ttsManager.onWordSpoken = onWordSpoken@ { charIndex ->

                if (!_uiState.value.isSpeaking) return@onWordSpoken

                val index = getWordIndex(charIndex)

                _uiState.update {
                    it.copy(joinSentenceSpeakingIndex = index)
                }
            }

            ttsManager.speak(
                text = sentence,
                utteranceId = "joined",
                isAddInQueue = true
            ) {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isSpeaking = false,
                            joinSentenceSpeakingIndex = null
                        )
                    }
                }
            }
        }
    }

    // Echo / read-listen-repeat: speak the whole sentence, then prompt the child
    // to repeat it during a length-based silent gap. (item 1.3)
    fun echo() {
        val words = words
        if (words.isEmpty()) return

        stopSpeaking()

        val sentence = words.joinToString(" ")
        prepareWordRanges(sentence, words)

        speakingJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSpeaking = true,
                    echoPhase = EchoPhase.LISTENING,
                    joinSentenceSpeakingIndex = 0
                )
            }

            ttsManager.onWordSpoken = onWordSpoken@ { charIndex ->
                if (!_uiState.value.isSpeaking) return@onWordSpoken
                _uiState.update { it.copy(joinSentenceSpeakingIndex = getWordIndex(charIndex)) }
            }

            ttsManager.speak(text = sentence, utteranceId = "echo", isAddInQueue = true) {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isSpeaking = false,
                            joinSentenceSpeakingIndex = null,
                            echoPhase = EchoPhase.YOUR_TURN
                        )
                    }
                    delay((SentenceBuilderLogic.echoGapSeconds(words.size) * 1000).toLong())
                    if (_uiState.value.echoPhase == EchoPhase.YOUR_TURN) {
                        _uiState.update { it.copy(echoPhase = EchoPhase.IDLE) }
                    }
                }
            }
        }
    }

    private fun speakSplit(words: List<String>) {

        ttsManager.stop()
        speakingJob?.cancel()

        speakingJob = viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isSpeaking = true,
                    splitSentenceWordIndex = 0
                )
            }

            for ((index, word) in words.withIndex()) {

                if (!isActive) return@launch

                _uiState.update {
                    it.copy(splitSentenceWordIndex = index)
                }

                speakWord(word, "word_$index")
            }

            _uiState.update {
                it.copy(
                    isSpeaking = false,
                    splitSentenceWordIndex = -1
                )
            }
        }
    }

    private suspend fun speakWord(text: String, id: String) =
        suspendCancellableCoroutine<Unit> { cont ->
            ttsManager.speak(text, id, isAddInQueue = true) {
                if (cont.isActive) cont.resume(Unit) { cause, _, _ -> }
            }
        }

    fun stopSpeaking() {
        speakingJob?.cancel()
        speakingJob = null

        ttsManager.stop()
        ttsManager.onWordSpoken = null

        _uiState.update {
            it.copy(
                isSpeaking = false,
                joinSentenceSpeakingIndex = null,
                splitSentenceWordIndex = -1,
                echoPhase = EchoPhase.IDLE
            )
        }
    }
}