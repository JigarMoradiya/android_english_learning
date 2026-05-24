package com.example.myapplication.main.base.nav

sealed class RouteNavigation(val route: String) {

    object AgeCategories : RouteNavigation("AgeCategories")
    object Settings : RouteNavigation("Settings")
    object ParentProgress : RouteNavigation("ParentProgress")
    object AccessPlan : RouteNavigation("AccessPlan")
    object AgeGroup3to5 : RouteNavigation("AgeGroup3to5")
    object AlphabetTracing : RouteNavigation("AlphabetTracing")
    object LetterRecognition : RouteNavigation("LetterRecognition")
    object ABCDWithImages : RouteNavigation("ABCDWithImages")
    object MatchLetters : RouteNavigation("MatchLetters")
    object FillTheBlankLetters : RouteNavigation("FillTheBlankLetters")
    object FillTheBlankLettersPlay : RouteNavigation("FillTheBlankLettersPlay/{position}/{mode}") {
        fun createRoute(position: String, mode: String) = "FillTheBlankLettersPlay/$position/$mode"
    }
    object ArrangeLetterInSequence : RouteNavigation("ArrangeLetterInSequence")
    object MatchLetterWithImage : RouteNavigation("MatchLetterWithImage")
    object MissingLetterEasy : RouteNavigation("MissingLetterEasy")
    object DragDropWord : RouteNavigation("DragDropWord")
    object ColoringAlphabets : RouteNavigation("ColoringAlphabets")
    object LetterPhonicsSoundRoute : RouteNavigation("LetterPhonicsSoundRoute")
    object PhonicsReadingLevels : RouteNavigation("PhonicsReadingLevels")



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
    // ── AgeGroup 5–7: Opposite Words ────────────────────────────────────────
    object OppositeWords : RouteNavigation("OppositeWords")
    object OppositeWordActivities : RouteNavigation("OppositeWordActivities")
    object MatchOpposites : RouteNavigation("MatchOpposites/{difficulty}") {
        fun matchOpposites(difficulty: String): String = "MatchOpposites/$difficulty"
    }
    object ChooseOpposite : RouteNavigation("ChooseOpposite/{difficulty}") {
        fun chooseOpposite(difficulty: String): String = "ChooseOpposite/$difficulty"
    }

    // ── AgeGroup 5–7: Singular & Plural ─────────────────────────────────────
    object SingularPlural : RouteNavigation("SingularPlural")   // lesson page
    object SingularPluralActivities : RouteNavigation("SingularPluralActivities")
    object MatchSingularPlural : RouteNavigation("MatchSingularPlural")
    object ChooseSingularPluralForm : RouteNavigation("ChooseSingularPluralForm")

    object AgeGroup6to8 : RouteNavigation("AgeGroup6to8")
    object SentenceUnitList : RouteNavigation("SentenceUnitList/{screenType}") {
        fun sentenceUnitList(screenType: String): String = "SentenceUnitList/$screenType"
    }
    object SentenceLessonList : RouteNavigation("SentenceLessonList/{screenType}/{unit}/{level}") {
        fun sentenceLessonList(screenType: String, unit: String, level: String): String = "SentenceLessonList/$screenType/$unit/$level"
    }
    object ReadAndListen : RouteNavigation("ReadAndListen/{screenType}/{lessonData}"){
        fun readAndListen(screenType: String, lessonData: String): String = "ReadAndListen/$screenType/$lessonData"
    }
    object OneWordAnswer : RouteNavigation("OneWordAnswer/{screenType}/{lessonData}/{level}"){
        fun oneWordAnswer(screenType: String, lessonData: String,level : String): String = "OneWordAnswer/$screenType/$lessonData/$level"
    }

    object FillTheMissingWord : RouteNavigation("FillTheMissingWord/{screenType}/{lessonData}"){
        fun fillTheMissingWord(screenType: String, lessonData: String): String = "FillTheMissingWord/$screenType/$lessonData"
    }
    object ChooseTheRightSentence : RouteNavigation("ChooseTheRightSentence")
    object MatchThePicture : RouteNavigation("MatchThePicture/{unit}/{level}") {
        fun matchThePicture(unit: String, level: String): String = "MatchThePicture/$unit/$level"
    }
    object WhichSentenceSoundRight : RouteNavigation("WhichSentenceSoundRight/{unit}/{level}") {
        fun whichSentenceSoundRight(unit: String, level: String): String = "WhichSentenceSoundRight/$unit/$level"
    }
    object FindTheCorrectWriting : RouteNavigation("FindTheCorrectWriting/{unit}/{level}") {
        fun findTheCorrectWriting(unit: String, level: String): String = "FindTheCorrectWriting/$unit/$level"
    }
    object SentenceCheck : RouteNavigation("SentenceCheck/{unit}/{level}") {
        fun sentenceCheck(unit: String, level: String): String = "SentenceCheck/$unit/$level"
    }
    object SentenceBuilder : RouteNavigation("SentenceBuilder/{unit}/{level}") {
        fun sentenceBuilder(unit: String, level: String): String = "SentenceBuilder/$unit/$level"
    }
    object GrammarBasic : RouteNavigation("GrammarBasic")
    object GrammarBasicNoun : RouteNavigation("GrammarBasicNoun")
    object GrammarBasicNounPractice : RouteNavigation("GrammarBasicNounPractice")
    object GrammarBasicVerb : RouteNavigation("GrammarBasicVerb")
    object GrammarBasicVerbPractice : RouteNavigation("GrammarBasicVerbPractice")
    object GrammarBasicAdjectives : RouteNavigation("GrammarBasicAdjectives")
    object GrammarBasicAdjectivesPractice : RouteNavigation("GrammarBasicAdjectivesPractice")
    object GrammarBasicPronouns : RouteNavigation("GrammarBasicPronouns")
    object GrammarBasicPronounsPractice : RouteNavigation("GrammarBasicPronounsPractice")

    // ── AgeGroup 6–8: Mixed Grammar Challenge ───────────────────────────────
    object MixedGrammarChallenge : RouteNavigation("MixedGrammarChallenge")
    // Beginner sub-menu
    object MixedGrammarBeginner : RouteNavigation("MixedGrammarBeginner")
    object TapTheWord : RouteNavigation("TapTheWord")
    object GrammarMultipleChoice : RouteNavigation("GrammarMultipleChoice")
    // Medium
    object DragToGrammarBucket : RouteNavigation("DragToGrammarBucket")
    // Advanced sub-menu
    object MixedGrammarAdvanced : RouteNavigation("MixedGrammarAdvanced")
    object FixTheSentence : RouteNavigation("FixTheSentence")
    object GrammarSentenceBuilder : RouteNavigation("GrammarSentenceBuilder")
    object GrammarFillTheBlanks : RouteNavigation("GrammarFillTheBlanks")
}
