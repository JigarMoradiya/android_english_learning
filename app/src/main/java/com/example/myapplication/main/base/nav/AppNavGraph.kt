package com.example.myapplication.main.base.nav

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.data.model.ReadSentenceItemNew
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceUnit
import com.example.myapplication.data.model.UnitSelectionScreen
import com.example.myapplication.data.model.getSentenceLevel
import com.example.myapplication.data.model.getSentenceUnit
import com.example.myapplication.data.model.getUnitSelectionScreen
import com.example.myapplication.main.age_category.MainLearningAgesCategoriesScreen
import com.example.myapplication.main.base.notification.NotificationCommand
import com.example.myapplication.main.base.notification.PendingNotificationRoute
import com.example.myapplication.main.age_group.AgeGroup3to5Page
import com.example.myapplication.main.age_group.AgeGroup5to7Page
import com.example.myapplication.main.age_group.AgeGroup6to8Page
import com.example.myapplication.main.age_group.from_3_to_5.abcd_with_images.ABCDWithImagesPage
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.AlphabetTracingPage
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.LetterPracticePage
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode
import com.example.myapplication.main.age_group.from_3_to_5.arrange_letter_sequence.ArrangeLetterInSequencePage
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.ColoringAlphabetsPage
import com.example.myapplication.main.age_group.from_3_to_5.letter_phonics_sound.LetterPhonicsSoundPage
import com.example.myapplication.main.age_group.from_3_to_5.letter_phonics_sound.LetterSoundsIntroPage
import com.example.myapplication.main.age_group.from_3_to_5.letter_phonics_sound.LetterSoundsPracticePage
import com.example.myapplication.main.age_group.from_3_to_5.phonics_reading.PhonicsReadingLevelsPage
import com.example.myapplication.main.age_group.phonics.l2_short_vowels.ShortVowelsIntroPage
import com.example.myapplication.main.age_group.phonics.l2_short_vowels.ShortVowelsLearnPage
import com.example.myapplication.main.age_group.phonics.l2_short_vowels.ShortVowelsPracticePage
import com.example.myapplication.main.age_group.phonics.l3_blending.BlendingIntroPage
import com.example.myapplication.main.age_group.phonics.l3_blending.BlendingLearnPage
import com.example.myapplication.main.age_group.phonics.l3_blending.BlendingPracticePage
import com.example.myapplication.main.age_group.phonics.l4_cvc_words.CvcWordsIntroPage
import com.example.myapplication.main.age_group.phonics.l4_cvc_words.CvcWordsLearnPage
import com.example.myapplication.main.age_group.phonics.l4_cvc_words.CvcWordsPracticePage
import com.example.myapplication.main.age_group.phonics.l5_short_vowel_rules.ShortVowelRulesIntroPage
import com.example.myapplication.main.age_group.phonics.l5_short_vowel_rules.ShortVowelRulesLearnPage
import com.example.myapplication.main.age_group.phonics.l5_short_vowel_rules.ShortVowelRulesPracticePage
import com.example.myapplication.main.age_group.phonics.l6_word_families.WordFamiliesIntroPage
import com.example.myapplication.main.age_group.phonics.l6_word_families.WordFamiliesLearnPage
import com.example.myapplication.main.age_group.phonics.l6_word_families.WordFamiliesPracticePage
import com.example.myapplication.main.age_group.phonics.listen.PhonicsListenPage
import com.example.myapplication.main.age_group.phonics.compare.ComparisonListPage
import com.example.myapplication.main.age_group.phonics.compare.ComparisonDetailPage
import com.example.myapplication.main.age_group.phonics.l11_open_syllable.OpenSyllableIntroPage
import com.example.myapplication.main.age_group.phonics.l11_open_syllable.OpenSyllableLearnPage
import com.example.myapplication.main.age_group.phonics.l11_open_syllable.OpenSyllablePracticePage
import com.example.myapplication.main.age_group.phonics.l13_vowel_teams.VowelTeamsIntroPage
import com.example.myapplication.main.age_group.phonics.l13_vowel_teams.VowelTeamsLearnPage
import com.example.myapplication.main.age_group.phonics.l13_vowel_teams.VowelTeamsPracticePage
import com.example.myapplication.main.age_group.phonics.l7_beginning_blends.BeginningBlendsIntroPage
import com.example.myapplication.main.age_group.phonics.l7_beginning_blends.BeginningBlendsLearnPage
import com.example.myapplication.main.age_group.phonics.l7_beginning_blends.BeginningBlendsPracticePage
import com.example.myapplication.main.age_group.phonics.l8_ending_blends.EndingBlendsIntroPage
import com.example.myapplication.main.age_group.phonics.l8_ending_blends.EndingBlendsLearnPage
import com.example.myapplication.main.age_group.phonics.l8_ending_blends.EndingBlendsPracticePage
import com.example.myapplication.main.age_group.phonics.l9_digraphs.DigraphsIntroPage
import com.example.myapplication.main.age_group.phonics.l9_digraphs.DigraphsLearnPage
import com.example.myapplication.main.age_group.phonics.l9_digraphs.DigraphsPracticePage
import com.example.myapplication.main.age_group.phonics.l10_special_endings.SpecialEndingsIntroPage
import com.example.myapplication.main.age_group.phonics.l10_special_endings.SpecialEndingsLearnPage
import com.example.myapplication.main.age_group.phonics.l10_special_endings.SpecialEndingsPracticePage
import com.example.myapplication.main.age_group.phonics.l12_magic_e.MagicEIntroPage
import com.example.myapplication.main.age_group.phonics.l12_magic_e.MagicELearnPage
import com.example.myapplication.main.age_group.phonics.l12_magic_e.MagicEPracticePage
import com.example.myapplication.main.age_group.phonics.l14_diphthongs.DiphthongsIntroPage
import com.example.myapplication.main.age_group.phonics.l14_diphthongs.DiphthongsLearnPage
import com.example.myapplication.main.age_group.phonics.l14_diphthongs.DiphthongsPracticePage
import com.example.myapplication.main.age_group.phonics.l15_r_controlled.RControlledIntroPage
import com.example.myapplication.main.age_group.phonics.l15_r_controlled.RControlledLearnPage
import com.example.myapplication.main.age_group.phonics.l15_r_controlled.RControlledPracticePage
import com.example.myapplication.main.age_group.phonics.l16_igh_gh.IghGhIntroPage
import com.example.myapplication.main.age_group.phonics.l16_igh_gh.IghGhLearnPage
import com.example.myapplication.main.age_group.phonics.l16_igh_gh.IghGhPracticePage
import com.example.myapplication.main.age_group.phonics.l17_y_as_vowel.YAsVowelIntroPage
import com.example.myapplication.main.age_group.phonics.l17_y_as_vowel.YAsVowelLearnPage
import com.example.myapplication.main.age_group.phonics.l17_y_as_vowel.YAsVowelPracticePage
import com.example.myapplication.main.age_group.phonics.l18_three_letter_blends.ThreeLetterBlendsIntroPage
import com.example.myapplication.main.age_group.phonics.l18_three_letter_blends.ThreeLetterBlendsLearnPage
import com.example.myapplication.main.age_group.phonics.l18_three_letter_blends.ThreeLetterBlendsPracticePage
import com.example.myapplication.main.age_group.phonics.l19_soft_c_soft_g.SoftCSoftGIntroPage
import com.example.myapplication.main.age_group.phonics.l19_soft_c_soft_g.SoftCSoftGLearnPage
import com.example.myapplication.main.age_group.phonics.l19_soft_c_soft_g.SoftCSoftGPracticePage
import com.example.myapplication.main.age_group.phonics.l20_silent_letters.SilentLettersIntroPage
import com.example.myapplication.main.age_group.phonics.l20_silent_letters.SilentLettersLearnPage
import com.example.myapplication.main.age_group.phonics.l20_silent_letters.SilentLettersPracticePage
import com.example.myapplication.main.age_group.phonics.l21_word_endings.WordEndingsIntroPage
import com.example.myapplication.main.age_group.phonics.l21_word_endings.WordEndingsLearnPage
import com.example.myapplication.main.age_group.phonics.l21_word_endings.WordEndingsPracticePage
import com.example.myapplication.main.age_group.phonics.l22_prefixes.PrefixesIntroPage
import com.example.myapplication.main.age_group.phonics.l22_prefixes.PrefixesLearnPage
import com.example.myapplication.main.age_group.phonics.l22_prefixes.PrefixesPracticePage
import com.example.myapplication.main.age_group.phonics.l23_suffixes.SuffixesIntroPage
import com.example.myapplication.main.age_group.phonics.l23_suffixes.SuffixesLearnPage
import com.example.myapplication.main.age_group.phonics.l23_suffixes.SuffixesPracticePage
import com.example.myapplication.main.age_group.phonics.l24_contractions.ContractionsIntroPage
import com.example.myapplication.main.age_group.phonics.l24_contractions.ContractionsLearnPage
import com.example.myapplication.main.age_group.phonics.l24_contractions.ContractionsPracticePage
import com.example.myapplication.main.age_group.phonics.l25_consonant_le.ConsonantLeIntroPage
import com.example.myapplication.main.age_group.phonics.l25_consonant_le.ConsonantLeLearnPage
import com.example.myapplication.main.age_group.phonics.l25_consonant_le.ConsonantLePracticePage
import com.example.myapplication.main.age_group.phonics.l26_compound_words.CompoundWordsIntroPage
import com.example.myapplication.main.age_group.phonics.l26_compound_words.CompoundWordsLearnPage
import com.example.myapplication.main.age_group.phonics.l26_compound_words.CompoundWordsPracticePage
import com.example.myapplication.main.age_group.phonics.l27_syllable_division.SyllableDivisionIntroPage
import com.example.myapplication.main.age_group.phonics.l27_syllable_division.SyllableDivisionLearnPage
import com.example.myapplication.main.age_group.phonics.l27_syllable_division.SyllableDivisionPracticePage
import com.example.myapplication.main.age_group.phonics.l28_sight_words.StarWordsIntroPage
import com.example.myapplication.main.age_group.phonics.l28_sight_words.StarWordsLearnPage
import com.example.myapplication.main.age_group.phonics.l28_sight_words.StarWordsPracticePage
import com.example.myapplication.main.age_group.from_3_to_5.drag_and_drop_word.DragDropWordPage35
import com.example.myapplication.main.age_group.from_3_to_5.fill_blank.BlankPosition
import com.example.myapplication.main.age_group.from_3_to_5.fill_blank.FillBlankLettersPage
import com.example.myapplication.main.age_group.from_3_to_5.fill_blank.FillBlankSetupScreen
import com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.LetterRecognitionPage
import com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.LetterRecognitionHubPage
import com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.mirror_letters.MirrorLetterPair
import com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.mirror_letters.MirrorLettersIntroPage
import com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.mirror_letters.MirrorLettersPickerPage
import com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.mirror_letters.MirrorLettersPracticePage
import com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.speed_game.LetterSpeedGamePage
import com.example.myapplication.main.age_group.from_3_to_5.match_latters.MatchLettersPage
import com.example.myapplication.main.age_group.from_3_to_5.match_letter_with_image.MatchLetterWithImagePage
import com.example.myapplication.main.age_group.from_3_to_5.missing_letter.MissingLetterPage35
import com.example.myapplication.main.age_group.from_3_to_5.missing_letter.view_model.DifficultyLevel
import com.example.myapplication.main.age_group.from_5_to_7.missing_letter.MissingLetterPage57
import com.example.myapplication.main.age_group.from_5_to_7.missing_letter.view_model.DifficultyLevel as DifficultyLevel57
import com.example.myapplication.main.age_group.from_5_to_7.drag_and_drop_word.DragDropWordPage57
import com.example.myapplication.main.age_group.from_5_to_7.article_choice.ArticleChoicePage
import com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.example.ArticlesAAnExamplePage
import com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.info.ArticlesAAnPage
import com.example.myapplication.main.age_group.from_5_to_7.coloring_word.ColoringWordPage
import com.example.myapplication.main.age_group.from_5_to_7.listen_and_select_answer.ListenAndSelectWordPage
import com.example.myapplication.main.age_group.from_5_to_7.sight_word_choice.SightWordChoicePage
import com.example.myapplication.main.age_group.from_5_to_7.sight_words.SightWordsPage
import com.example.myapplication.main.age_group.from_5_to_7.vocabulary_building.category_detail.VocabularyBuildingDetailPage
import com.example.myapplication.main.age_group.from_5_to_7.vocabulary_building.category_list.VocabularyBuildingPage
import com.example.myapplication.main.age_group.from_5_to_7.word_match_picture.WordMatchImagePage
import com.example.myapplication.main.age_group.from_6_to_8.choose_the_right_sentence.ChooseTheRightSentencePage
import com.example.myapplication.main.age_group.from_6_to_8.choose_the_right_sentence.find_the_correct_writing.FindTheCorrectWritingPage
import com.example.myapplication.main.age_group.from_6_to_8.choose_the_right_sentence.match_the_picture.MatchThePicturePage
import com.example.myapplication.main.age_group.from_6_to_8.choose_the_right_sentence.which_sentence_sound_right.WhichSentenceSoundRightPage
import com.example.myapplication.main.age_group.from_6_to_8.common.lesson.SentenceLessonPage
import com.example.myapplication.main.age_group.from_6_to_8.common.unit.SentenceUnitPage
import com.example.myapplication.main.age_group.from_6_to_8.fill_the_missing_word.FillTheMissingWordPage
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.GrammarBasicPage
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.adjectives.lesson.AdjectivesLessonPage
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.adjectives.practice.AdjectivesPracticePage
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.noun.lesson.NounLessonPage
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.noun.practice.NounPracticePage
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.pronouns.lesson.PronounLessonPage
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.pronouns.practice.PronounPracticePage
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.verb.lesson.VerbLessonPage
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.verb.practice.VerbPracticePage
import com.example.myapplication.main.age_group.from_6_to_8.one_word_answer.OneWordAnswerPage
import com.example.myapplication.main.age_group.from_6_to_8.read_listen.ReadAndListenPage
import com.example.myapplication.main.age_group.from_6_to_8.sentence_builder.SentenceBuilderPage
import com.example.myapplication.main.age_group.from_6_to_8.sentence_check.SentenceCheckPage
import com.example.myapplication.data.generation.loader.OppositeDifficulty
import com.example.myapplication.main.age_group.from_5_to_7.opposite_words.OppositeWordsPage
import com.example.myapplication.main.age_group.from_5_to_7.opposite_words.activities.OppositeWordActivitiesPage
import com.example.myapplication.main.age_group.from_5_to_7.opposite_words.match_opposites.MatchOppositesPage
import com.example.myapplication.main.age_group.from_5_to_7.opposite_words.choose_opposite.ChooseCorrectOppositePage
import com.example.myapplication.main.age_group.from_5_to_7.singular_plural.SingularPluralPage
import com.example.myapplication.main.age_group.from_5_to_7.singular_plural.SingularPluralActivitiesPage
import com.example.myapplication.main.age_group.from_5_to_7.singular_plural.match_form.MatchSingularPluralPage
import com.example.myapplication.main.age_group.from_5_to_7.singular_plural.choose_form.ChooseSingularPluralFormPage
import com.example.myapplication.main.age_group.from_5_to_7.singular_plural.spot_wrong.SpotWrongPluralPage
import com.example.myapplication.main.age_group.from_5_to_7.sight_words.speed.SightWordSpeedPage
import com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.MixedGrammarChallengePage
import com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.beginner.MixedGrammarBeginnerMenuPage
import com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.beginner.tap_the_word.TapTheWordPage
import com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.beginner.multiple_choice.GrammarMultipleChoicePage
import com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.medium.drag_to_bucket.DragToGrammarBucketPage
import com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.advanced.MixedGrammarAdvancedMenuPage
import com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.advanced.fix_sentence.FixTheSentencePage
import com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.advanced.sentence_builder.GrammarSentenceBuilderPage
import com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.advanced.fill_blanks.GrammarFillTheBlanksPage
import com.google.gson.Gson

