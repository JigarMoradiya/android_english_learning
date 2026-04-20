package com.example.myapplication.main.base.nav

sealed class RouteNavigation(val route: String) {

    object AgeCategories : RouteNavigation("AgeCategories")
    object AgeGroup3to5 : RouteNavigation("AgeGroup3to5")
    object AlphabetTracing : RouteNavigation("AlphabetTracing")
    object LetterRecognition : RouteNavigation("LetterRecognition")
    object ABCDWithImages : RouteNavigation("ABCDWithImages")
    object MatchLetters : RouteNavigation("MatchLetters")
    object FillTheBlankLetters : RouteNavigation("FillTheBlankLetters")
    object ArrangeLetterInSequence : RouteNavigation("ArrangeLetterInSequence")
    object MatchLetterWithImage : RouteNavigation("MatchLetterWithImage")
    object MissingLetterEasy : RouteNavigation("MissingLetterEasy")
    object DragDropWord : RouteNavigation("DragDropWord")
    object ColoringAlphabets : RouteNavigation("ColoringAlphabets")



    object AgeGroup5to7 : RouteNavigation("AgeGroup5to7")
    object VocabularyBuilding : RouteNavigation("VocabularyBuilding")
    object VocabularyDetail : RouteNavigation("VocabularyDetail/{type}/{title}") {
        fun vocabularyDetail(type: String,title : String): String =
            "VocabularyDetail/$type/$title"
    }
    object ColoringWord : RouteNavigation("ColoringWord")
    object WordMatchImage : RouteNavigation("WordMatchImage")
    object ListenAndSelectWord : RouteNavigation("ListenAndSelectWord")
    object MissingLetterMedium : RouteNavigation("MissingLetterMedium")
    object WordJigsaw : RouteNavigation("WordJigsaw")
    object ArticlesAAn : RouteNavigation("ArticlesAAn")
    object ArticlesAAnExample : RouteNavigation("ArticlesAAnExample")
    object SightWords : RouteNavigation("SightWords")
    object ArticleChoice : RouteNavigation("ArticleChoice")
    object SightWordChoice : RouteNavigation("SightWordChoice")

    object AgeGroup6to8 : RouteNavigation("AgeGroup6to8")
    object SentenceUnitList : RouteNavigation("SentenceUnitList/{screenType}") {
        fun sentenceUnitList(screenType: String): String = "SentenceUnitList/$screenType"
    }
    object SentenceLessonList : RouteNavigation("SentenceLessonList/{screenType}/{unit}/{level}") {
        fun sentenceLessonList(screenType: String, unit: String, level: String): String = "SentenceLessonList/$screenType/$unit/$level"
    }
    object ReadAndListen : RouteNavigation("ReadAndListen")
    object OneWordAnswer : RouteNavigation("OneWordAnswer")
    object FillTheMissingWord : RouteNavigation("FillTheMissingWord")
    object ChooseTheRightSentence : RouteNavigation("ChooseTheRightSentence")
    object SentenceCheck : RouteNavigation("SentenceCheck")
    object SentenceBuilder : RouteNavigation("SentenceBuilder")

}