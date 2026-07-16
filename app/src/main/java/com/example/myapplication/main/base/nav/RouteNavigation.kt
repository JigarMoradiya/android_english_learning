package com.example.myapplication.main.base.nav

sealed class RouteNavigation(val route: String) {

    object AgeCategories : RouteNavigation("AgeCategories")
    object Settings : RouteNavigation("Settings")
    object ParentProgress : RouteNavigation("ParentProgress")
    object SentenceProgress : RouteNavigation("SentenceProgress")   // Age 6-8 progress (item 5.3)
    object GrammarDrillAgreement : RouteNavigation("GrammarDrillAgreement")   // 2.2a
    object GrammarDrillHasHave : RouteNavigation("GrammarDrillHasHave")       // 4.1a
    object AccessPlan : RouteNavigation("AccessPlan")
    object AgeGroup3to5 : RouteNavigation("AgeGroup3to5")
    object AlphabetTracing : RouteNavigation("AlphabetTracing")
    object LetterPractice : RouteNavigation("LetterPractice")
    object LetterRecognition : RouteNavigation("LetterRecognition")
    object LetterRecognitionExplore : RouteNavigation("LetterRecognitionExplore")
    object MirrorLettersPicker : RouteNavigation("MirrorLettersPicker")
    object MirrorLettersIntro : RouteNavigation("MirrorLettersIntro/{pair}") {
        fun createRoute(pair: String) = "MirrorLettersIntro/$pair"
    }
    object MirrorLettersPractice : RouteNavigation("MirrorLettersPractice/{pair}") {
        fun createRoute(pair: String) = "MirrorLettersPractice/$pair"
    }
    object LetterSpeedGame : RouteNavigation("LetterSpeedGame")
    object ABCDWithImages : RouteNavigation("ABCDWithImages")
    object MatchLetters : RouteNavigation("MatchLetters")
    object FillTheBlankLetters : RouteNavigation("FillTheBlankLetters")
    object FillTheBlankLettersPlay : RouteNavigation("FillTheBlankLettersPlay/{position}/{mode}") {
        fun createRoute(position: String, mode: String) = "FillTheBlankLettersPlay/$position/$mode"
    }
    object ArrangeLetterInSequence : RouteNavigation("ArrangeLetterInSequence/{mode}") {
        fun createRoute(mode: String) = "ArrangeLetterInSequence/$mode"
    }
    object MatchLetterWithImage : RouteNavigation("MatchLetterWithImage")
    object MissingLetterEasy : RouteNavigation("MissingLetterEasy")
    object DragDropWord : RouteNavigation("DragDropWord")
    object ColoringAlphabets : RouteNavigation("ColoringAlphabets")
    object LetterPhonicsSoundRoute : RouteNavigation("LetterPhonicsSoundRoute")
    object LetterSoundsIntro : RouteNavigation("LetterSoundsIntro")
    object LetterSoundsPractice : RouteNavigation("LetterSoundsPractice")
    object PhonicsReadingLevels : RouteNavigation("PhonicsReadingLevels")
    object PhonicsComparisons : RouteNavigation("PhonicsComparisons")
    object PhonicsComparison : RouteNavigation("PhonicsComparison/{comparisonId}") {
        fun createRoute(comparisonId: String) = "PhonicsComparison/$comparisonId"
    }
    object ShortVowelsIntro : RouteNavigation("ShortVowelsIntro")
    object ShortVowelsLearn : RouteNavigation("ShortVowelsLearn")
    object ShortVowelsPractice : RouteNavigation("ShortVowelsPractice")
    object BlendingIntro : RouteNavigation("BlendingIntro")
    object BlendingLearn : RouteNavigation("BlendingLearn")
    object BlendingPractice : RouteNavigation("BlendingPractice")
    object CvcWordsIntro : RouteNavigation("CvcWordsIntro")
    object CvcWordsLearn : RouteNavigation("CvcWordsLearn")
    object CvcWordsPractice : RouteNavigation("CvcWordsPractice")
    object WordFamiliesIntro : RouteNavigation("WordFamiliesIntro")
    object WordFamiliesLearn : RouteNavigation("WordFamiliesLearn")
    object WordFamiliesPractice : RouteNavigation("WordFamiliesPractice")
    object ShortVowelRulesIntro : RouteNavigation("ShortVowelRulesIntro")
    object ShortVowelRulesLearn : RouteNavigation("ShortVowelRulesLearn")
    object ShortVowelRulesPractice : RouteNavigation("ShortVowelRulesPractice")

