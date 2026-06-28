package com.example.myapplication.main.age_group.phonics.listen.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.utilities.AudioPhonicsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume

// ── Level Key ─────────────────────────────────────────────────────────────────

enum class PhonicsListenLevelKey {
    letterSounds, shortVowels, blending, cvcWords, shortVowelRules,
    wordFamilies, openSyllable, vowelTeams, beginningBlends, endingBlends, digraphs, specialEndings, magicE,
    diphthongs, rControlled
}

// ── Models ────────────────────────────────────────────────────────────────────

data class ListenSegment(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val indices: List<Int>
) {
    val audioFileName: String get() {
        val clean = text.replace("_", "")
        return if (clean.length == 1) "phonics_letter/sound_$clean" else "phonics_blend/$clean"
    }
}

data class ListenWord(
    val id: String = UUID.randomUUID().toString(),
    val word: String,
    val segments: List<ListenSegment>
)

data class PhonicsListenConfig(
    val title: String,
    val subtitle: String,
    val levelKey: PhonicsListenLevelKey,
    val accentColor: Color,
    val shadowColor: Color,
    val words: List<ListenWord>
)

// ── UI State ──────────────────────────────────────────────────────────────────

data class PhonicsListenUiState(
    val segmentIndex: Int = -1,
    val isAutoMode: Boolean = false,
    val isPlaying: Boolean = false,
    val wordDone: Boolean = false,
    val playedSegments: Set<Int> = emptySet(),
    val isGoingForward: Boolean = true
)

// ── Builder helpers ───────────────────────────────────────────────────────────

private fun s(text: String, indices: List<Int>) = ListenSegment(text = text, indices = indices)
private fun w(word: String, segs: List<ListenSegment>) = ListenWord(word = word, segments = segs)

// ── All Level Configs ─────────────────────────────────────────────────────────

