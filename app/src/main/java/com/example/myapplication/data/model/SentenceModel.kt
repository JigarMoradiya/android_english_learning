package com.example.myapplication.data.model

import java.util.UUID

// ==============================
// UNIT SELECTION SCREEN
// ==============================
enum class UnitSelectionScreen {
    READ_AND_LISTEN_SENTENCE,
    ONE_WORD_ANSWER,
    FILL_THE_MISSING_WORD,
    MATCH_THE_PICTURE,
    WHICH_SENTENCE_SOUNDS_RIGHT,
    FIND_THE_CORRECT_WRITING,
    SENTENCE_CHECK,
    BUILD_THE_SENTENCE
}

fun getUnitSelectionScreen(name: String): UnitSelectionScreen {
    return try {
        UnitSelectionScreen.valueOf(name)
    } catch (e: Exception) {
        UnitSelectionScreen.READ_AND_LISTEN_SENTENCE
    }
}

// ==============================
// NAVIGATION MODEL
// ==============================
data class ChooseRightSentenceModel(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val subTitle: String,
    val imageName: String,
    val color: String,
//    val route: AppRoute
)

// ==============================
// SENTENCE UNIT
// ==============================
enum class SentenceUnit {
    PLAY_AND_FUN,
    HOME_LIFE,
    SCHOOL_LIFE,
    NATURE,
    FAMILY,
    FOOD,
    DAILY_ROUTINE,
    EMOTIONS,
    COLORS_SHAPES,
    TRANSPORT,
    WEATHER,
    COMMUNITY,
    SPORTS,
    FESTIVALS
}

fun getSentenceUnit(name: String): SentenceUnit {
    return try {
        SentenceUnit.valueOf(name)
    } catch (e: Exception) {
        SentenceUnit.PLAY_AND_FUN
    }
}


// ==============================
// SENTENCE LEVEL
// ==============================
enum class SentenceLevel(val title: String) {
    EASY("Short Sentence"),
    MEDIUM("Long Sentence")
}
fun getSentenceLevel(name: String): SentenceLevel {
    return try {
        SentenceLevel.valueOf(name)
    } catch (e: Exception) {
        SentenceLevel.EASY
    }
}

// ==============================
// SENTENCE UNIT MODEL
// ==============================
data class SentenceUnitModel(
    val unit: SentenceUnit,
    val title: String,
    val imageName: String,
    val description: String
) {
    val id: SentenceUnit = unit
}

// ==============================
// MAIN LESSON MODEL
// ==============================
data class ReadSentenceItemNew(
    val id: String,
    val imageName: String,
    val title: String,
    val unit: SentenceUnit,
    val level: SentenceLevel,
    val ageGroup: String,
    val learningGoals: List<String>,
    val order: Int,
    val sentences: List<LessonSentence>
)

// ==============================
// LESSON SENTENCE
// ==============================
data class LessonSentence(
    val id: String,
    val text: String,
    val type: SentenceType,
    val tense: String,
    val difficulty: Int,
    val isPrimary: Boolean,
    val grammarFocus: List<String>,
    val tags: List<String>,
    val distractorGroup: String,
    val activities: SentenceActivities,
    val blankableWords: List<BlankableWord>
)

// ==============================
// SENTENCE TYPE
// ==============================
enum class SentenceType {
    STATEMENT,
    QUESTION,
    EXCLAMATION
}

// ==============================
// ACTIVITIES
// ==============================
data class SentenceActivities(
    val readListen: Boolean,
    val fillBlank: Boolean,
    val chooseCorrect: Boolean,
    val sentenceBuilder: Boolean
)

// ==============================
// BLANKABLE WORD
// ==============================
data class BlankableWord(
    val word: String,
    val type: WordType
)

// ==============================
// WORD TYPE
// ==============================
enum class WordType {
    NOUN,
    ADJECTIVE,
    VERB,
    PRONOUN
}

// ==============================
// COMPREHENSION QUESTION
// ==============================
data class ComprehensionQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswer: String
)

// ==============================
// MATCH PICTURE QUESTION
// ==============================
data class MatchPictureQuestion(
    val id: String,
    val imageId: String,
    val imageName: String,
    val level: SentenceLevel,
    val correctSentence: String,
    val wrongOptions: List<String>
)

// ==============================
// WHICH SENTENCE SOUNDS RIGHT
// ==============================
data class WhichSentenceSoundsRight(
    val id: String,
    val unit: String,
    val level: SentenceLevel,
    val correctSentence: String,
    val wrongOptions: List<String>
)

// ==============================
// GRAMMAR QUESTION
// ==============================
data class GrammarQuestion(
    val id: String,
    val correctSentence: String,
    val imageName: String,
    val options: List<String>
)

// ==============================
// TRUE / FALSE QUESTION
// ==============================
data class TrueFalseQuestion(
    val id: String,
    val imageName: String,
    val statement: String,
    val isTrue: String
)

// ==============================
// SENTENCE BUILDER
// ==============================
data class SentenceBuilderQuestion(
    val id: String,
    val imageName: String,
    val correctSentence: String
)