    // ── Phonics: Listen screen ───────────────────────────────────────────────
    object PhonicsListen : RouteNavigation("PhonicsListen/{levelKey}") {
        fun createRoute(levelKey: String) = "PhonicsListen/$levelKey"
    }

    // ── Phonics L7: Open Syllable ────────────────────────────────────────────
    object OpenSyllableIntro : RouteNavigation("OpenSyllableIntro")
    object OpenSyllableLearn : RouteNavigation("OpenSyllableLearn")
    object OpenSyllablePractice : RouteNavigation("OpenSyllablePractice")

    // ── Phonics L8: Vowel Teams ───────────────────────────────────────────────
    object VowelTeamsIntro : RouteNavigation("VowelTeamsIntro")
    object VowelTeamsLearn : RouteNavigation("VowelTeamsLearn")
    object VowelTeamsPractice : RouteNavigation("VowelTeamsPractice")

    // ── Phonics L9: Beginning Blends ─────────────────────────────────────────
    object BeginningBlendsIntro : RouteNavigation("BeginningBlendsIntro")
    object BeginningBlendsLearn : RouteNavigation("BeginningBlendsLearn")
    object BeginningBlendsPractice : RouteNavigation("BeginningBlendsPractice")

    // ── Phonics L10: Ending Blends ────────────────────────────────────────────
    object EndingBlendsIntro : RouteNavigation("EndingBlendsIntro")
    object EndingBlendsLearn : RouteNavigation("EndingBlendsLearn")
    object EndingBlendsPractice : RouteNavigation("EndingBlendsPractice")

    // ── Phonics L11: Digraphs ────────────────────────────────────────────────
    object DigraphsIntro : RouteNavigation("DigraphsIntro")
    object DigraphsLearn : RouteNavigation("DigraphsLearn")
    object DigraphsPractice : RouteNavigation("DigraphsPractice")

    // ── Phonics L10: Special Endings ─────────────────────────────────────────
    object SpecialEndingsIntro : RouteNavigation("SpecialEndingsIntro")
    object SpecialEndingsLearn : RouteNavigation("SpecialEndingsLearn")
    object SpecialEndingsPractice : RouteNavigation("SpecialEndingsPractice")

    // ── Phonics L12: Magic E ──────────────────────────────────────────────────
    object MagicEIntro : RouteNavigation("MagicEIntro")
    object MagicELearn : RouteNavigation("MagicELearn")
    object MagicEPractice : RouteNavigation("MagicEPractice")

    // ── Phonics L14: Diphthongs ───────────────────────────────────────────────
    object DiphthongsIntro : RouteNavigation("DiphthongsIntro")
    object DiphthongsLearn : RouteNavigation("DiphthongsLearn")
    object DiphthongsPractice : RouteNavigation("DiphthongsPractice")

    // ── Phonics L15: R-Controlled Vowels ─────────────────────────────────────
    object RControlledIntro : RouteNavigation("RControlledIntro")
    object RControlledLearn : RouteNavigation("RControlledLearn")
    object RControlledPractice : RouteNavigation("RControlledPractice")

    // ── Phonics L16: igh & gh Patterns ───────────────────────────────────────
    object IghGhIntro : RouteNavigation("IghGhIntro")
    object IghGhLearn : RouteNavigation("IghGhLearn")
    object IghGhPractice : RouteNavigation("IghGhPractice")

    // ── Phonics L17: Y as a Vowel ─────────────────────────────────────────────
    object YAsVowelIntro : RouteNavigation("YAsVowelIntro")
    object YAsVowelLearn : RouteNavigation("YAsVowelLearn")
    object YAsVowelPractice : RouteNavigation("YAsVowelPractice")

