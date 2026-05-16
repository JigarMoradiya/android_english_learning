package com.example.myapplication.main.age_group.from_5_to_7.opposite_words

import com.example.myapplication.data.model.GrammarExampleModel

import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.utilities.TextToSpeechManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OppositeWordViewModel @Inject constructor(
    private val ttsManager: TextToSpeechManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(OppositeWordsUiState())
    val uiState: StateFlow<OppositeWordsUiState> = _uiState.asStateFlow()

    init {
        loadExamples()
    }

    private fun loadExamples() {
        _uiState.value = _uiState.value.copy(
            examples = listOf(
                GrammarExampleModel("Big → Small", "Apple is big, and teddy is small.", R.drawable.adjective_small),
                GrammarExampleModel("Happy → Sad", "I am happy, and you are sad.", R.drawable.adjective_happy),
                GrammarExampleModel("Tall → Short", "The boy is tall, and the girl is short.", R.drawable.adjective_tall)
            )
        )
    }

    fun onExampleTapped(example: GrammarExampleModel) {
        ttsManager.speak(example.speakText)
    }
}


data class OppositeWordsUiState(

    val explanationText0: String = """
        <font color='#EE0000'><b>What are Opposites?</b></font><br>
        Opposite words have different meanings.<br>
        👉 They show the reverse meaning of another word.<br>
        We use opposite words every day.
    """.trimIndent(),

    val explanationText1: String = """
        <font color='#EE0000'><b>Examples around you:</b></font><br>
        big - small<br>
        hot - cold<br>
        day - night<br>
        up - down<br>
        happy - sad
    """.trimIndent(),

    val explanationText2: String = """
        <font color='#EE0000'><b>Common Opposites:</b></font><br>

        <b>Size 📏</b><br>
        big - small<br>
        tall - short<br><br>

        <b>Temperature 🌡️</b><br>
        hot - cold<br><br>

        <b>Feelings 😊</b><br>
        happy - sad<br>
        excited - tired<br><br>

        <b>Direction ⬆️</b><br>
        up - down<br>
        left - right
    """.trimIndent(),

    val explanationText3: String = """
        <font color='#EE0000'><b>Easy Trick 💡</b></font><br>
        <b>Ask yourself:</b><br>
        What means the opposite?<br><br>

        <b>Example:</b><br>
        hot → cold<br>
        big → small<br>
        open → close
    """.trimIndent(),

    val explanationText4: String = """
        <font color='#EE0000'><b>Practice:</b></font><br>
        big → ?<br>
        Answer: <b>small</b><br><br>

        happy → ?<br>
        Answer: <b>sad</b>
    """.trimIndent(),

    val examples: List<GrammarExampleModel> = emptyList()
)