package com.example.myapplication.main.age_group.from_5_to_7.singular_plural

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
class SingularPluralWordViewModel @Inject constructor(
    private val ttsManager: TextToSpeechManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SingularPluralUiState())
    val uiState: StateFlow<SingularPluralUiState> = _uiState.asStateFlow()

    init {
        loadExamples()
    }

    private fun loadExamples() {
        _uiState.value = _uiState.value.copy(
            examples = listOf(
                GrammarExampleModel("book → books", "One book. Many books.", R.drawable.book),
                GrammarExampleModel("dog → dogs", "One dog. Many dogs.", R.drawable.dog),
                GrammarExampleModel("cat → cats", "One cat. Many cats.", R.drawable.cat)
            )
        )
    }

    fun onExampleTapped(example: GrammarExampleModel) {
        ttsManager.speak(example.speakText)
    }
}


data class SingularPluralUiState(

    val explanationText0: String = """
        <font color='#EE0000'><b>What is Singular and Plural?</b></font><br>
        <b>Singular</b> means ONE thing.<br>
        <b>Plural</b> means MORE THAN ONE thing.<br>
        👉 We use plural when there is more than one!
    """.trimIndent(),

    val explanationText1: String = """
        <font color='#EE0000'><b>Singular Examples:</b></font><br>
        one 🐱 cat<br>
        one 🍎 apple<br>
        one 🚗 car<br>
        one ⭐ star
    """.trimIndent(),

    val explanationText2: String = """
        <font color='#EE0000'><b>Plural Examples:</b></font><br>
        many 🐱🐱 cats<br>
        many 🍎🍎 apple<br>
        many 🚗🚗 cars<br>
        many ⭐⭐ stars
    """.trimIndent(),

    val explanationText3: String = """
        <font color='#EE0000'><b>How to make Plural? 💡</b></font><br>

        <b>Rule 1:</b> Most words → add <b>-s</b><br>
        cat → cats<br>
        dog → dogs<br>
        book → books<br>
        car → cars<br><br>

        <b>Rule 2:</b> Words ending in s, sh, ch, x → add <b>-es</b><br>
        bus → buses<br>
        box → boxes<br>
        brush → brushes<br>
        watch → watches<br><br>

        <b>Rule 3:</b> Words ending in <b>y</b><br>
        Change <b>y → ies</b><br>
        baby → babies<br>
        city → cities<br>
        story → stories<br><br>

        <b>Rule 4:</b> Some words ending in <b>f / fe</b><br>
        Change to <b>ves</b><br>
        leaf → leaves<br>
        wolf → wolves<br>
        knife → knives
    """.trimIndent(),

    val explanationText4: String = """
        <font color='#EE0000'><b>Special Words 🌟</b></font><br><br>

        Some words don't change!<br>
        fish → fish<br>
        sheep → sheep<br>
        deer → deer<br><br>

        Some words change in a special way!<br>
        child → children<br>
        man → men<br>
        woman → women<br>
        tooth → teeth<br>
        foot → feet
    """.trimIndent(),

    val examples: List<GrammarExampleModel> = emptyList()
)