val phonicsListenConfigs: Map<PhonicsListenLevelKey, PhonicsListenConfig> = mapOf(

    PhonicsListenLevelKey.letterSounds to PhonicsListenConfig(
        title = "Letter Sounds", subtitle = "Every letter has its own sound",
        levelKey = PhonicsListenLevelKey.letterSounds,
        accentColor = Color(0xFFE65100), shadowColor = Color(0xFFBF360C),
        words = listOf(
            w("ant", listOf(s("a", listOf(0)), s("n", listOf(1)), s("t", listOf(2)))),
            w("arm", listOf(s("a", listOf(0)), s("r", listOf(1)), s("m", listOf(2)))),
            w("bat", listOf(s("b", listOf(0)), s("a", listOf(1)), s("t", listOf(2)))),
            w("bed", listOf(s("b", listOf(0)), s("e", listOf(1)), s("d", listOf(2)))),
            w("big", listOf(s("b", listOf(0)), s("i", listOf(1)), s("g", listOf(2)))),
            w("cat", listOf(s("c", listOf(0)), s("a", listOf(1)), s("t", listOf(2)))),
            w("cup", listOf(s("c", listOf(0)), s("u", listOf(1)), s("p", listOf(2)))),
            w("dog", listOf(s("d", listOf(0)), s("o", listOf(1)), s("g", listOf(2)))),
            w("dip", listOf(s("d", listOf(0)), s("i", listOf(1)), s("p", listOf(2)))),
            w("egg", listOf(s("e", listOf(0)), s("g", listOf(1)), s("g", listOf(2)))),
            w("end", listOf(s("e", listOf(0)), s("n", listOf(1)), s("d", listOf(2)))),
            w("fan", listOf(s("f", listOf(0)), s("a", listOf(1)), s("n", listOf(2)))),
            w("fox", listOf(s("f", listOf(0)), s("o", listOf(1)), s("x", listOf(2)))),
            w("gas", listOf(s("g", listOf(0)), s("a", listOf(1)), s("s", listOf(2)))),
            w("got", listOf(s("g", listOf(0)), s("o", listOf(1)), s("t", listOf(2)))),
            w("hat", listOf(s("h", listOf(0)), s("a", listOf(1)), s("t", listOf(2)))),
            w("hop", listOf(s("h", listOf(0)), s("o", listOf(1)), s("p", listOf(2)))),
            w("ink", listOf(s("i", listOf(0)), s("n", listOf(1)), s("k", listOf(2)))),
            w("jam", listOf(s("j", listOf(0)), s("a", listOf(1)), s("m", listOf(2)))),
            w("jet", listOf(s("j", listOf(0)), s("e", listOf(1)), s("t", listOf(2)))),
            w("kit", listOf(s("k", listOf(0)), s("i", listOf(1)), s("t", listOf(2)))),
            w("lip", listOf(s("l", listOf(0)), s("i", listOf(1)), s("p", listOf(2)))),
            w("log", listOf(s("l", listOf(0)), s("o", listOf(1)), s("g", listOf(2)))),
            w("mat", listOf(s("m", listOf(0)), s("a", listOf(1)), s("t", listOf(2)))),
            w("mud", listOf(s("m", listOf(0)), s("u", listOf(1)), s("d", listOf(2)))),
            w("net", listOf(s("n", listOf(0)), s("e", listOf(1)), s("t", listOf(2)))),
            w("nap", listOf(s("n", listOf(0)), s("a", listOf(1)), s("p", listOf(2)))),
            w("ox", listOf(s("o", listOf(0)), s("x", listOf(1)))),
            w("pat", listOf(s("p", listOf(0)), s("a", listOf(1)), s("t", listOf(2)))),
            w("rat", listOf(s("r", listOf(0)), s("a", listOf(1)), s("t", listOf(2)))),
            w("red", listOf(s("r", listOf(0)), s("e", listOf(1)), s("d", listOf(2)))),
            w("sit", listOf(s("s", listOf(0)), s("i", listOf(1)), s("t", listOf(2)))),
            w("sun", listOf(s("s", listOf(0)), s("u", listOf(1)), s("n", listOf(2)))),
            w("top", listOf(s("t", listOf(0)), s("o", listOf(1)), s("p", listOf(2)))),
            w("tin", listOf(s("t", listOf(0)), s("i", listOf(1)), s("n", listOf(2)))),
            w("up",  listOf(s("u", listOf(0)), s("p", listOf(1)))),
            w("van", listOf(s("v", listOf(0)), s("a", listOf(1)), s("n", listOf(2)))),
            w("wet", listOf(s("w", listOf(0)), s("e", listOf(1)), s("t", listOf(2)))),
            w("win", listOf(s("w", listOf(0)), s("i", listOf(1)), s("n", listOf(2)))),
            w("fix", listOf(s("f", listOf(0)), s("i", listOf(1)), s("x", listOf(2)))),
            w("yam", listOf(s("y", listOf(0)), s("a", listOf(1)), s("m", listOf(2)))),
            w("yes", listOf(s("y", listOf(0)), s("e", listOf(1)), s("s", listOf(2)))),
            w("zip", listOf(s("z", listOf(0)), s("i", listOf(1)), s("p", listOf(2)))),
            w("zap", listOf(s("z", listOf(0)), s("a", listOf(1)), s("p", listOf(2))))
        )
    ),

    PhonicsListenLevelKey.shortVowels to PhonicsListenConfig(
        title = "Short Vowels", subtitle = "/a/ /e/ /i/ /o/ /u/",
        levelKey = PhonicsListenLevelKey.shortVowels,
        accentColor = Color(0xFF2E7D32), shadowColor = Color(0xFF1B5E20),
        words = listOf(
            w("cat", listOf(s("c", listOf(0)), s("a", listOf(1)), s("t", listOf(2)))),
            w("hat", listOf(s("h", listOf(0)), s("a", listOf(1)), s("t", listOf(2)))),
            w("pan", listOf(s("p", listOf(0)), s("a", listOf(1)), s("n", listOf(2)))),
            w("map", listOf(s("m", listOf(0)), s("a", listOf(1)), s("p", listOf(2)))),
            w("bag", listOf(s("b", listOf(0)), s("a", listOf(1)), s("g", listOf(2)))),
            w("ram", listOf(s("r", listOf(0)), s("a", listOf(1)), s("m", listOf(2)))),
            w("bed", listOf(s("b", listOf(0)), s("e", listOf(1)), s("d", listOf(2)))),
            w("red", listOf(s("r", listOf(0)), s("e", listOf(1)), s("d", listOf(2)))),
            w("hen", listOf(s("h", listOf(0)), s("e", listOf(1)), s("n", listOf(2)))),
            w("ten", listOf(s("t", listOf(0)), s("e", listOf(1)), s("n", listOf(2)))),
            w("leg", listOf(s("l", listOf(0)), s("e", listOf(1)), s("g", listOf(2)))),
            w("wet", listOf(s("w", listOf(0)), s("e", listOf(1)), s("t", listOf(2)))),
            w("big", listOf(s("b", listOf(0)), s("i", listOf(1)), s("g", listOf(2)))),
            w("sit", listOf(s("s", listOf(0)), s("i", listOf(1)), s("t", listOf(2)))),
            w("win", listOf(s("w", listOf(0)), s("i", listOf(1)), s("n", listOf(2)))),
            w("pig", listOf(s("p", listOf(0)), s("i", listOf(1)), s("g", listOf(2)))),
            w("hip", listOf(s("h", listOf(0)), s("i", listOf(1)), s("p", listOf(2)))),
            w("hot", listOf(s("h", listOf(0)), s("o", listOf(1)), s("t", listOf(2)))),
            w("pot", listOf(s("p", listOf(0)), s("o", listOf(1)), s("t", listOf(2)))),
            w("log", listOf(s("l", listOf(0)), s("o", listOf(1)), s("g", listOf(2)))),
            w("fox", listOf(s("f", listOf(0)), s("o", listOf(1)), s("x", listOf(2)))),
            w("dot", listOf(s("d", listOf(0)), s("o", listOf(1)), s("t", listOf(2)))),
            w("bug", listOf(s("b", listOf(0)), s("u", listOf(1)), s("g", listOf(2)))),
            w("cup", listOf(s("c", listOf(0)), s("u", listOf(1)), s("p", listOf(2)))),
            w("sun", listOf(s("s", listOf(0)), s("u", listOf(1)), s("n", listOf(2)))),
            w("run", listOf(s("r", listOf(0)), s("u", listOf(1)), s("n", listOf(2)))),
            w("mud", listOf(s("m", listOf(0)), s("u", listOf(1)), s("d", listOf(2)))),
            w("jug", listOf(s("j", listOf(0)), s("u", listOf(1)), s("g", listOf(2))))
        )
    ),

    PhonicsListenLevelKey.blending to PhonicsListenConfig(
        title = "2-Sound Blending", subtitle = "VC & CV blends",
        levelKey = PhonicsListenLevelKey.blending,
        accentColor = Color(0xFF1565C0), shadowColor = Color(0xFF0D47A1),
        words = listOf(
            w("at", listOf(s("a", listOf(0)), s("t", listOf(1)))),
            w("an", listOf(s("a", listOf(0)), s("n", listOf(1)))),
            w("am", listOf(s("a", listOf(0)), s("m", listOf(1)))),
            w("in", listOf(s("i", listOf(0)), s("n", listOf(1)))),
            w("it", listOf(s("i", listOf(0)), s("t", listOf(1)))),
            w("on", listOf(s("o", listOf(0)), s("n", listOf(1)))),
            w("up", listOf(s("u", listOf(0)), s("p", listOf(1)))),
            w("us", listOf(s("u", listOf(0)), s("s", listOf(1)))),
            w("ba", listOf(s("b", listOf(0)), s("a", listOf(1)))),
            w("ma", listOf(s("m", listOf(0)), s("a", listOf(1)))),
            w("go", listOf(s("g", listOf(0)), s("o", listOf(1)))),
            w("no", listOf(s("n", listOf(0)), s("o", listOf(1)))),
            w("me", listOf(s("m", listOf(0)), s("e", listOf(1)))),
            w("be", listOf(s("b", listOf(0)), s("e", listOf(1)))),
            w("he", listOf(s("h", listOf(0)), s("e", listOf(1)))),
            w("we", listOf(s("w", listOf(0)), s("e", listOf(1))))
        )
    ),

    PhonicsListenLevelKey.cvcWords to PhonicsListenConfig(
        title = "CVC Words", subtitle = "Consonant · Vowel · Consonant",
        levelKey = PhonicsListenLevelKey.cvcWords,
        accentColor = Color(0xFF6A1B9A), shadowColor = Color(0xFF4A148C),
        words = listOf(
            w("cat", listOf(s("c", listOf(0)), s("a", listOf(1)), s("t", listOf(2)))),
            w("hat", listOf(s("h", listOf(0)), s("a", listOf(1)), s("t", listOf(2)))),
            w("pan", listOf(s("p", listOf(0)), s("a", listOf(1)), s("n", listOf(2)))),
            w("map", listOf(s("m", listOf(0)), s("a", listOf(1)), s("p", listOf(2)))),
            w("bag", listOf(s("b", listOf(0)), s("a", listOf(1)), s("g", listOf(2)))),
            w("bed", listOf(s("b", listOf(0)), s("e", listOf(1)), s("d", listOf(2)))),
            w("red", listOf(s("r", listOf(0)), s("e", listOf(1)), s("d", listOf(2)))),
            w("hen", listOf(s("h", listOf(0)), s("e", listOf(1)), s("n", listOf(2)))),
            w("ten", listOf(s("t", listOf(0)), s("e", listOf(1)), s("n", listOf(2)))),
            w("wet", listOf(s("w", listOf(0)), s("e", listOf(1)), s("t", listOf(2)))),
            w("big", listOf(s("b", listOf(0)), s("i", listOf(1)), s("g", listOf(2)))),
            w("sit", listOf(s("s", listOf(0)), s("i", listOf(1)), s("t", listOf(2)))),
            w("win", listOf(s("w", listOf(0)), s("i", listOf(1)), s("n", listOf(2)))),
            w("pig", listOf(s("p", listOf(0)), s("i", listOf(1)), s("g", listOf(2)))),
            w("hip", listOf(s("h", listOf(0)), s("i", listOf(1)), s("p", listOf(2)))),
            w("hot", listOf(s("h", listOf(0)), s("o", listOf(1)), s("t", listOf(2)))),
            w("pot", listOf(s("p", listOf(0)), s("o", listOf(1)), s("t", listOf(2)))),
            w("log", listOf(s("l", listOf(0)), s("o", listOf(1)), s("g", listOf(2)))),
            w("fox", listOf(s("f", listOf(0)), s("o", listOf(1)), s("x", listOf(2)))),
            w("dot", listOf(s("d", listOf(0)), s("o", listOf(1)), s("t", listOf(2)))),
            w("bug", listOf(s("b", listOf(0)), s("u", listOf(1)), s("g", listOf(2)))),
            w("cup", listOf(s("c", listOf(0)), s("u", listOf(1)), s("p", listOf(2)))),
            w("sun", listOf(s("s", listOf(0)), s("u", listOf(1)), s("n", listOf(2)))),
            w("run", listOf(s("r", listOf(0)), s("u", listOf(1)), s("n", listOf(2)))),
            w("mud", listOf(s("m", listOf(0)), s("u", listOf(1)), s("d", listOf(2)))),
            w("jug", listOf(s("j", listOf(0)), s("u", listOf(1)), s("g", listOf(2))))
        )
    ),

    PhonicsListenLevelKey.shortVowelRules to PhonicsListenConfig(
        title = "Spelling Rules", subtitle = "-ff -ll -ss -zz -ck -ng -nk",
        levelKey = PhonicsListenLevelKey.shortVowelRules,
        accentColor = Color(0xFFC62828), shadowColor = Color(0xFFB71C1C),
        words = listOf(
            w("off",  listOf(s("o", listOf(0)), s("ff", listOf(1,2)))),
            w("bell", listOf(s("b", listOf(0)), s("e", listOf(1)), s("ll", listOf(2,3)))),
            w("miss", listOf(s("m", listOf(0)), s("i", listOf(1)), s("ss", listOf(2,3)))),
            w("buzz", listOf(s("b", listOf(0)), s("u", listOf(1)), s("zz", listOf(2,3)))),
            w("back", listOf(s("b", listOf(0)), s("a", listOf(1)), s("ck", listOf(2,3)))),
            w("kick", listOf(s("k", listOf(0)), s("i", listOf(1)), s("ck", listOf(2,3)))),
            w("lock", listOf(s("l", listOf(0)), s("o", listOf(1)), s("ck", listOf(2,3)))),
            w("duck", listOf(s("d", listOf(0)), s("u", listOf(1)), s("ck", listOf(2,3)))),
            w("ring", listOf(s("r", listOf(0)), s("i", listOf(1)), s("ng", listOf(2,3)))),
            w("song", listOf(s("s", listOf(0)), s("o", listOf(1)), s("ng", listOf(2,3)))),
            w("king", listOf(s("k", listOf(0)), s("i", listOf(1)), s("ng", listOf(2,3)))),
            w("sink", listOf(s("s", listOf(0)), s("i", listOf(1)), s("nk", listOf(2,3)))),
            w("tank", listOf(s("t", listOf(0)), s("a", listOf(1)), s("nk", listOf(2,3)))),
            w("pink", listOf(s("p", listOf(0)), s("i", listOf(1)), s("nk", listOf(2,3))))
        )
    ),

    PhonicsListenLevelKey.wordFamilies to PhonicsListenConfig(
        title = "Word Families", subtitle = "-at -an -ap -en -et -ig -op -ot -ug -un",
        levelKey = PhonicsListenLevelKey.wordFamilies,
        accentColor = Color(0xFFC2185B), shadowColor = Color(0xFF880E4F),
        words = listOf(
            w("bat", listOf(s("b", listOf(0)), s("at", listOf(1,2)))),
            w("cat", listOf(s("c", listOf(0)), s("at", listOf(1,2)))),
            w("hat", listOf(s("h", listOf(0)), s("at", listOf(1,2)))),
            w("mat", listOf(s("m", listOf(0)), s("at", listOf(1,2)))),
            w("ban", listOf(s("b", listOf(0)), s("an", listOf(1,2)))),
            w("can", listOf(s("c", listOf(0)), s("an", listOf(1,2)))),
            w("fan", listOf(s("f", listOf(0)), s("an", listOf(1,2)))),
            w("cap", listOf(s("c", listOf(0)), s("ap", listOf(1,2)))),
            w("lap", listOf(s("l", listOf(0)), s("ap", listOf(1,2)))),
            w("map", listOf(s("m", listOf(0)), s("ap", listOf(1,2)))),
            w("den", listOf(s("d", listOf(0)), s("en", listOf(1,2)))),
            w("hen", listOf(s("h", listOf(0)), s("en", listOf(1,2)))),
            w("ten", listOf(s("t", listOf(0)), s("en", listOf(1,2)))),
            w("bet", listOf(s("b", listOf(0)), s("et", listOf(1,2)))),
            w("jet", listOf(s("j", listOf(0)), s("et", listOf(1,2)))),
            w("big", listOf(s("b", listOf(0)), s("ig", listOf(1,2)))),
            w("dig", listOf(s("d", listOf(0)), s("ig", listOf(1,2)))),
            w("pig", listOf(s("p", listOf(0)), s("ig", listOf(1,2)))),
            w("hop", listOf(s("h", listOf(0)), s("op", listOf(1,2)))),
            w("mop", listOf(s("m", listOf(0)), s("op", listOf(1,2)))),
            w("hot", listOf(s("h", listOf(0)), s("ot", listOf(1,2)))),
            w("pot", listOf(s("p", listOf(0)), s("ot", listOf(1,2)))),
            w("bug", listOf(s("b", listOf(0)), s("ug", listOf(1,2)))),
            w("hug", listOf(s("h", listOf(0)), s("ug", listOf(1,2)))),
            w("rug", listOf(s("r", listOf(0)), s("ug", listOf(1,2)))),
            w("bun", listOf(s("b", listOf(0)), s("un", listOf(1,2)))),
            w("fun", listOf(s("f", listOf(0)), s("un", listOf(1,2)))),
            w("run", listOf(s("r", listOf(0)), s("un", listOf(1,2))))
        )
    ),

    PhonicsListenLevelKey.openSyllable to PhonicsListenConfig(
        title = "Open Syllable", subtitle = "Vowel at the end says its long name",
        levelKey = PhonicsListenLevelKey.openSyllable,
        accentColor = Color(0xFF6A1B9A), shadowColor = Color(0xFF4A148C),
        words = listOf(
            w("me",  listOf(s("m", listOf(0)), s("e", listOf(1)))),
            w("he",  listOf(s("h", listOf(0)), s("e", listOf(1)))),
            w("she", listOf(s("sh", listOf(0,1)), s("e", listOf(2)))),
            w("be",  listOf(s("b", listOf(0)), s("e", listOf(1)))),
            w("we",  listOf(s("w", listOf(0)), s("e", listOf(1)))),
            w("go",  listOf(s("g", listOf(0)), s("o", listOf(1)))),
            w("no",  listOf(s("n", listOf(0)), s("o", listOf(1)))),
            w("so",  listOf(s("s", listOf(0)), s("o", listOf(1)))),
            w("do",  listOf(s("d", listOf(0)), s("o", listOf(1)))),
            w("hi",  listOf(s("h", listOf(0)), s("i", listOf(1)))),
            w("by",  listOf(s("b", listOf(0)), s("y", listOf(1)))),
            w("my",  listOf(s("m", listOf(0)), s("y", listOf(1)))),
            w("fly", listOf(s("fl", listOf(0,1)), s("y", listOf(2)))),
            w("shy", listOf(s("sh", listOf(0,1)), s("y", listOf(2)))),
            w("sky", listOf(s("sk", listOf(0,1)), s("y", listOf(2)))),
            w("cry", listOf(s("cr", listOf(0,1)), s("y", listOf(2)))),
            w("dry", listOf(s("dr", listOf(0,1)), s("y", listOf(2)))),
            w("pro", listOf(s("pr", listOf(0,1)), s("o", listOf(2))))
        )
    ),

    PhonicsListenLevelKey.vowelTeams to PhonicsListenConfig(
        title = "Vowel Teams", subtitle = "Two vowels together — one long sound",
        levelKey = PhonicsListenLevelKey.vowelTeams,
        accentColor = Color(0xFFEF6C00), shadowColor = Color(0xFFE65100),
        words = listOf(
            w("rain",  listOf(s("r", listOf(0)), s("ai", listOf(1,2)), s("n", listOf(3)))),
            w("tail",  listOf(s("t", listOf(0)), s("ai", listOf(1,2)), s("l", listOf(3)))),
            w("day",   listOf(s("d", listOf(0)), s("ay", listOf(1,2)))),
            w("play",  listOf(s("pl", listOf(0,1)), s("ay", listOf(2,3)))),
            w("feet",  listOf(s("f", listOf(0)), s("ee", listOf(1,2)), s("t", listOf(3)))),
            w("tree",  listOf(s("tr", listOf(0,1)), s("ee", listOf(2,3)))),
            w("read",  listOf(s("r", listOf(0)), s("ea", listOf(1,2)), s("d", listOf(3)))),
            w("team",  listOf(s("t", listOf(0)), s("ea", listOf(1,2)), s("m", listOf(3)))),
            w("boat",  listOf(s("b", listOf(0)), s("oa", listOf(1,2)), s("t", listOf(3)))),
            w("coat",  listOf(s("c", listOf(0)), s("oa", listOf(1,2)), s("t", listOf(3)))),
            w("snow",  listOf(s("sn", listOf(0,1)), s("ow", listOf(2,3)))),
            w("grow",  listOf(s("gr", listOf(0,1)), s("ow", listOf(2,3)))),
            w("wait",  listOf(s("w", listOf(0)), s("ai", listOf(1,2)), s("t", listOf(3)))),
            w("beach", listOf(s("b", listOf(0)), s("ea", listOf(1,2)), s("ch", listOf(3,4)))),
            w("road",  listOf(s("r", listOf(0)), s("oa", listOf(1,2)), s("d", listOf(3)))),
            w("say",   listOf(s("s", listOf(0)), s("ay", listOf(1,2)))),
            w("green", listOf(s("gr", listOf(0,1)), s("ee", listOf(2,3)), s("n", listOf(4)))),
            w("goat",  listOf(s("g", listOf(0)), s("oa", listOf(1,2)), s("t", listOf(3))))
        )
    ),

    PhonicsListenLevelKey.beginningBlends to PhonicsListenConfig(
        title = "Beginning Blends", subtitle = "L-blends · R-blends · S-blends",
        levelKey = PhonicsListenLevelKey.beginningBlends,
        accentColor = Color(0xFF00897B), shadowColor = Color(0xFF00695C),
        words = listOf(
            w("blue",  listOf(s("bl", listOf(0,1)), s("ue", listOf(2,3)))),
            w("black", listOf(s("bl", listOf(0,1)), s("a", listOf(2)), s("ck", listOf(3,4)))),
            w("blink", listOf(s("bl", listOf(0,1)), s("i", listOf(2)), s("nk", listOf(3,4)))),
            w("clap",  listOf(s("cl", listOf(0,1)), s("a", listOf(2)), s("p", listOf(3)))),
            w("clock", listOf(s("cl", listOf(0,1)), s("o", listOf(2)), s("ck", listOf(3,4)))),
            w("clip",  listOf(s("cl", listOf(0,1)), s("i", listOf(2)), s("p", listOf(3)))),
            w("flag",  listOf(s("fl", listOf(0,1)), s("a", listOf(2)), s("g", listOf(3)))),
            w("flip",  listOf(s("fl", listOf(0,1)), s("i", listOf(2)), s("p", listOf(3)))),
            w("flat",  listOf(s("fl", listOf(0,1)), s("a", listOf(2)), s("t", listOf(3)))),
            w("glad",  listOf(s("gl", listOf(0,1)), s("a", listOf(2)), s("d", listOf(3)))),
            w("glow",  listOf(s("gl", listOf(0,1)), s("ow", listOf(2,3)))),
            w("plan",  listOf(s("pl", listOf(0,1)), s("a", listOf(2)), s("n", listOf(3)))),
            w("plug",  listOf(s("pl", listOf(0,1)), s("u", listOf(2)), s("g", listOf(3)))),
            w("sled",  listOf(s("sl", listOf(0,1)), s("e", listOf(2)), s("d", listOf(3)))),
            w("slim",  listOf(s("sl", listOf(0,1)), s("i", listOf(2)), s("m", listOf(3)))),
            w("brag",  listOf(s("br", listOf(0,1)), s("a", listOf(2)), s("g", listOf(3)))),
            w("brim",  listOf(s("br", listOf(0,1)), s("i", listOf(2)), s("m", listOf(3)))),
            w("crab",  listOf(s("cr", listOf(0,1)), s("a", listOf(2)), s("b", listOf(3)))),
            w("crop",  listOf(s("cr", listOf(0,1)), s("o", listOf(2)), s("p", listOf(3)))),
            w("drum",  listOf(s("dr", listOf(0,1)), s("u", listOf(2)), s("m", listOf(3)))),
            w("drip",  listOf(s("dr", listOf(0,1)), s("i", listOf(2)), s("p", listOf(3)))),
            w("frog",  listOf(s("fr", listOf(0,1)), s("o", listOf(2)), s("g", listOf(3)))),
            w("from",  listOf(s("fr", listOf(0,1)), s("o", listOf(2)), s("m", listOf(3)))),
            w("grab",  listOf(s("gr", listOf(0,1)), s("a", listOf(2)), s("b", listOf(3)))),
            w("grin",  listOf(s("gr", listOf(0,1)), s("i", listOf(2)), s("n", listOf(3)))),
            w("prop",  listOf(s("pr", listOf(0,1)), s("o", listOf(2)), s("p", listOf(3)))),
            w("tree",  listOf(s("tr", listOf(0,1)), s("ee", listOf(2,3)))),
            w("trip",  listOf(s("tr", listOf(0,1)), s("i", listOf(2)), s("p", listOf(3)))),
            w("scan",  listOf(s("sc", listOf(0,1)), s("a", listOf(2)), s("n", listOf(3)))),
            w("skip",  listOf(s("sk", listOf(0,1)), s("i", listOf(2)), s("p", listOf(3)))),
            w("skin",  listOf(s("sk", listOf(0,1)), s("i", listOf(2)), s("n", listOf(3)))),
            w("smash", listOf(s("sm", listOf(0,1)), s("a", listOf(2)), s("sh", listOf(3,4)))),
            w("snap",  listOf(s("sn", listOf(0,1)), s("a", listOf(2)), s("p", listOf(3)))),
            w("spin",  listOf(s("sp", listOf(0,1)), s("i", listOf(2)), s("n", listOf(3)))),
            w("spot",  listOf(s("sp", listOf(0,1)), s("o", listOf(2)), s("t", listOf(3)))),
            w("star",  listOf(s("st", listOf(0,1)), s("ar", listOf(2,3)))),
            w("stop",  listOf(s("st", listOf(0,1)), s("o", listOf(2)), s("p", listOf(3)))),
            w("swim",  listOf(s("sw", listOf(0,1)), s("i", listOf(2)), s("m", listOf(3)))),
            w("swam",  listOf(s("sw", listOf(0,1)), s("a", listOf(2)), s("m", listOf(3))))
        )
    ),

    PhonicsListenLevelKey.endingBlends to PhonicsListenConfig(
        title = "Ending Blends", subtitle = "Level 8 · Ending Blends",
        levelKey = PhonicsListenLevelKey.endingBlends,
        accentColor = Color(0xFF3949AB), shadowColor = Color(0xFF1A237E),
        words = listOf(
            w("hand",  listOf(s("h", listOf(0)), s("a", listOf(1)), s("nd", listOf(2,3)))),
            w("sand",  listOf(s("s", listOf(0)), s("a", listOf(1)), s("nd", listOf(2,3)))),
            w("band",  listOf(s("b", listOf(0)), s("a", listOf(1)), s("nd", listOf(2,3)))),
            w("wind",  listOf(s("w", listOf(0)), s("i", listOf(1)), s("nd", listOf(2,3)))),
            w("tent",  listOf(s("t", listOf(0)), s("e", listOf(1)), s("nt", listOf(2,3)))),
            w("mint",  listOf(s("m", listOf(0)), s("i", listOf(1)), s("nt", listOf(2,3)))),
            w("hunt",  listOf(s("h", listOf(0)), s("u", listOf(1)), s("nt", listOf(2,3)))),
            w("lamp",  listOf(s("l", listOf(0)), s("a", listOf(1)), s("mp", listOf(2,3)))),
            w("jump",  listOf(s("j", listOf(0)), s("u", listOf(1)), s("mp", listOf(2,3)))),
            w("bump",  listOf(s("b", listOf(0)), s("u", listOf(1)), s("mp", listOf(2,3)))),
            w("milk",  listOf(s("m", listOf(0)), s("i", listOf(1)), s("lk", listOf(2,3)))),
            w("silk",  listOf(s("s", listOf(0)), s("i", listOf(1)), s("lk", listOf(2,3)))),
            w("belt",  listOf(s("b", listOf(0)), s("e", listOf(1)), s("lt", listOf(2,3)))),
            w("melt",  listOf(s("m", listOf(0)), s("e", listOf(1)), s("lt", listOf(2,3)))),
            w("bolt",  listOf(s("b", listOf(0)), s("o", listOf(1)), s("lt", listOf(2,3)))),
            w("desk",  listOf(s("d", listOf(0)), s("e", listOf(1)), s("sk", listOf(2,3)))),
            w("task",  listOf(s("t", listOf(0)), s("a", listOf(1)), s("sk", listOf(2,3)))),
            w("left",  listOf(s("l", listOf(0)), s("e", listOf(1)), s("ft", listOf(2,3)))),
            w("gift",  listOf(s("g", listOf(0)), s("i", listOf(1)), s("ft", listOf(2,3)))),
            w("lift",  listOf(s("l", listOf(0)), s("i", listOf(1)), s("ft", listOf(2,3)))),
            w("best",  listOf(s("b", listOf(0)), s("e", listOf(1)), s("st", listOf(2,3)))),
            w("nest",  listOf(s("n", listOf(0)), s("e", listOf(1)), s("st", listOf(2,3)))),
            w("fast",  listOf(s("f", listOf(0)), s("a", listOf(1)), s("st", listOf(2,3)))),
            w("dust",  listOf(s("d", listOf(0)), s("u", listOf(1)), s("st", listOf(2,3))))
        )
    ),

    PhonicsListenLevelKey.digraphs to PhonicsListenConfig(
        title = "Digraphs", subtitle = "ch · sh · th · wh · ph · qu",
        levelKey = PhonicsListenLevelKey.digraphs,
        accentColor = Color(0xFFE65100), shadowColor = Color(0xFFBF360C),
        words = listOf(
            w("chip",    listOf(s("ch", listOf(0,1)), s("i", listOf(2)), s("p", listOf(3)))),
            w("chop",    listOf(s("ch", listOf(0,1)), s("o", listOf(2)), s("p", listOf(3)))),
            w("chat",    listOf(s("ch", listOf(0,1)), s("a", listOf(2)), s("t", listOf(3)))),
            w("chin",    listOf(s("ch", listOf(0,1)), s("i", listOf(2)), s("n", listOf(3)))),
            w("check",   listOf(s("ch", listOf(0,1)), s("e", listOf(2)), s("ck", listOf(3,4)))),
            w("chest",   listOf(s("ch", listOf(0,1)), s("e", listOf(2)), s("st", listOf(3,4)))),
            w("ship",    listOf(s("sh", listOf(0,1)), s("i", listOf(2)), s("p", listOf(3)))),
            w("shop",    listOf(s("sh", listOf(0,1)), s("o", listOf(2)), s("p", listOf(3)))),
            w("shed",    listOf(s("sh", listOf(0,1)), s("e", listOf(2)), s("d", listOf(3)))),
            w("shell",   listOf(s("sh", listOf(0,1)), s("e", listOf(2)), s("ll", listOf(3,4)))),
            w("shark",   listOf(s("sh", listOf(0,1)), s("ar", listOf(2,3)), s("k", listOf(4)))),
            w("thin",    listOf(s("th", listOf(0,1)), s("i", listOf(2)), s("n", listOf(3)))),
            w("that",    listOf(s("th", listOf(0,1)), s("a", listOf(2)), s("t", listOf(3)))),
            w("them",    listOf(s("th", listOf(0,1)), s("e", listOf(2)), s("m", listOf(3)))),
            w("thick",   listOf(s("th", listOf(0,1)), s("i", listOf(2)), s("ck", listOf(3,4)))),
            w("three",   listOf(s("th", listOf(0,1)), s("r", listOf(2)), s("ee", listOf(3,4)))),
            w("whip",    listOf(s("wh", listOf(0,1)), s("i", listOf(2)), s("p", listOf(3)))),
            w("when",    listOf(s("wh", listOf(0,1)), s("e", listOf(2)), s("n", listOf(3)))),
            w("what",    listOf(s("wh", listOf(0,1)), s("a", listOf(2)), s("t", listOf(3)))),
            w("whale",   listOf(s("wh", listOf(0,1)), s("a", listOf(2)), s("le", listOf(3,4)))),
            w("phone",   listOf(s("ph", listOf(0,1)), s("o", listOf(2)), s("ne", listOf(3,4)))),
            w("photo",   listOf(s("ph", listOf(0,1)), s("o", listOf(2)), s("to", listOf(3,4)))),
            w("queen",   listOf(s("qu", listOf(0,1)), s("ee", listOf(2,3)), s("n", listOf(4)))),
            w("quiz",    listOf(s("qu", listOf(0,1)), s("i", listOf(2)), s("z", listOf(3)))),
            w("quick",   listOf(s("qu", listOf(0,1)), s("i", listOf(2)), s("ck", listOf(3,4))))
        )
    ),

    PhonicsListenLevelKey.specialEndings to PhonicsListenConfig(
        title = "Special Endings", subtitle = "-tch · -dge · -nk",
        levelKey = PhonicsListenLevelKey.specialEndings,
        accentColor = Color(0xFF558B2F), shadowColor = Color(0xFF33691E),
        words = listOf(
            w("catch",  listOf(s("c", listOf(0)), s("a", listOf(1)), s("tch", listOf(2,3,4)))),
            w("match",  listOf(s("m", listOf(0)), s("a", listOf(1)), s("tch", listOf(2,3,4)))),
            w("patch",  listOf(s("p", listOf(0)), s("a", listOf(1)), s("tch", listOf(2,3,4)))),
            w("fetch",  listOf(s("f", listOf(0)), s("e", listOf(1)), s("tch", listOf(2,3,4)))),
            w("witch",  listOf(s("w", listOf(0)), s("i", listOf(1)), s("tch", listOf(2,3,4)))),
            w("ditch",  listOf(s("d", listOf(0)), s("i", listOf(1)), s("tch", listOf(2,3,4)))),
            w("notch",  listOf(s("n", listOf(0)), s("o", listOf(1)), s("tch", listOf(2,3,4)))),
            w("watch",  listOf(s("w", listOf(0)), s("a", listOf(1)), s("tch", listOf(2,3,4)))),
            w("badge",  listOf(s("b", listOf(0)), s("a", listOf(1)), s("dge", listOf(2,3,4)))),
            w("edge",   listOf(s("e", listOf(0)), s("dge", listOf(1,2,3)))),
            w("hedge",  listOf(s("h", listOf(0)), s("e", listOf(1)), s("dge", listOf(2,3,4)))),
            w("bridge", listOf(s("br", listOf(0,1)), s("i", listOf(2)), s("dge", listOf(3,4,5)))),
            w("judge",  listOf(s("j", listOf(0)), s("u", listOf(1)), s("dge", listOf(2,3,4)))),
            w("fudge",  listOf(s("f", listOf(0)), s("u", listOf(1)), s("dge", listOf(2,3,4)))),
            w("sink",   listOf(s("s", listOf(0)), s("i", listOf(1)), s("nk", listOf(2,3)))),
            w("pink",   listOf(s("p", listOf(0)), s("i", listOf(1)), s("nk", listOf(2,3)))),
            w("bank",   listOf(s("b", listOf(0)), s("a", listOf(1)), s("nk", listOf(2,3)))),
            w("tank",   listOf(s("t", listOf(0)), s("a", listOf(1)), s("nk", listOf(2,3)))),
            w("honk",   listOf(s("h", listOf(0)), s("o", listOf(1)), s("nk", listOf(2,3)))),
            w("bunk",   listOf(s("b", listOf(0)), s("u", listOf(1)), s("nk", listOf(2,3))))
        )
    ),

    PhonicsListenLevelKey.magicE to PhonicsListenConfig(
        title = "Magic E", subtitle = "a-e · i-e · o-e · u-e",
        levelKey = PhonicsListenLevelKey.magicE,
        accentColor = Color(0xFF880E4F), shadowColor = Color(0xFF4A148C),
        words = listOf(
            // a-e
            w("cape",  listOf(s("c", listOf(0)),   s("a_e", listOf(1,3)), s("p", listOf(2)))),
            w("hate",  listOf(s("h", listOf(0)),   s("a_e", listOf(1,3)), s("t", listOf(2)))),
            w("made",  listOf(s("m", listOf(0)),   s("a_e", listOf(1,3)), s("d", listOf(2)))),
            w("tape",  listOf(s("t", listOf(0)),   s("a_e", listOf(1,3)), s("p", listOf(2)))),
            w("mane",  listOf(s("m", listOf(0)),   s("a_e", listOf(1,3)), s("n", listOf(2)))),
            w("cane",  listOf(s("c", listOf(0)),   s("a_e", listOf(1,3)), s("n", listOf(2)))),
            // i-e
            w("bite",  listOf(s("b", listOf(0)),   s("i_e", listOf(1,3)), s("t", listOf(2)))),
            w("hide",  listOf(s("h", listOf(0)),   s("i_e", listOf(1,3)), s("d", listOf(2)))),
            w("dime",  listOf(s("d", listOf(0)),   s("i_e", listOf(1,3)), s("m", listOf(2)))),
            w("pine",  listOf(s("p", listOf(0)),   s("i_e", listOf(1,3)), s("n", listOf(2)))),
            w("kite",  listOf(s("k", listOf(0)),   s("i_e", listOf(1,3)), s("t", listOf(2)))),
            w("fine",  listOf(s("f", listOf(0)),   s("i_e", listOf(1,3)), s("n", listOf(2)))),
            // o-e
            w("hope",  listOf(s("h", listOf(0)),   s("o_e", listOf(1,3)), s("p", listOf(2)))),
            w("note",  listOf(s("n", listOf(0)),   s("o_e", listOf(1,3)), s("t", listOf(2)))),
            w("code",  listOf(s("c", listOf(0)),   s("o_e", listOf(1,3)), s("d", listOf(2)))),
            w("robe",  listOf(s("r", listOf(0)),   s("o_e", listOf(1,3)), s("b", listOf(2)))),
            w("cone",  listOf(s("c", listOf(0)),   s("o_e", listOf(1,3)), s("n", listOf(2)))),
            w("tone",  listOf(s("t", listOf(0)),   s("o_e", listOf(1,3)), s("n", listOf(2)))),
            // u-e
            w("cube",  listOf(s("c", listOf(0)),   s("u_e", listOf(1,3)), s("b", listOf(2)))),
            w("cute",  listOf(s("c", listOf(0)),   s("u_e", listOf(1,3)), s("t", listOf(2)))),
            w("tube",  listOf(s("t", listOf(0)),   s("u_e", listOf(1,3)), s("b", listOf(2)))),
            w("dune",  listOf(s("d", listOf(0)),   s("u_e", listOf(1,3)), s("n", listOf(2)))),
            w("tune",  listOf(s("t", listOf(0)),   s("u_e", listOf(1,3)), s("n", listOf(2))))
        )
    ),

    PhonicsListenLevelKey.diphthongs to PhonicsListenConfig(
        title = "Diphthongs", subtitle = "oi/oy · ou/ow · au/aw",
        levelKey = PhonicsListenLevelKey.diphthongs,
        accentColor = Color(0xFFE65100), shadowColor = Color(0xFFBF360C),
        words = listOf(
            // /ɔɪ/ — oi
            w("coin",  listOf(s("c",  listOf(0)),    s("oi", listOf(1,2)), s("n",  listOf(3)))),
            w("oil",   listOf(s("oi", listOf(0,1)),  s("l",  listOf(2)))),
            w("foil",  listOf(s("f",  listOf(0)),    s("oi", listOf(1,2)), s("l",  listOf(3)))),
            w("join",  listOf(s("j",  listOf(0)),    s("oi", listOf(1,2)), s("n",  listOf(3)))),
            w("soil",  listOf(s("s",  listOf(0)),    s("oi", listOf(1,2)), s("l",  listOf(3)))),
            // /ɔɪ/ — oy
            w("toy",   listOf(s("t",  listOf(0)),    s("oy", listOf(1,2)))),
            w("boy",   listOf(s("b",  listOf(0)),    s("oy", listOf(1,2)))),
            w("joy",   listOf(s("j",  listOf(0)),    s("oy", listOf(1,2)))),
            w("soy",   listOf(s("s",  listOf(0)),    s("oy", listOf(1,2)))),
            // /aʊ/ — ou
            w("out",   listOf(s("ou", listOf(0,1)),  s("t",  listOf(2)))),
            w("loud",  listOf(s("l",  listOf(0)),    s("ou", listOf(1,2)), s("d",  listOf(3)))),
            w("cloud", listOf(s("cl", listOf(0,1)),  s("ou", listOf(2,3)), s("d",  listOf(4)))),
            w("found", listOf(s("f",  listOf(0)),    s("ou", listOf(1,2)), s("nd", listOf(3,4)))),
            w("mouth", listOf(s("m",  listOf(0)),    s("ou", listOf(1,2)), s("th", listOf(3,4)))),
            // /aʊ/ — ow
            w("cow",   listOf(s("c",  listOf(0)),    s("ow", listOf(1,2)))),
            w("now",   listOf(s("n",  listOf(0)),    s("ow", listOf(1,2)))),
            w("down",  listOf(s("d",  listOf(0)),    s("ow", listOf(1,2)), s("n",  listOf(3)))),
            w("town",  listOf(s("t",  listOf(0)),    s("ow", listOf(1,2)), s("n",  listOf(3)))),
            w("brown", listOf(s("br", listOf(0,1)),  s("ow", listOf(2,3)), s("n",  listOf(4)))),
            // /ɔː/ — au
            w("haul",  listOf(s("h",  listOf(0)),    s("au", listOf(1,2)), s("l",  listOf(3)))),
            w("cause", listOf(s("c",  listOf(0)),    s("au", listOf(1,2)), s("se", listOf(3,4)))),
            w("pause", listOf(s("p",  listOf(0)),    s("au", listOf(1,2)), s("se", listOf(3,4)))),
            // /ɔː/ — aw
            w("saw",   listOf(s("s",  listOf(0)),    s("aw", listOf(1,2)))),
            w("paw",   listOf(s("p",  listOf(0)),    s("aw", listOf(1,2)))),
            w("jaw",   listOf(s("j",  listOf(0)),    s("aw", listOf(1,2)))),
            w("draw",  listOf(s("dr", listOf(0,1)),  s("aw", listOf(2,3)))),
            w("yawn",  listOf(s("y",  listOf(0)),    s("aw", listOf(1,2)), s("n",  listOf(3))))
        )
    ),

    PhonicsListenLevelKey.rControlled to PhonicsListenConfig(
        title = "R-Controlled Vowels", subtitle = "ar · or · er · ir · ur",
        levelKey = PhonicsListenLevelKey.rControlled,
        accentColor = Color(0xFF2E7D32), shadowColor = Color(0xFF1B5E20),
        words = listOf(
            // ar
            w("car",   listOf(s("c",  listOf(0)),    s("ar", listOf(1,2)))),
            w("star",  listOf(s("st", listOf(0,1)),  s("ar", listOf(2,3)))),
            w("farm",  listOf(s("f",  listOf(0)),    s("ar", listOf(1,2)), s("m",  listOf(3)))),
            w("bark",  listOf(s("b",  listOf(0)),    s("ar", listOf(1,2)), s("k",  listOf(3)))),
            w("dark",  listOf(s("d",  listOf(0)),    s("ar", listOf(1,2)), s("k",  listOf(3)))),
            // or
            w("for",   listOf(s("f",  listOf(0)),    s("or", listOf(1,2)))),
            w("fork",  listOf(s("f",  listOf(0)),    s("or", listOf(1,2)), s("k",  listOf(3)))),
            w("corn",  listOf(s("c",  listOf(0)),    s("or", listOf(1,2)), s("n",  listOf(3)))),
            w("horn",  listOf(s("h",  listOf(0)),    s("or", listOf(1,2)), s("n",  listOf(3)))),
            w("short", listOf(s("sh", listOf(0,1)),  s("or", listOf(2,3)), s("t",  listOf(4)))),
            // er
            w("her",   listOf(s("h",  listOf(0)),    s("er", listOf(1,2)))),
            w("fern",  listOf(s("f",  listOf(0)),    s("er", listOf(1,2)), s("n",  listOf(3)))),
            w("verb",  listOf(s("v",  listOf(0)),    s("er", listOf(1,2)), s("b",  listOf(3)))),
            w("herd",  listOf(s("h",  listOf(0)),    s("er", listOf(1,2)), s("d",  listOf(3)))),
            // ir
            w("bird",  listOf(s("b",  listOf(0)),    s("ir", listOf(1,2)), s("d",  listOf(3)))),
            w("girl",  listOf(s("g",  listOf(0)),    s("ir", listOf(1,2)), s("l",  listOf(3)))),
            w("first", listOf(s("f",  listOf(0)),    s("ir", listOf(1,2)), s("st", listOf(3,4)))),
            w("dirt",  listOf(s("d",  listOf(0)),    s("ir", listOf(1,2)), s("t",  listOf(3)))),
            // ur
            w("burn",  listOf(s("b",  listOf(0)),    s("ur", listOf(1,2)), s("n",  listOf(3)))),
            w("turn",  listOf(s("t",  listOf(0)),    s("ur", listOf(1,2)), s("n",  listOf(3)))),
            w("curl",  listOf(s("c",  listOf(0)),    s("ur", listOf(1,2)), s("l",  listOf(3)))),
            w("hurt",  listOf(s("h",  listOf(0)),    s("ur", listOf(1,2)), s("t",  listOf(3))))
        )
    )
)

