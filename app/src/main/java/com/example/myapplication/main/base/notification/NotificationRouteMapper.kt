package com.example.myapplication.main.base.notification

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
}
