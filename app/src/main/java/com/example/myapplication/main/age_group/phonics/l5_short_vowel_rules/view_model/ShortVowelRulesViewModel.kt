package com.example.myapplication.main.age_group.phonics.l5_short_vowel_rules.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.myapplication.utilities.AudioPhonicsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

// ── Models ────────────────────────────────────────────────────────────────────

data class RuleWordExample(
    val id: String = UUID.randomUUID().toString(),
    val prefix: String,
    val suffix: String,
    val highlightPrefix: Boolean = false
) {
    val word: String get() = prefix + suffix
}

data class RuleWordExampleGroup(
    val id: String = UUID.randomUUID().toString(),
    val label: String?,
    val examples: List<RuleWordExample>
)

data class RuleComparison(
    val id: String = UUID.randomUUID().toString(),
    val label: String,
    val condition: String,
    val color: Color,
    val words: List<String>
)

data class SpellingRule(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val pattern: String,
    val emoji: String,
    val color: Color,
    val shadowColor: Color,
    val ruleStatement: String,
    val whenToUse: List<String>,
    val whyItWorks: String,
    val watchOut: List<String>,
    val tip: String,
    val comparisons: List<RuleComparison>,
    val exampleGroups: List<RuleWordExampleGroup>
)

// ── Data ──────────────────────────────────────────────────────────────────────