    // ── Phonics L18: 3-Letter Blends ──────────────────────────────────────────
    object ThreeLetterBlendsIntro : RouteNavigation("ThreeLetterBlendsIntro")
    object ThreeLetterBlendsLearn : RouteNavigation("ThreeLetterBlendsLearn")
    object ThreeLetterBlendsPractice : RouteNavigation("ThreeLetterBlendsPractice")

    // ── Phonics L19: Soft C & Soft G ──────────────────────────────────────────
    object SoftCSoftGIntro : RouteNavigation("SoftCSoftGIntro")
    object SoftCSoftGLearn : RouteNavigation("SoftCSoftGLearn")
    object SoftCSoftGPractice : RouteNavigation("SoftCSoftGPractice")

    // ── Phonics L20: Silent Letters ────────────────────────────────────────────
    object SilentLettersIntro : RouteNavigation("SilentLettersIntro")
    object SilentLettersLearn : RouteNavigation("SilentLettersLearn")
    object SilentLettersPractice : RouteNavigation("SilentLettersPractice")

    // ── Phonics L21: Word Endings ─────────────────────────────────────────────
    object WordEndingsIntro : RouteNavigation("WordEndingsIntro")
    object WordEndingsLearn : RouteNavigation("WordEndingsLearn")
    object WordEndingsPractice : RouteNavigation("WordEndingsPractice")

    // ── Phonics L22: Prefixes ─────────────────────────────────────────────────
    object PrefixesIntro : RouteNavigation("PrefixesIntro")
    object PrefixesLearn : RouteNavigation("PrefixesLearn")
    object PrefixesPractice : RouteNavigation("PrefixesPractice")

    // ── Phonics L23: Suffixes ─────────────────────────────────────────────────
    object SuffixesIntro : RouteNavigation("SuffixesIntro")
    object SuffixesLearn : RouteNavigation("SuffixesLearn")
    object SuffixesPractice : RouteNavigation("SuffixesPractice")

    // ── Phonics L24: Contractions ─────────────────────────────────────────────
    object ContractionsIntro : RouteNavigation("ContractionsIntro")
    object ContractionsLearn : RouteNavigation("ContractionsLearn")
    object ContractionsPractice : RouteNavigation("ContractionsPractice")

    // ── Phonics L25: Consonant + -le ──────────────────────────────────────────
    object ConsonantLeIntro : RouteNavigation("ConsonantLeIntro")
    object ConsonantLeLearn : RouteNavigation("ConsonantLeLearn")
    object ConsonantLePractice : RouteNavigation("ConsonantLePractice")

    // ── Phonics L26: Compound Words ───────────────────────────────────────────
    object CompoundWordsIntro : RouteNavigation("CompoundWordsIntro")
    object CompoundWordsLearn : RouteNavigation("CompoundWordsLearn")
    object CompoundWordsPractice : RouteNavigation("CompoundWordsPractice")

    // ── Phonics L27: Syllable Division ────────────────────────────────────────
    object SyllableDivisionIntro : RouteNavigation("SyllableDivisionIntro")
    object SyllableDivisionLearn : RouteNavigation("SyllableDivisionLearn")
    object SyllableDivisionPractice : RouteNavigation("SyllableDivisionPractice")

    // ── Phonics L28: Sight Words ──────────────────────────────────────────────
    object StarWordsIntro : RouteNavigation("StarWordsIntro")
    object StarWordsLearn : RouteNavigation("StarWordsLearn")
    object StarWordsPractice : RouteNavigation("StarWordsPractice")

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
    object SightWordSpeed : RouteNavigation("SightWordSpeed")
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
    object SpotWrongPlural : RouteNavigation("SpotWrongPlural")

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

    object FillTheMissingWord : RouteNavigation("FillTheMissingWord/{screenType}/{lessonData}/{level}"){
        fun fillTheMissingWord(screenType: String, lessonData: String, level: String): String = "FillTheMissingWord/$screenType/$lessonData/$level"
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
