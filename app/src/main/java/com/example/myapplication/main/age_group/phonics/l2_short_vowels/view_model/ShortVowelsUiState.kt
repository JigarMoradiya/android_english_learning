package com.example.myapplication.main.age_group.phonics.l2_short_vowels.view_model

data class ShortVowelExample(val word: String)

data class ShortVowelModel(
    val vowel: String,
    val label: String,
    val soundWord: String,
    val anchorWord: String,
    val anchorEmoji: String,
    val colorHex: Long,
    val soundFile: String,
    val anchorAudioFile: String,
    val examples: List<ShortVowelExample>
)

val shortVowelData = listOf(
    ShortVowelModel(
        vowel = "A", label = "/a/ as in... · 🎬 open wide like a doctor visit! 😮", soundWord = "aah",
        anchorWord = "ant", anchorEmoji = "🐜", colorHex = 0xFFE53935L,
        soundFile = "phonics_letter/sound_a", anchorAudioFile = "phonics_word/ant",
        examples = listOf(ShortVowelExample("cat"), ShortVowelExample("bat"), ShortVowelExample("hat"), ShortVowelExample("man"))
    ),
    ShortVowelModel(
        vowel = "E", label = "/e/ as in... · 🎬 stretch a little smile! 🙂", soundWord = "eh",
        anchorWord = "egg", anchorEmoji = "🥚", colorHex = 0xFFF57C00L,
        soundFile = "phonics_letter/sound_e", anchorAudioFile = "phonics_word/egg",
        examples = listOf(ShortVowelExample("hen"), ShortVowelExample("ten"), ShortVowelExample("pen"), ShortVowelExample("red"))
    ),
    ShortVowelModel(
        vowel = "I", label = "/i/ as in... · 🎬 tiny quick smile — ih ih ih! 🐣", soundWord = "ih",
        anchorWord = "ink", anchorEmoji = "🖊️", colorHex = 0xFF7B1FA2L,
        soundFile = "phonics_letter/sound_i", anchorAudioFile = "phonics_word/ink",
        examples = listOf(ShortVowelExample("pig"), ShortVowelExample("sit"), ShortVowelExample("big"), ShortVowelExample("win"))
    ),
    ShortVowelModel(
        vowel = "O", label = "/o/ as in... · 🎬 make a round O mouth! ⭕", soundWord = "oh",
        anchorWord = "ox", anchorEmoji = "🐂", colorHex = 0xFF2E7D32L,
        soundFile = "phonics_letter/sound_o", anchorAudioFile = "phonics_word/ox",
        examples = listOf(ShortVowelExample("hot"), ShortVowelExample("dog"), ShortVowelExample("log"), ShortVowelExample("pot"))
    ),
    ShortVowelModel(
        vowel = "U", label = "/u/ as in... · 🎬 relax and say uh! 😌", soundWord = "uh",
        anchorWord = "up", anchorEmoji = "⬆️", colorHex = 0xFF1565C0L,
        soundFile = "phonics_letter/sound_u", anchorAudioFile = "phonics_word/up",
        examples = listOf(ShortVowelExample("bug"), ShortVowelExample("sun"), ShortVowelExample("cup"), ShortVowelExample("run"))
    )
)

enum class ShortVowelPhase { IDLE, VOWEL_IN, ANCHOR_IN, EXAMPLES_IN }

data class ShortVowelsUiState(
    val selectedVowel: ShortVowelModel? = null,
    val phase: ShortVowelPhase = ShortVowelPhase.IDLE,
    val visibleExamples: Int = 0,
    val animateAnchor: Boolean = false
)
