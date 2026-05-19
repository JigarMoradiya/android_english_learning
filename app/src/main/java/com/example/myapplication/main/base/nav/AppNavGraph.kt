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
import com.example.myapplication.main.age_group.from_3_to_5.arrange_letter_sequence.ArrangeLetterInSequencePage
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.ColoringAlphabetsPage
import com.example.myapplication.main.age_group.from_3_to_5.letter_phonics_sound.LetterPhonicsSoundPage
import com.example.myapplication.main.age_group.from_3_to_5.drag_and_drop_word.DragDropWordPage
import com.example.myapplication.main.age_group.from_3_to_5.fill_blank.FillBlankLettersPage
import com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.LetterRecognitionPage
import com.example.myapplication.main.age_group.from_3_to_5.match_latters.MatchLettersPage
import com.example.myapplication.main.age_group.from_3_to_5.match_letter_with_image.MatchLetterWithImagePage
import com.example.myapplication.main.age_group.from_3_to_5.missing_letter.MissingLetterPage
import com.example.myapplication.main.age_group.from_3_to_5.missing_letter.view_model.DifficultyLevel
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
        // Age Category 3 to 5
        composable(RouteNavigation.AgeGroup3to5.route) {
            AgeGroup3to5Page(navController)
        }
        composable(RouteNavigation.AlphabetTracing.route) {
            AlphabetTracingPage(navController)
        }
        composable(RouteNavigation.LetterRecognition.route) {
            LetterRecognitionPage(navController)
        }
        composable(RouteNavigation.ABCDWithImages.route) {
            ABCDWithImagesPage(navController)
        }
        composable(RouteNavigation.MatchLetters.route) {
            MatchLettersPage(navController)
        }
        composable(RouteNavigation.FillTheBlankLetters.route) {
            FillBlankLettersPage(navController)
        }
        composable(RouteNavigation.ArrangeLetterInSequence.route) {
            ArrangeLetterInSequencePage(navController)
        }
        composable(RouteNavigation.MatchLetterWithImage.route) {
            MatchLetterWithImagePage(navController)
        }
        composable(RouteNavigation.MissingLetterEasy.route) {
            MissingLetterPage(navController, DifficultyLevel.EASY)
        }
        composable(RouteNavigation.DragDropWord.route) {
            DragDropWordPage(navController, DifficultyLevel.EASY)
        }
        composable(RouteNavigation.ColoringAlphabets.route) {
            ColoringAlphabetsPage(navController)
        }
        composable(RouteNavigation.LetterPhonicsSoundRoute.route) {
            LetterPhonicsSoundPage(navController)
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
            MissingLetterPage(navController, DifficultyLevel.MEDIUM)
        }
        composable(RouteNavigation.WordJigsaw.route) {
            DragDropWordPage(navController, DifficultyLevel.MEDIUM)
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
            )
        ) { backStackEntry ->
            val screenType = backStackEntry.arguments?.getString("screenType")
                ?: UnitSelectionScreen.READ_AND_LISTEN_SENTENCE.name
            val lessonData = backStackEntry.arguments?.getString("lessonData")
            lessonData?.let {
                val data = Gson().fromJson(lessonData, ReadSentenceItemNew::class.java)
                FillTheMissingWordPage(getUnitSelectionScreen(screenType), data, navController)
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