val spellingRulesData: List<SpellingRule> = listOf(

    SpellingRule(
        name = "Floss Rule",
        pattern = "ff · ll · ss · zz",
        emoji = "🧵",
        color = Color(0xFF00897B),
        shadowColor = Color(0xFF00695C),
        ruleStatement = "In a **one-syllable** word with a **short vowel**, double the final **F, L, S, or Z**.",
        whenToUse = listOf(
            "Word has **1 syllable** — one clap: **off** ✓  **bell** ✓  **miss** ✓",
            "The vowel sounds **short** — a, e, i, o, u said quickly",
            "The word ends in **F, L, S, or Z**",
            "All **3 conditions** must be true — then double the last letter!"
        ),
        whyItWorks = "These 4 letters make sounds you can **stretch** — **ffff · llll · ssss · zzzz**. Doubling them tells the reader: the vowel before is **short**.\n\nWithout the double, you might read a **long vowel** instead. So **short vowel + double consonant** = the short sound is locked in.",
        watchOut = listOf(
            "Only **F, L, S, Z** double — not C, K, P, T or any other letter",
            "For **/k/** after a short vowel → use **CK** not CC — '**duck**' not '**ducc**'",
            "'**bee**' has **EE** (vowel team, long sound) — totally different rule",
            "Multi-syllable words like '**travel**' do **not** follow this rule"
        ),
        tip = "The word **FLOSS** follows its own rule — 1 syllable, short **O**, ends in **SS**. Let the word remind you!",
        comparisons = listOf(
            RuleComparison(
                label = "Short vowel → DOUBLE",
                condition = "1 syllable + short vowel + ends F/L/S/Z",
                color = Color(0xFF00897B),
                words = listOf("off", "bell", "miss", "buzz", "cliff", "tall", "grass", "fizz", "shell", "dress")
            ),
            RuleComparison(
                label = "Long vowel → SINGLE",
                condition = "Long vowel → no doubling",
                color = Color(0xFF757575),
                words = listOf("leaf", "feel", "tool", "fuse", "pool", "sail")
            )
        ),
        exampleGroups = listOf(
            RuleWordExampleGroup(label = null, examples = listOf(
                RuleWordExample(prefix = "o",   suffix = "ff"),
                RuleWordExample(prefix = "be",  suffix = "ll"),
                RuleWordExample(prefix = "mi",  suffix = "ss"),
                RuleWordExample(prefix = "bu",  suffix = "zz"),
                RuleWordExample(prefix = "cli", suffix = "ff"),
                RuleWordExample(prefix = "ta",  suffix = "ll"),
                RuleWordExample(prefix = "gra", suffix = "ss"),
                RuleWordExample(prefix = "fi",  suffix = "zz"),
                RuleWordExample(prefix = "she", suffix = "ll"),
                RuleWordExample(prefix = "dre", suffix = "ss")
            ))
        )
    ),

    SpellingRule(
        name = "-ck Rule",
        pattern = "c · ck · k",
        emoji = "🔑",
        color = Color(0xFFF57C00),
        shadowColor = Color(0xFFE65100),
        ruleStatement = "**C**, **CK**, and **K** all make the **/k/** sound — which one you use depends on **where** in the word and **what comes before** it.",
        whenToUse = listOf(
            "Use **C** → word **starts** with /k/ before **A, O, U** → cat, cod, cup",
            "Use **CK** → word **ends** with /k/ right after a **short vowel** → duck, back, kick",
            "Use **K** → word **starts** with /k/ before **E or I** → key, kit, king"
        ),
        whyItWorks = "**C** is tricky — before **E** and **I** it says **/s/** (city, cent), not /k/. So English uses **K** at the start before E and I instead.\n\nAfter a **short vowel** at the end, English uses the pair **CK** — two letters but **one /k/ sound**. English **never** writes 'cc' or 'kk' for the /k/ sound.",
        watchOut = listOf(
            "**Never** write 'cc' or 'kk' — English does not do this for /k/",
            "'**duck**' ✓ · '**dukk**' ✗ · '**ducc**' ✗ · '**duk**' ✗",
            "'**cat**' needs **C** (before A) — '**kat**' is wrong",
            "'**kit**' needs **K** (before I) — '**cit**' would say /s/ sound",
            "**CK** is two letters but **one /k/ sound** — do not say /ck/"
        ),
        tip = "Short vowel at the end? → Always **CK**. Think of the duck: **du-CK** — quack!",
        comparisons = listOf(
            RuleComparison(label = "Use C", condition = "Word start + before A, O, U", color = Color(0xFF1E88E5), words = listOf("cat", "cup", "cod", "cab", "cot", "cut")),
            RuleComparison(label = "Use CK", condition = "Word end + after short vowel", color = Color(0xFFF57C00), words = listOf("duck", "back", "kick", "rock", "luck", "pack", "check", "thick")),
            RuleComparison(label = "Use K", condition = "Word start + before E, I", color = Color(0xFF8E24AA), words = listOf("key", "kit", "king", "kept", "kid", "kite"))
        ),
        exampleGroups = listOf(
            RuleWordExampleGroup(label = "C", examples = listOf(
                RuleWordExample(prefix = "c", suffix = "at",  highlightPrefix = true),
                RuleWordExample(prefix = "c", suffix = "up",  highlightPrefix = true),
                RuleWordExample(prefix = "c", suffix = "od",  highlightPrefix = true),
                RuleWordExample(prefix = "c", suffix = "ot",  highlightPrefix = true),
                RuleWordExample(prefix = "c", suffix = "ab",  highlightPrefix = true),
                RuleWordExample(prefix = "c", suffix = "ut",  highlightPrefix = true)
            )),
            RuleWordExampleGroup(label = "CK", examples = listOf(
                RuleWordExample(prefix = "du",  suffix = "ck"),
                RuleWordExample(prefix = "ba",  suffix = "ck"),
                RuleWordExample(prefix = "ki",  suffix = "ck"),
                RuleWordExample(prefix = "ro",  suffix = "ck"),
                RuleWordExample(prefix = "lu",  suffix = "ck"),
                RuleWordExample(prefix = "pa",  suffix = "ck"),
                RuleWordExample(prefix = "che", suffix = "ck"),
                RuleWordExample(prefix = "thi", suffix = "ck"),
                RuleWordExample(prefix = "blo", suffix = "ck"),
                RuleWordExample(prefix = "so",  suffix = "ck")
            )),
            RuleWordExampleGroup(label = "K", examples = listOf(
                RuleWordExample(prefix = "k", suffix = "ey",  highlightPrefix = true),
                RuleWordExample(prefix = "k", suffix = "it",  highlightPrefix = true),
                RuleWordExample(prefix = "k", suffix = "ing", highlightPrefix = true),
                RuleWordExample(prefix = "k", suffix = "id",  highlightPrefix = true),
                RuleWordExample(prefix = "k", suffix = "ept", highlightPrefix = true),
                RuleWordExample(prefix = "k", suffix = "eg",  highlightPrefix = true)
            ))
        )
    ),

    SpellingRule(
        name = "-ng Rule",
        pattern = "ng",
        emoji = "🔔",
        color = Color(0xFF7B1FA2),
        shadowColor = Color(0xFF4A148C),
        ruleStatement = "**NG** at the end of a word = **one sound /ŋ/**. N and G team up into a brand-new sound — there is **no separate /g/** at the end.",
        whenToUse = listOf(
            "End sound is **/ŋ/** after a short vowel → write **NG** → ring, song, king",
            "End sound is plain **/n/** → write just **N** → bin, sun, hen",
            "The **G** in word-final NG is **completely silent**",
            "**NG** = one new sound, not N + G separately"
        ),
        whyItWorks = "**NG** is a digraph — two letters that make **one sound** that neither letter can make alone.\n\nThe **/ŋ/** sound comes from the **back of the throat** (not the front like /n/). Press your tongue to the back of your mouth — that is it!\n\nIn '**finger**' you hear N + G as **separate sounds** (fin·ger). But at a **word ending**, NG always blends into just **/ŋ/**.",
        watchOut = listOf(
            "Do **not** say '**ring-g**' with a hard G — the G is silent at the end",
            "'**ring**' has **3 sounds**: /r/ /ɪ/ /ŋ/ — not 4",
            "'**finger**' has N + G separately (fin·ger) — **different** from word-end NG",
            "**NG** = one sound /ŋ/ · **NK** = two sounds /ŋ/ + /k/ · 'ring' vs '**rink**'"
        ),
        tip = "Press the **back** of your tongue up and hum — that is **/ŋ/**! You can't make it with just N or just G alone.",
        comparisons = listOf(
            RuleComparison(label = "NG  →  one /ŋ/ sound", condition = "One nasal sound — no /g/ at the end", color = Color(0xFF7B1FA2), words = listOf("ring", "song", "king", "sing", "lung", "bang", "long", "hung", "thing", "strong")),
            RuleComparison(label = "N  →  plain /n/", condition = "Plain /n/ sound — from the front", color = Color(0xFF757575), words = listOf("bin", "sun", "hen", "tan", "fin", "run", "pen", "man"))
        ),
        exampleGroups = listOf(
            RuleWordExampleGroup(label = null, examples = listOf(
                RuleWordExample(prefix = "ri",   suffix = "ng"),
                RuleWordExample(prefix = "so",   suffix = "ng"),
                RuleWordExample(prefix = "ki",   suffix = "ng"),
                RuleWordExample(prefix = "si",   suffix = "ng"),
                RuleWordExample(prefix = "lu",   suffix = "ng"),
                RuleWordExample(prefix = "ba",   suffix = "ng"),
                RuleWordExample(prefix = "lo",   suffix = "ng"),
                RuleWordExample(prefix = "hu",   suffix = "ng"),
                RuleWordExample(prefix = "thi",  suffix = "ng"),
                RuleWordExample(prefix = "stro", suffix = "ng")
            ))
        )
    ),

    SpellingRule(
        name = "-nk Rule",
        pattern = "nk",
        emoji = "⚓",
        color = Color(0xFF1565C0),
        shadowColor = Color(0xFF0D47A1),
        ruleStatement = "**NK** after a short vowel = **two sounds**: **/ŋ/** (same as -ng) then **/k/**. It is the NG sound with a **/k/** clipped on at the end.",
        whenToUse = listOf(
            "Hear **/ŋk/** at the end? → write **NK** → bank, sink, honk",
            "Hear **/k/** at the end? → **NK**. No /k/? → **NG**",
            "Count the end sounds: **1 sound** → NG · **2 sounds** → NK",
            "Both appear after **short vowels**"
        ),
        whyItWorks = "The **N** in NK does **not** say regular /n/ — together with K it makes the **/ŋk/ blend**.\n\n'**bank**' has **4 sounds**: /b/ /æ/ **/ŋ/** **/k/** — not /b/ /æ/ /n/ /k/.\n\nThink: **NK = NG + K**. The **/ŋ/** is the same sound as in -ng words. NK just **adds /k/** right after it.",
        watchOut = listOf(
            "The **N** in NK is NOT regular /n/ — it is **/ŋ/** (back of throat)",
            "'**sink**' sounds like /sɪ**ŋk**/, not /sɪ**nk**/ — hear the difference",
            "**NG** stops at /ŋ/. **NK** has /ŋ/ then **/k/** — 'ring' vs '**rink**'",
            "Spelling is always **NK** — never 'ngk'"
        ),
        tip = "Say '**bang**' then '**bank**' — the only difference is the **/k/** at the end. That /k/ is what makes **NK** different from **NG**!",
        comparisons = listOf(
            RuleComparison(label = "NK  →  /ŋ/ + /k/", condition = "Two sounds at end: nasal + /k/", color = Color(0xFF1565C0), words = listOf("bank", "sink", "honk", "bunk", "drink", "pink", "trunk", "blank", "think", "stunk")),
            RuleComparison(label = "NG  →  /ŋ/ only", condition = "One sound at end: nasal only", color = Color(0xFF757575), words = listOf("ring", "song", "sing", "lung", "bang", "long", "hang", "strong"))
        ),
        exampleGroups = listOf(
            RuleWordExampleGroup(label = null, examples = listOf(
                RuleWordExample(prefix = "ba",  suffix = "nk"),
                RuleWordExample(prefix = "si",  suffix = "nk"),
                RuleWordExample(prefix = "ho",  suffix = "nk"),
                RuleWordExample(prefix = "bu",  suffix = "nk"),
                RuleWordExample(prefix = "dri", suffix = "nk"),
                RuleWordExample(prefix = "pi",  suffix = "nk"),
                RuleWordExample(prefix = "tru", suffix = "nk"),
                RuleWordExample(prefix = "bla", suffix = "nk"),
                RuleWordExample(prefix = "thi", suffix = "nk"),
                RuleWordExample(prefix = "stu", suffix = "nk")
            ))
        )
    )
)

// ── UI State ──────────────────────────────────────────────────────────────────

data class ShortVowelRulesUiState(
    val selectedRule: SpellingRule? = null,
    val showExamples: Boolean = false,
    val highlightedExampleId: String? = null
)

// ── ViewModel ─────────────────────────────────────────────────────────────────

@HiltViewModel
class ShortVowelRulesViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    var uiState by mutableStateOf(ShortVowelRulesUiState()); private set

    fun onRuleTap(rule: SpellingRule) {
        audioManager.stop()
        uiState = ShortVowelRulesUiState(selectedRule = rule, showExamples = true)
    }

    fun onExampleTap(example: RuleWordExample) {
        audioManager.stop()
        uiState = uiState.copy(highlightedExampleId = example.id)
        // Set callback AFTER playPhonicsSound — it calls stop() internally which would clear
        // any callback set before the call.
        audioManager.playPhonicsSound("phonics_word/${example.word}")
        audioManager.onAudioCompleted = {
            uiState = uiState.copy(highlightedExampleId = null)
        }
    }

    fun stop() {
        audioManager.stop()
    }

    override fun onCleared() {
        super.onCleared()
        stop()
    }
}
