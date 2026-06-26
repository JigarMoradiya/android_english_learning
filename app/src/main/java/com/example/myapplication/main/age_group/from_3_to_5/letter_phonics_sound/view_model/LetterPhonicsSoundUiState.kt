package com.example.myapplication.main.age_group.from_3_to_5.letter_phonics_sound.view_model

data class LetterPhonicsSoundItem(
    val letter: String,
    val word: String,
    val phonicsSoundText: String,
    val highlightTimings: List<Double>?,
)

data class LetterPhonicsSoundUiState(
    val letters: List<LetterPhonicsSoundItem> = emptyList(),
    val selectedLetter: String? = null,
    val animateObjectImage: Boolean = false,
    val isDevMode: Boolean = false,
    val phonicsAnimationTrigger: Int = 0,
) {
    val selectedItem: LetterPhonicsSoundItem?
        get() = letters.firstOrNull { it.letter == selectedLetter }
}
