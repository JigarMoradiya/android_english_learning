package com.example.myapplication.main.base.notification

import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.model.UnitSelectionScreen
import com.example.myapplication.main.base.nav.RouteNavigation

object NotificationRouteMapper {

    fun routeFromType(type: String): String? = when (type) {
        // Age groups
        "age3to5" -> RouteNavigation.AgeGroup3to5.route
        "age5to7" -> RouteNavigation.AgeGroup5to7.route
        "age6to8" -> RouteNavigation.AgeGroup6to8.route

        // 3 to 5 screens
        "letterTracing" -> RouteNavigation.AlphabetTracing.route
        "letterRecognition" -> RouteNavigation.LetterRecognition.route
        "abcdWithImages" -> RouteNavigation.ABCDWithImages.route
        "coloringAlphabets" -> RouteNavigation.ColoringAlphabets.route
        "letterMatching" -> RouteNavigation.MatchLetters.route
        "letterMatchingWithImage" -> RouteNavigation.MatchLetterWithImage.route
        "missingLetter3_5" -> RouteNavigation.MissingLetterEasy.route
        "dragAndDropWord" -> RouteNavigation.DragDropWord.route
        "fillTheBlankLetter" -> RouteNavigation.FillTheBlankLetters.route
        "arrangeLetterInSequence" -> RouteNavigation.ArrangeLetterInSequence.route

        // 5 to 7 screens
        "vocabularyBuilding" -> RouteNavigation.VocabularyBuilding.route
        "coloringWord" -> RouteNavigation.ColoringWord.route
        "wordMatchImage" -> RouteNavigation.WordMatchImage.route
        "listenAndSelectWord" -> RouteNavigation.ListenAndSelectWord.route
        "wordJigsaw" -> RouteNavigation.WordJigsaw.route
        "articlesA_An" -> RouteNavigation.ArticlesAAn.route
        "articlesA_AnExample" -> RouteNavigation.ArticlesAAnExample.route
        "sightWords" -> RouteNavigation.SightWords.route
        "articleChoice" -> RouteNavigation.ArticleChoice.route
        "sightWordChoice" -> RouteNavigation.SightWordChoice.route
        "missingLetter5_7" -> RouteNavigation.MissingLetterMedium.route

        // 6 to 8 screens (unit lists keyed by screenType)
        "readAndListenSentence" -> RouteNavigation.SentenceUnitList.sentenceUnitList(UnitSelectionScreen.READ_AND_LISTEN_SENTENCE.name)
        "oneWordAnswer" -> RouteNavigation.SentenceUnitList.sentenceUnitList(UnitSelectionScreen.ONE_WORD_ANSWER.name)
        "fillTheMissingWord" -> RouteNavigation.SentenceUnitList.sentenceUnitList(UnitSelectionScreen.FILL_THE_MISSING_WORD.name)
        "matchThePicture" -> RouteNavigation.SentenceUnitList.sentenceUnitList(UnitSelectionScreen.MATCH_THE_PICTURE.name)
        "whichSentenceSoundsRight" -> RouteNavigation.SentenceUnitList.sentenceUnitList(UnitSelectionScreen.WHICH_SENTENCE_SOUNDS_RIGHT.name)
        "findTheCorrectWriting" -> RouteNavigation.SentenceUnitList.sentenceUnitList(UnitSelectionScreen.FIND_THE_CORRECT_WRITING.name)
        "sentenceCheck" -> RouteNavigation.SentenceUnitList.sentenceUnitList(UnitSelectionScreen.SENTENCE_CHECK.name)
        "buildTheSentence" -> RouteNavigation.SentenceUnitList.sentenceUnitList(UnitSelectionScreen.BUILD_THE_SENTENCE.name)
        "chooseTheRightSentence" -> RouteNavigation.ChooseTheRightSentence.route

        else -> null
    }

    /**
     * The access-gate module for a notification type — the same id its home tile
     * checks, so a notification tap can't skip the paywall or the daily limit.
     * Empty = free entry (age groups and lists whose inner pages gate themselves).
     */
    fun moduleIdFromType(type: String): String = when (type) {
        "letterTracing" -> ModuleID.ALPHABET_TRACING
        "letterRecognition" -> ModuleID.LETTER_RECOGNITION
        "abcdWithImages" -> ModuleID.ABCD_WITH_IMAGES
        "coloringAlphabets" -> ModuleID.COLORING_ALPHABETS
        "letterMatching" -> ModuleID.MATCH_UPPER_LOWER
        "letterMatchingWithImage" -> ModuleID.MATCH_LETTER_WITH_IMAGE
        "missingLetter3_5" -> ModuleID.MISSING_LETTER
        "dragAndDropWord" -> ModuleID.DRAG_DROP_LETTERS
        "fillTheBlankLetter" -> ModuleID.FILL_THE_BLANK_LETTER
        "arrangeLetterInSequence" -> ModuleID.ARRANGE_LETTER_SEQUENCE

        "coloringWord" -> ModuleID.COLORING_WORDS
        "wordMatchImage" -> ModuleID.MATCH_WORD_WITH_PICTURE
        "listenAndSelectWord" -> ModuleID.LISTEN_AND_SELECT
        "wordJigsaw" -> ModuleID.WORD_JIGSAW
        "articlesA_An", "articlesA_AnExample" -> ModuleID.ARTICLES_A_AN
        "sightWords" -> ModuleID.SIGHT_WORDS
        "articleChoice" -> ModuleID.ARTICLES_CHOICE
        "sightWordChoice" -> ModuleID.SIGHT_WORD_CHOICE
        "missingLetter5_7" -> ModuleID.MISSING_LETTER_57

        "readAndListenSentence" -> ModuleID.READ_LISTEN
        "oneWordAnswer" -> ModuleID.ONE_WORD_ANSWER
        "fillTheMissingWord" -> ModuleID.FILL_MISSING_WORD
        "matchThePicture" -> ModuleID.MATCH_THE_PICTURE
        "whichSentenceSoundsRight" -> ModuleID.WHICH_SENTENCE_RIGHT
        "findTheCorrectWriting" -> ModuleID.FIND_CORRECT_WRITING
        "sentenceCheck" -> ModuleID.SENTENCE_CHECK
        "buildTheSentence" -> ModuleID.SENTENCE_BUILDER

        else -> ""
    }
}