// ── ViewModel ─────────────────────────────────────────────────────────────────

@HiltViewModel
class PhonicsListenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    private val levelKeyStr: String = savedStateHandle["levelKey"] ?: "beginningBlends"
    val config: PhonicsListenConfig = run {
        val key = try { PhonicsListenLevelKey.valueOf(levelKeyStr) }
                  catch (_: Exception) { PhonicsListenLevelKey.beginningBlends }
        phonicsListenConfigs[key] ?: phonicsListenConfigs[PhonicsListenLevelKey.beginningBlends]!!
    }

    var wordIndex by mutableIntStateOf(0); private set
    var uiState by mutableStateOf(PhonicsListenUiState()); private set

    val totalWords: Int get() = config.words.size
    val currentWord: ListenWord get() = config.words[wordIndex]

    private var autoPlayJob: Job? = null

    fun onSegmentTap(idx: Int) {
        if (idx >= currentWord.segments.count()) return
        autoPlayJob?.cancel()
        audioManager.stop()
        uiState = uiState.copy(segmentIndex = idx, playedSegments = uiState.playedSegments + idx)
        audioManager.playPhonicsSound(currentWord.segments[idx].audioFileName)
        audioManager.onAudioCompleted = {
            if (uiState.segmentIndex == idx) {
                val next = idx + 1
                if (next >= currentWord.segments.size) {
                    playFullWord()
                    uiState = uiState.copy(wordDone = true)
                } else {
                    uiState = uiState.copy(segmentIndex = -1)
                }
            }
        }
    }

    fun startAutoPlay() {
        if (uiState.isPlaying) return
        uiState = uiState.copy(isPlaying = true, isAutoMode = true)
        autoPlayJob = viewModelScope.launch {
            val word = currentWord
            for (segIdx in word.segments.indices) {
                if (uiState.playedSegments.contains(segIdx)) continue
                uiState = uiState.copy(segmentIndex = segIdx, playedSegments = uiState.playedSegments + segIdx)
                suspendCancellableCoroutine { cont ->
                    audioManager.playPhonicsSound(word.segments[segIdx].audioFileName)
                    audioManager.onAudioCompleted = { if (cont.isActive) cont.resume(Unit) }
                    cont.invokeOnCancellation { audioManager.stop() }
                }
                delay(120)
            }
            uiState = uiState.copy(wordDone = true, segmentIndex = -1)
            suspendCancellableCoroutine { cont ->
                audioManager.playPhonicsSound("phonics_word/${word.word}")
                audioManager.onAudioCompleted = { if (cont.isActive) cont.resume(Unit) }
                cont.invokeOnCancellation { audioManager.stop() }
            }
            uiState = uiState.copy(isPlaying = false)
        }
    }

    fun pauseAutoPlay() {
        autoPlayJob?.cancel()
        autoPlayJob = null
        audioManager.stop()
        uiState = uiState.copy(isPlaying = false)
    }

    fun nextWord() {
        if (wordIndex >= totalWords - 1) return
        pauseAutoPlay()
        wordIndex += 1
        uiState = PhonicsListenUiState(isAutoMode = uiState.isAutoMode, isGoingForward = true)
    }

    fun prevWord() {
        if (wordIndex <= 0) return
        pauseAutoPlay()
        wordIndex -= 1
        uiState = PhonicsListenUiState(isAutoMode = uiState.isAutoMode, isGoingForward = false)
    }

    fun toggleMode() {
        pauseAutoPlay()
        uiState = PhonicsListenUiState(isAutoMode = !uiState.isAutoMode, isGoingForward = uiState.isGoingForward)
    }

    fun playFullWord() {
        audioManager.stop()
        audioManager.playPhonicsSound("phonics_word/${currentWord.word}")
    }

    fun stop() {
        autoPlayJob?.cancel()
        audioManager.stop()
    }

    override fun onCleared() {
        super.onCleared()
        stop()
    }
}