@Composable
fun AppNavGraph(navController: NavHostController) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        PendingNotificationRoute.commands.collect { command ->
            when (command) {
                is NotificationCommand.Navigate -> {
                    navController.navigate(command.route) {
                        popUpTo(RouteNavigation.AgeCategories.route) { inclusive = false }
                        launchSingleTop = true
                    }
                    PendingNotificationRoute.clear()
                }
                is NotificationCommand.OpenLink -> {
                    runCatching {
                        val intent = Intent(Intent.ACTION_VIEW, command.url.toUri()).apply {
                            addCategory(Intent.CATEGORY_BROWSABLE)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        context.startActivity(intent)
                    }
                    PendingNotificationRoute.clear()
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = RouteNavigation.AgeCategories.route
    ) {
        // Home
        composable(RouteNavigation.AgeCategories.route) {
            MainLearningAgesCategoriesScreen(navController)
        }
        // Settings
        composable(RouteNavigation.Settings.route) {
            com.example.myapplication.main.settings.SettingsScreen(navController)
        }
        composable(RouteNavigation.AccessPlan.route) {
            com.example.myapplication.main.settings.AccessPlanScreen(navController)
        }
        composable(RouteNavigation.ParentProgress.route) {
            com.example.myapplication.main.parent.ParentProgressScreen(navController)
        }
        composable(RouteNavigation.SentenceProgress.route) {
            com.example.myapplication.main.age_group.from_6_to_8.progress.SentenceProgressPage(navController)
        }
        composable(RouteNavigation.GrammarDrillAgreement.route) {
            com.example.myapplication.main.age_group.from_6_to_8.grammar_drill.GrammarDrillPage(
                com.example.myapplication.data.generation.loader.GrammarDrillType.SUBJECT_VERB_AGREEMENT, navController)
        }
        composable(RouteNavigation.GrammarDrillHasHave.route) {
            com.example.myapplication.main.age_group.from_6_to_8.grammar_drill.GrammarDrillPage(
                com.example.myapplication.data.generation.loader.GrammarDrillType.HAS_HAVE, navController)
        }
        // Age Category 3 to 5
        composable(RouteNavigation.AgeGroup3to5.route) {
            AgeGroup3to5Page(navController)
        }
        composable(RouteNavigation.AlphabetTracing.route) {
            AlphabetTracingPage(navController)
        }
        composable(RouteNavigation.LetterPractice.route) {
            LetterPracticePage(navController)
        }
        composable(RouteNavigation.LetterRecognition.route) {
            LetterRecognitionHubPage(navController)
        }
        composable(RouteNavigation.LetterRecognitionExplore.route) {
            LetterRecognitionPage(navController)
        }
        composable(RouteNavigation.MirrorLettersPicker.route) {
            MirrorLettersPickerPage(navController)
        }
        composable(
            route = RouteNavigation.MirrorLettersIntro.route,
            arguments = listOf(navArgument("pair") { type = NavType.StringType })
        ) { back ->
            val pair = MirrorLetterPair.valueOf(back.arguments?.getString("pair") ?: MirrorLetterPair.BD.name)
            MirrorLettersIntroPage(navController, pair)
        }
        composable(
            route = RouteNavigation.MirrorLettersPractice.route,
            arguments = listOf(navArgument("pair") { type = NavType.StringType })
        ) { back ->
            val pair = MirrorLetterPair.valueOf(back.arguments?.getString("pair") ?: MirrorLetterPair.BD.name)
            MirrorLettersPracticePage(navController, pair)
        }
        composable(RouteNavigation.LetterSpeedGame.route) {
            LetterSpeedGamePage(navController)
        }
        composable(RouteNavigation.ABCDWithImages.route) {
            ABCDWithImagesPage(navController)
        }
        composable(RouteNavigation.MatchLetters.route) {
            MatchLettersPage(navController)
        }
        composable(RouteNavigation.FillTheBlankLetters.route) {
            FillBlankSetupScreen(navController)
        }
        composable(
            route = RouteNavigation.FillTheBlankLettersPlay.route,
            arguments = listOf(
                navArgument("position") { type = NavType.StringType },
                navArgument("mode")     { type = NavType.StringType }
            )
        ) { back ->
            val position = BlankPosition.valueOf(back.arguments?.getString("position") ?: BlankPosition.RANDOM.name)
            val mode     = LetterMode.valueOf(back.arguments?.getString("mode") ?: LetterMode.UPPERCASE.name)
            FillBlankLettersPage(navController, position, mode)
        }
        composable(
            RouteNavigation.ArrangeLetterInSequence.route,
            arguments = listOf(navArgument("mode") { type = NavType.StringType })
        ) { back ->
            val mode = LetterMode.valueOf(back.arguments?.getString("mode") ?: LetterMode.UPPERCASE.name)
            ArrangeLetterInSequencePage(navController, mode)
        }
        composable(RouteNavigation.MatchLetterWithImage.route) {
            MatchLetterWithImagePage(navController)
        }
        composable(RouteNavigation.MissingLetterEasy.route) {
            MissingLetterPage35(navController, DifficultyLevel.EASY)
        }
        composable(RouteNavigation.DragDropWord.route) {
            DragDropWordPage35(navController, DifficultyLevel.EASY)
        }
        composable(RouteNavigation.ColoringAlphabets.route) {
            ColoringAlphabetsPage(navController)
        }
        composable(RouteNavigation.LetterPhonicsSoundRoute.route) {
            LetterPhonicsSoundPage(navController)
        }
        composable(RouteNavigation.LetterSoundsIntro.route) {
            LetterSoundsIntroPage(navController)
        }
        composable(RouteNavigation.LetterSoundsPractice.route) {
            LetterSoundsPracticePage(navController)
        }
        composable(RouteNavigation.PhonicsReadingLevels.route) {
            PhonicsReadingLevelsPage(navController)
        }
        composable(RouteNavigation.PhonicsComparisons.route) {
            ComparisonListPage(navController)
        }
        composable(
            route = RouteNavigation.PhonicsComparison.route,
            arguments = listOf(navArgument("comparisonId") { type = NavType.StringType })
        ) {
            ComparisonDetailPage(navController)
        }
        composable(RouteNavigation.ShortVowelsIntro.route) {
            ShortVowelsIntroPage(navController)
        }
        composable(RouteNavigation.ShortVowelsLearn.route) {
            ShortVowelsLearnPage(navController)
        }
        composable(RouteNavigation.ShortVowelsPractice.route) {
            ShortVowelsPracticePage(navController)
        }
        composable(RouteNavigation.BlendingIntro.route) {
            BlendingIntroPage(navController)
        }
        composable(RouteNavigation.BlendingLearn.route) {
            BlendingLearnPage(navController)
        }
        composable(RouteNavigation.BlendingPractice.route) {
            BlendingPracticePage(navController)
        }
        composable(RouteNavigation.CvcWordsIntro.route) {
            CvcWordsIntroPage(navController)
        }
        composable(RouteNavigation.CvcWordsLearn.route) {
            CvcWordsLearnPage(navController)
        }
        composable(RouteNavigation.CvcWordsPractice.route) {
            CvcWordsPracticePage(navController)
        }
        composable(RouteNavigation.WordFamiliesIntro.route) {
            WordFamiliesIntroPage(navController)
        }
        composable(RouteNavigation.WordFamiliesLearn.route) {
            WordFamiliesLearnPage(navController)
        }
        composable(RouteNavigation.WordFamiliesPractice.route) {
            WordFamiliesPracticePage(navController)
        }
        composable(RouteNavigation.ShortVowelRulesIntro.route) {
            ShortVowelRulesIntroPage(navController)
        }
        composable(RouteNavigation.ShortVowelRulesLearn.route) {
            ShortVowelRulesLearnPage(navController)
        }
        composable(RouteNavigation.ShortVowelRulesPractice.route) {
            ShortVowelRulesPracticePage(navController)
        }

        // ── Phonics: Listen screen ───────────────────────────────────────────
        composable(
            route = RouteNavigation.PhonicsListen.route,
            arguments = listOf(navArgument("levelKey") { type = NavType.StringType })
        ) {
            PhonicsListenPage(navController = navController)
        }

        // ── Phonics L7: Open Syllable ────────────────────────────────────────
        composable(RouteNavigation.OpenSyllableIntro.route) {
            OpenSyllableIntroPage(navController)
        }
        composable(RouteNavigation.OpenSyllableLearn.route) {
            OpenSyllableLearnPage(navController)
        }
        composable(RouteNavigation.OpenSyllablePractice.route) {
            OpenSyllablePracticePage(navController)
        }

        // ── Phonics L8: Vowel Teams ──────────────────────────────────────────
        composable(RouteNavigation.VowelTeamsIntro.route) {
            VowelTeamsIntroPage(navController)
        }
        composable(RouteNavigation.VowelTeamsLearn.route) {
            VowelTeamsLearnPage(navController)
        }
        composable(RouteNavigation.VowelTeamsPractice.route) {
            VowelTeamsPracticePage(navController)
        }

        // ── Phonics L9: Beginning Blends ─────────────────────────────────────
        composable(RouteNavigation.BeginningBlendsIntro.route) {
            BeginningBlendsIntroPage(navController)
        }
        composable(RouteNavigation.BeginningBlendsLearn.route) {
            BeginningBlendsLearnPage(navController)
        }
        composable(RouteNavigation.BeginningBlendsPractice.route) {
            BeginningBlendsPracticePage(navController)
        }

        // ── Phonics L10: Ending Blends ────────────────────────────────────────
        composable(RouteNavigation.EndingBlendsIntro.route) {
            EndingBlendsIntroPage(navController)
        }
        composable(RouteNavigation.EndingBlendsLearn.route) {
            EndingBlendsLearnPage(navController)
        }
        composable(RouteNavigation.EndingBlendsPractice.route) {
            EndingBlendsPracticePage(navController)
        }

        // ── Phonics L11: Digraphs ────────────────────────────────────────────
        composable(RouteNavigation.DigraphsIntro.route) {
            DigraphsIntroPage(navController)
        }
        composable(RouteNavigation.DigraphsLearn.route) {
            DigraphsLearnPage(navController)
        }
        composable(RouteNavigation.DigraphsPractice.route) {
            DigraphsPracticePage(navController)
        }

        // ── Phonics L10: Special Endings ─────────────────────────────────────
        composable(RouteNavigation.SpecialEndingsIntro.route) {
            SpecialEndingsIntroPage(navController)
        }
        composable(RouteNavigation.SpecialEndingsLearn.route) {
            SpecialEndingsLearnPage(navController)
        }
        composable(RouteNavigation.SpecialEndingsPractice.route) {
            SpecialEndingsPracticePage(navController)
        }

        // ── Phonics L12: Magic E ──────────────────────────────────────────────
        composable(RouteNavigation.MagicEIntro.route) {
            MagicEIntroPage(navController)
        }
        composable(RouteNavigation.MagicELearn.route) {
            MagicELearnPage(navController)
        }
        composable(RouteNavigation.MagicEPractice.route) {
            MagicEPracticePage(navController)
        }
        // ── Phonics L14: Diphthongs ───────────────────────────────────────────
        composable(RouteNavigation.DiphthongsIntro.route) {
            DiphthongsIntroPage(navController)
        }
        composable(RouteNavigation.DiphthongsLearn.route) {
            DiphthongsLearnPage(navController)
        }
        composable(RouteNavigation.DiphthongsPractice.route) {
            DiphthongsPracticePage(navController)
        }
        // ── Phonics L15: R-Controlled Vowels ─────────────────────────────────
        composable(RouteNavigation.RControlledIntro.route) {
            RControlledIntroPage(navController)
        }
        composable(RouteNavigation.RControlledLearn.route) {
            RControlledLearnPage(navController)
        }
        composable(RouteNavigation.RControlledPractice.route) {
            RControlledPracticePage(navController)
        }
        // ── Phonics L16: igh & gh Patterns ───────────────────────────────────
        composable(RouteNavigation.IghGhIntro.route) {
            IghGhIntroPage(navController)
        }
        composable(RouteNavigation.IghGhLearn.route) {
            IghGhLearnPage(navController)
        }
        composable(RouteNavigation.IghGhPractice.route) {
            IghGhPracticePage(navController)
        }
        // ── Phonics L17: Y as a Vowel ─────────────────────────────────────────
        composable(RouteNavigation.YAsVowelIntro.route) {
            YAsVowelIntroPage(navController)
        }
        composable(RouteNavigation.YAsVowelLearn.route) {
            YAsVowelLearnPage(navController)
        }
        composable(RouteNavigation.YAsVowelPractice.route) {
            YAsVowelPracticePage(navController)
        }
        // ── Phonics L18: 3-Letter Blends ──────────────────────────────────────
        composable(RouteNavigation.ThreeLetterBlendsIntro.route) {
            ThreeLetterBlendsIntroPage(navController)
        }
        composable(RouteNavigation.ThreeLetterBlendsLearn.route) {
            ThreeLetterBlendsLearnPage(navController)
        }
        composable(RouteNavigation.ThreeLetterBlendsPractice.route) {
            ThreeLetterBlendsPracticePage(navController)
        }
        // ── Phonics L19: Soft C & Soft G ──────────────────────────────────────
        composable(RouteNavigation.SoftCSoftGIntro.route) {
            SoftCSoftGIntroPage(navController)
        }
        composable(RouteNavigation.SoftCSoftGLearn.route) {
            SoftCSoftGLearnPage(navController)
        }
        composable(RouteNavigation.SoftCSoftGPractice.route) {
            SoftCSoftGPracticePage(navController)
        }
        // ── Phonics L20: Silent Letters ────────────────────────────────────────
        composable(RouteNavigation.SilentLettersIntro.route) {
            SilentLettersIntroPage(navController)
        }
        composable(RouteNavigation.SilentLettersLearn.route) {
            SilentLettersLearnPage(navController)
        }
        composable(RouteNavigation.SilentLettersPractice.route) {
            SilentLettersPracticePage(navController)
        }
        // ── Phonics L21: Word Endings ──────────────────────────────────────────
        composable(RouteNavigation.WordEndingsIntro.route) {
            WordEndingsIntroPage(navController)
        }
        composable(RouteNavigation.WordEndingsLearn.route) {
            WordEndingsLearnPage(navController)
        }
        composable(RouteNavigation.WordEndingsPractice.route) {
            WordEndingsPracticePage(navController)
        }
        // ── Phonics L22: Prefixes ──────────────────────────────────────────────
        composable(RouteNavigation.PrefixesIntro.route) {
            PrefixesIntroPage(navController)
        }
        composable(RouteNavigation.PrefixesLearn.route) {
            PrefixesLearnPage(navController)
        }
        composable(RouteNavigation.PrefixesPractice.route) {
            PrefixesPracticePage(navController)
        }
        // ── Phonics L23: Suffixes ──────────────────────────────────────────────
        composable(RouteNavigation.SuffixesIntro.route) {
            SuffixesIntroPage(navController)
        }
        composable(RouteNavigation.SuffixesLearn.route) {
            SuffixesLearnPage(navController)
        }
        composable(RouteNavigation.SuffixesPractice.route) {
            SuffixesPracticePage(navController)
        }
        // ── Phonics L24: Contractions ──────────────────────────────────────────
        composable(RouteNavigation.ContractionsIntro.route) {
            ContractionsIntroPage(navController)
        }
        composable(RouteNavigation.ContractionsLearn.route) {
            ContractionsLearnPage(navController)
        }
        composable(RouteNavigation.ContractionsPractice.route) {
            ContractionsPracticePage(navController)
        }
        // ── Phonics L25: Consonant + -le ──────────────────────────────────────
        composable(RouteNavigation.ConsonantLeIntro.route) {
            ConsonantLeIntroPage(navController)
        }
        composable(RouteNavigation.ConsonantLeLearn.route) {
            ConsonantLeLearnPage(navController)
        }
        composable(RouteNavigation.ConsonantLePractice.route) {
            ConsonantLePracticePage(navController)
        }
        // ── Phonics L26: Compound Words ───────────────────────────────────────
        composable(RouteNavigation.CompoundWordsIntro.route) {
            CompoundWordsIntroPage(navController)
        }
        composable(RouteNavigation.CompoundWordsLearn.route) {
            CompoundWordsLearnPage(navController)
        }
        composable(RouteNavigation.CompoundWordsPractice.route) {
            CompoundWordsPracticePage(navController)
        }
        // ── Phonics L27: Syllable Division ────────────────────────────────────
        composable(RouteNavigation.SyllableDivisionIntro.route) {
            SyllableDivisionIntroPage(navController)
        }
        composable(RouteNavigation.SyllableDivisionLearn.route) {
            SyllableDivisionLearnPage(navController)
        }
        composable(RouteNavigation.SyllableDivisionPractice.route) {
            SyllableDivisionPracticePage(navController)
        }
        // ── Phonics L28: Sight Words ──────────────────────────────────────────
        composable(RouteNavigation.StarWordsIntro.route) {
            StarWordsIntroPage(navController)
        }
        composable(RouteNavigation.StarWordsLearn.route) {
            StarWordsLearnPage(navController)
        }
        composable(RouteNavigation.StarWordsPractice.route) {
            StarWordsPracticePage(navController)
        }
        // Age Category 5 to 7
        composable(RouteNavigation.AgeGroup5to7.route) {
            AgeGroup5to7Page(navController)
        }
        composable(RouteNavigation.VocabularyBuilding.route) {
            VocabularyBuildingPage(navController) { type, title ->
                navController.navigate(
                    RouteNavigation.VocabularyDetail.vocabularyDetail(type, title)
                )
            }
        }
        composable(
            route = RouteNavigation.VocabularyDetail.route,
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type") ?: "animals"
            val title = backStackEntry.arguments?.getString("title") ?: "Animals"
            VocabularyBuildingDetailPage(
                categoryType = type,
                title = title,
                navController = navController
            )
        }
        composable(RouteNavigation.ColoringWord.route) {
            ColoringWordPage(navController)
        }
        composable(RouteNavigation.WordMatchImage.route) {
            WordMatchImagePage(navController)
        }
        composable(RouteNavigation.ListenAndSelectWord.route) {
            ListenAndSelectWordPage(navController)
        }
        composable(RouteNavigation.MissingLetterMedium.route) {
            MissingLetterPage57(navController, DifficultyLevel57.MEDIUM)
        }
        composable(RouteNavigation.WordJigsaw.route) {
            DragDropWordPage57(navController, DifficultyLevel57.MEDIUM)
        }
        composable(RouteNavigation.ArticlesAAn.route) {
            ArticlesAAnPage(navController)
        }
        composable(RouteNavigation.ArticlesAAnExample.route) {
            ArticlesAAnExamplePage(navController)
        }
        composable(RouteNavigation.SightWords.route) {
            SightWordsPage(navController)
        }
        composable(RouteNavigation.ArticleChoice.route) {
            ArticleChoicePage(navController)
        }
        composable(RouteNavigation.SightWordChoice.route) {
            SightWordChoicePage(navController)
        }

        // Age Category 6 to 8
        composable(RouteNavigation.AgeGroup6to8.route) {
            AgeGroup6to8Page(navController)
        }
        composable(
            route = RouteNavigation.SentenceUnitList.route,
            arguments = listOf(
                navArgument("screenType") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val screenType = backStackEntry.arguments?.getString("screenType")
                ?: UnitSelectionScreen.READ_AND_LISTEN_SENTENCE.name
            SentenceUnitPage(
                screenType = getUnitSelectionScreen(screenType),
                navController = navController
            )
        }
        composable(
            route = RouteNavigation.SentenceLessonList.route,
            arguments = listOf(
                navArgument("screenType") { type = NavType.StringType },
                navArgument("unit") { type = NavType.StringType },
                navArgument("level") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val screenType = backStackEntry.arguments?.getString("screenType")
                ?: UnitSelectionScreen.READ_AND_LISTEN_SENTENCE.name
            val unit = backStackEntry.arguments?.getString("unit") ?: SentenceUnit.PLAY_AND_FUN.name
            val level = backStackEntry.arguments?.getString("level") ?: SentenceLevel.EASY.name
            SentenceLessonPage(
                screenType = getUnitSelectionScreen(screenType),
                unit = getSentenceUnit(unit),
                level = getSentenceLevel(level),
                navController = navController
            )
        }
        composable(
            RouteNavigation.ReadAndListen.route,
            arguments = listOf(
                navArgument("screenType") { type = NavType.StringType },
                navArgument("lessonData") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val screenType = backStackEntry.arguments?.getString("screenType")
                ?: UnitSelectionScreen.READ_AND_LISTEN_SENTENCE.name
            val lessonData = backStackEntry.arguments?.getString("lessonData")
            lessonData?.let {
                val data = Gson().fromJson(lessonData, ReadSentenceItemNew::class.java)
                ReadAndListenPage(getUnitSelectionScreen(screenType), data, navController)
            }
        }
        composable(
            RouteNavigation.OneWordAnswer.route,
            arguments = listOf(
                navArgument("screenType") { type = NavType.StringType },
                navArgument("lessonData") { type = NavType.StringType },
                navArgument("level") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val screenType = backStackEntry.arguments?.getString("screenType")
                ?: UnitSelectionScreen.READ_AND_LISTEN_SENTENCE.name
            val lessonData = backStackEntry.arguments?.getString("lessonData")
            val level = backStackEntry.arguments?.getString("level") ?: SentenceLevel.EASY.name
            lessonData?.let {
                val data = Gson().fromJson(lessonData, ReadSentenceItemNew::class.java)
                OneWordAnswerPage(getUnitSelectionScreen(screenType), data, level = getSentenceLevel(level), navController)
            }
        }
        composable(
            RouteNavigation.FillTheMissingWord.route,
            arguments = listOf(
                navArgument("screenType") { type = NavType.StringType },
                navArgument("lessonData") { type = NavType.StringType },
                navArgument("level") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val screenType = backStackEntry.arguments?.getString("screenType")
                ?: UnitSelectionScreen.READ_AND_LISTEN_SENTENCE.name
            val lessonData = backStackEntry.arguments?.getString("lessonData")
            val level = backStackEntry.arguments?.getString("level") ?: SentenceLevel.EASY.name
            lessonData?.let {
                val data = Gson().fromJson(lessonData, ReadSentenceItemNew::class.java)
                FillTheMissingWordPage(getUnitSelectionScreen(screenType), data, getSentenceLevel(level), navController)
            }
        }
        composable(RouteNavigation.ChooseTheRightSentence.route) {
            ChooseTheRightSentencePage(navController)
        }
        composable(
            route = RouteNavigation.MatchThePicture.route,
            arguments = listOf(
                navArgument("unit") { type = NavType.StringType },
                navArgument("level") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val unit = backStackEntry.arguments?.getString("unit") ?: SentenceUnit.PLAY_AND_FUN.name
            val level = backStackEntry.arguments?.getString("level") ?: SentenceLevel.EASY.name
            MatchThePicturePage(
                unit = getSentenceUnit(unit),
                level = getSentenceLevel(level),
                navController = navController
            )
        }
        composable(
            route = RouteNavigation.WhichSentenceSoundRight.route,
            arguments = listOf(
                navArgument("unit") { type = NavType.StringType },
                navArgument("level") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val unit = backStackEntry.arguments?.getString("unit") ?: SentenceUnit.PLAY_AND_FUN.name
            val level = backStackEntry.arguments?.getString("level") ?: SentenceLevel.EASY.name
            WhichSentenceSoundRightPage(
                unit = getSentenceUnit(unit),
                level = getSentenceLevel(level),
                navController = navController
            )
        }
        composable(
            route = RouteNavigation.FindTheCorrectWriting.route,
            arguments = listOf(
                navArgument("unit") { type = NavType.StringType },
                navArgument("level") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val unit = backStackEntry.arguments?.getString("unit") ?: SentenceUnit.PLAY_AND_FUN.name
            val level = backStackEntry.arguments?.getString("level") ?: SentenceLevel.EASY.name
            FindTheCorrectWritingPage(
                unit = getSentenceUnit(unit),
                level = getSentenceLevel(level),
                navController = navController
            )
        }
        composable(
            route = RouteNavigation.SentenceCheck.route,
            arguments = listOf(
                navArgument("unit") { type = NavType.StringType },
                navArgument("level") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val unit = backStackEntry.arguments?.getString("unit") ?: SentenceUnit.PLAY_AND_FUN.name
            val level = backStackEntry.arguments?.getString("level") ?: SentenceLevel.EASY.name
            SentenceCheckPage(
                unit = getSentenceUnit(unit),
                level = getSentenceLevel(level),
                navController = navController
            )
        }
        composable(
            route = RouteNavigation.SentenceBuilder.route,
            arguments = listOf(
                navArgument("unit") { type = NavType.StringType },
                navArgument("level") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val unit = backStackEntry.arguments?.getString("unit") ?: SentenceUnit.PLAY_AND_FUN.name
            val level = backStackEntry.arguments?.getString("level") ?: SentenceLevel.EASY.name
            SentenceBuilderPage(
                unit = getSentenceUnit(unit),
                level = getSentenceLevel(level),
                navController = navController
            )
        }
        composable(RouteNavigation.GrammarBasic.route) {
            GrammarBasicPage(navController)
        }
        composable(RouteNavigation.GrammarBasicNoun.route) {
            NounLessonPage(navController)
        }
        composable(RouteNavigation.GrammarBasicNounPractice.route) {
            NounPracticePage(navController)
        }
        composable(RouteNavigation.GrammarBasicVerb.route) {
            VerbLessonPage(navController)
        }
        composable(RouteNavigation.GrammarBasicVerbPractice.route) {
            VerbPracticePage(navController)
        }
        composable(RouteNavigation.GrammarBasicAdjectives.route) {
            AdjectivesLessonPage(navController)
        }
        composable(RouteNavigation.GrammarBasicAdjectivesPractice.route) {
            AdjectivesPracticePage(navController)
        }
        composable(RouteNavigation.GrammarBasicPronouns.route) {
            PronounLessonPage(navController)
        }
        composable(RouteNavigation.GrammarBasicPronounsPractice.route) {
            PronounPracticePage(navController)
        }

        // ── AgeGroup 5–7: Opposite Words ────────────────────────────────────
        composable(RouteNavigation.OppositeWords.route) {
            OppositeWordsPage(navController)
        }
        composable(RouteNavigation.OppositeWordActivities.route) {
            OppositeWordActivitiesPage(navController)
        }
        composable(
            route = RouteNavigation.MatchOpposites.route,
            arguments = listOf(navArgument("difficulty") { type = NavType.StringType })
        ) { backStackEntry ->
            val difficulty = backStackEntry.arguments?.getString("difficulty")
                ?: OppositeDifficulty.EASY.name
            MatchOppositesPage(
                difficulty = OppositeDifficulty.valueOf(difficulty),
                navController = navController
            )
        }
        composable(
            route = RouteNavigation.ChooseOpposite.route,
            arguments = listOf(navArgument("difficulty") { type = NavType.StringType })
        ) { backStackEntry ->
            val difficulty = backStackEntry.arguments?.getString("difficulty")
                ?: OppositeDifficulty.EASY.name
            ChooseCorrectOppositePage(
                difficulty = OppositeDifficulty.valueOf(difficulty),
                navController = navController
            )
        }

        // ── AgeGroup 5–7: Singular & Plural ─────────────────────────────────
        composable(RouteNavigation.SingularPlural.route) {
            SingularPluralPage(navController)
        }
        composable(RouteNavigation.SingularPluralActivities.route) {
            SingularPluralActivitiesPage(navController)
        }
        composable(RouteNavigation.MatchSingularPlural.route) {
            MatchSingularPluralPage(navController)
        }
        composable(RouteNavigation.ChooseSingularPluralForm.route) {
            ChooseSingularPluralFormPage(navController)
        }
        composable(RouteNavigation.SpotWrongPlural.route) {
            SpotWrongPluralPage(navController)
        }

        composable(RouteNavigation.SightWordSpeed.route) {
            SightWordSpeedPage(navController)
        }

        // ── AgeGroup 6–8: Mixed Grammar Challenge ───────────────────────────
        composable(RouteNavigation.MixedGrammarChallenge.route) {
            MixedGrammarChallengePage(navController)
        }
        composable(RouteNavigation.MixedGrammarBeginner.route) {
            MixedGrammarBeginnerMenuPage(navController)
        }
        composable(RouteNavigation.TapTheWord.route) {
            TapTheWordPage(navController)
        }
        composable(RouteNavigation.GrammarMultipleChoice.route) {
            GrammarMultipleChoicePage(navController)
        }
        composable(RouteNavigation.DragToGrammarBucket.route) {
            DragToGrammarBucketPage(navController)
        }
        composable(RouteNavigation.MixedGrammarAdvanced.route) {
            MixedGrammarAdvancedMenuPage(navController)
        }
        composable(RouteNavigation.FixTheSentence.route) {
            FixTheSentencePage(navController)
        }
        composable(RouteNavigation.GrammarSentenceBuilder.route) {
            GrammarSentenceBuilderPage(navController)
        }
        composable(RouteNavigation.GrammarFillTheBlanks.route) {
            GrammarFillTheBlanksPage(navController)
        }
    }
}
