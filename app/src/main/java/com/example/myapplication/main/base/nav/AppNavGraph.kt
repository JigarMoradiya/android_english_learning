package com.example.myapplication.main.base.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.main.age_category.MainLearningAgesCategoriesScreen
import com.example.myapplication.main.age_group.AgeGroup3to5Page
import com.example.myapplication.main.age_group.AgeGroup5to7Page
import com.example.myapplication.main.age_group.AgeGroup6to8Page
import com.example.myapplication.main.age_group.from_3_to_5.abcd_with_images.ABCDWithImagesPage
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.AlphabetTracingPage
import com.example.myapplication.main.age_group.from_3_to_5.arrange_letter_sequence.ArrangeLetterInSequencePage
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.ColoringAlphabetsPage
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

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = RouteNavigation.AgeCategories.route
    ) {
        // Home
        composable(RouteNavigation.AgeCategories.route) {
            MainLearningAgesCategoriesScreen(navController)
        }
        // Age Category 3 to 5
        composable(RouteNavigation.AgeGroup3to5.route) {
            AgeGroup3to5Page(navController)
        }
        composable(RouteNavigation.AgeGroup6to8.route) {
            AgeGroup6to8Page()
        }
        // Age Category 3 to 5
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
            MissingLetterPage(navController,DifficultyLevel.EASY)
        }
        composable(RouteNavigation.DragDropWord.route) {
            DragDropWordPage(navController,DifficultyLevel.EASY)
        }
        composable(RouteNavigation.ColoringAlphabets.route) {
            ColoringAlphabetsPage(navController)
        }

        // Age Category 5 to 7
        composable(RouteNavigation.AgeGroup5to7.route) {
            AgeGroup5to7Page(navController)
        }
        composable(RouteNavigation.VocabularyBuilding.route) {
            VocabularyBuildingPage(navController){ type, title ->
                navController.navigate(
                    RouteNavigation.VocabularyDetail.vocabularyDetail(type,title)
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
            val type = backStackEntry.arguments?.getString("type")?:"animals" // new or edit
            val title = backStackEntry.arguments?.getString("title")?:"Animals" // new or edit

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
            MissingLetterPage(navController,DifficultyLevel.MEDIUM)
        }
        composable(RouteNavigation.WordJigsaw.route) {
//            WordJigsawPage(navController)
            DragDropWordPage(navController,DifficultyLevel.MEDIUM)
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
    }
}
