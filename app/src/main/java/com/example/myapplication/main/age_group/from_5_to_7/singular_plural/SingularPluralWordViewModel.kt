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
        many 🐱🐱 cat<font color='#0B7A3B'><b>s</b></font><br>
        many 🍎🍎 apple<font color='#0B7A3B'><b>s</b></font><br>
        many 🚗🚗 car<font color='#0B7A3B'><b>s</b></font><br>
        many ⭐⭐ star<font color='#0B7A3B'><b>s</b></font>
    """.trimIndent(),

    val explanationText3: String = """
        <font color='#EE0000'><b>How to make Plural? 💡</b></font><br>

        <b>Rule 1:</b> Most words → add <font color='#0B7A3B'><b>-s</b></font><br>
        cat → cat<font color='#0B7A3B'><b>s</b></font><br>
        dog → dog<font color='#0B7A3B'><b>s</b></font><br>
        book → book<font color='#0B7A3B'><b>s</b></font><br>
        car → car<font color='#0B7A3B'><b>s</b></font><br><br>

        <b>Rule 2:</b> Words ending in s, sh, ch, x → add <font color='#0B7A3B'><b>-es</b></font><br>
        bus → bus<font color='#0B7A3B'><b>es</b></font><br>
        box → box<font color='#0B7A3B'><b>es</b></font><br>
        brush → brush<font color='#0B7A3B'><b>es</b></font><br>
        watch → watch<font color='#0B7A3B'><b>es</b></font><br><br>

        <b>Rule 3:</b> Words ending in <b>y</b><br>
        Change <b>y</b> → <font color='#0B7A3B'><b>ies</b></font><br>
        baby → bab<font color='#0B7A3B'><b>ies</b></font><br>
        city → cit<font color='#0B7A3B'><b>ies</b></font><br>
        story → stor<font color='#0B7A3B'><b>ies</b></font><br><br>

        <b>Rule 4:</b> Some words ending in <b>f / fe</b><br>
        Change to <font color='#0B7A3B'><b>ves</b></font><br>
        leaf → lea<font color='#0B7A3B'><b>ves</b></font><br>
        wolf → wol<font color='#0B7A3B'><b>ves</b></font><br>
        knife → kni<font color='#0B7A3B'><b>ves</b></font>
    """.trimIndent(),

    val explanationText4: String = """
        <font color='#EE0000'><b>Special Words 🌟</b></font><br><br>

        Some words don't change!<br>
        fish → fish<br>
        sheep → sheep<br>
        deer → deer<br><br>

        Some words change in a special way!<br>
        child → child<font color='#0B7A3B'><b>ren</b></font><br>
        man → m<font color='#0B7A3B'><b>e</b></font>n<br>
        woman → wom<font color='#0B7A3B'><b>e</b></font>n<br>
        tooth → t<font color='#0B7A3B'><b>ee</b></font>th<br>
        foot → f<font color='#0B7A3B'><b>ee</b></font>t
    """.trimIndent(),

    val examples: List<GrammarExampleModel> = emptyList()
)