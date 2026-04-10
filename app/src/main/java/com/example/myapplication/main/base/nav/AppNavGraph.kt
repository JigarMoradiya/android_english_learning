package com.example.myapplication.main.base.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.main.age_category.MainLearningAgesCategoriesScreen
import com.example.myapplication.main.age_group.AgeGroup3to5Page
import com.example.myapplication.main.age_group.AgeGroup5to7Page
import com.example.myapplication.main.age_group.AgeGroup6to8Page
import com.example.myapplication.main.age_group.from_3_to_5.abcd_with_images.ABCDWithImagesPage
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.AlphabetTracingPage
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.ColoringAlphabetsPage
import com.example.myapplication.main.age_group.from_3_to_5.drag_and_drop_word.DragDropWordPage
import com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.LetterRecognitionPage
import com.example.myapplication.main.age_group.from_3_to_5.match_latters.MatchLettersPage
import com.example.myapplication.main.age_group.from_3_to_5.match_letter_with_image.MatchLetterWithImagePage
import com.example.myapplication.main.age_group.from_3_to_5.missing_letter.MissingLetterPage
import com.example.myapplication.main.age_group.from_3_to_5.missing_letter.view_model.DifficultyLevel
import com.example.myapplication.main.age_group.from_5_to_7.article_choice.ArticleChoicePage
import com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.ArticlesAAnPage
import com.example.myapplication.main.age_group.from_5_to_7.coloring_word.ColoringWordPage
import com.example.myapplication.main.age_group.from_5_to_7.listen_and_select_answer.ListenAndSelectWordPage
import com.example.myapplication.main.age_group.from_5_to_7.sight_word_choice.SightWordChoicePage
import com.example.myapplication.main.age_group.from_5_to_7.sight_words.SightWordsPage
import com.example.myapplication.main.age_group.from_5_to_7.vocabulary_building.VocabularyBuildingPage
import com.example.myapplication.main.age_group.from_5_to_7.word_jigsaw.WordJigsawPage
import com.example.myapplication.main.age_group.from_5_to_7.word_match_picture.WordMatchImagePage

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = RouteNavigation.AgeCategories.name
    ) {
        // Home
        composable(RouteNavigation.AgeCategories.name) {
            MainLearningAgesCategoriesScreen(navController)
        }
        // Age Category 3 to 5
        composable(RouteNavigation.AgeGroup3to5.name) {
            AgeGroup3to5Page(navController)
        }
        composable(RouteNavigation.AgeGroup6to8.name) {
            AgeGroup6to8Page()
        }
        // Age Category 3 to 5
        composable(RouteNavigation.AlphabetTracing.name) {
            AlphabetTracingPage(navController)
        }
        composable(RouteNavigation.LetterRecognition.name) {
            LetterRecognitionPage(navController)
        }
        composable(RouteNavigation.ABCDWithImages.name) {
            ABCDWithImagesPage(navController)
        }
        composable(RouteNavigation.MatchLetters.name) {
            MatchLettersPage(navController)
        }
        composable(RouteNavigation.MatchLetterWithImage.name) {
            MatchLetterWithImagePage(navController)
        }
        composable(RouteNavigation.MissingLetterEasy.name) {
            MissingLetterPage(navController,DifficultyLevel.EASY)
        }
        composable(RouteNavigation.DragDropWord.name) {
            DragDropWordPage(navController,DifficultyLevel.EASY)
        }
        composable(RouteNavigation.ColoringAlphabets.name) {
            ColoringAlphabetsPage(navController)
        }

        // Age Category 5 to 7
        composable(RouteNavigation.AgeGroup5to7.name) {
            AgeGroup5to7Page(navController)
        }
        composable(RouteNavigation.VocabularyBuilding.name) {
            VocabularyBuildingPage(navController)
        }
        composable(RouteNavigation.ColoringWord.name) {
            ColoringWordPage(navController)
        }
        composable(RouteNavigation.WordMatchImage.name) {
            WordMatchImagePage(navController)
        }
        composable(RouteNavigation.ListenAndSelectWord.name) {
            ListenAndSelectWordPage(navController)
        }
        composable(RouteNavigation.MissingLetterMedium.name) {
            MissingLetterPage(navController,DifficultyLevel.MEDIUM)
        }
        composable(RouteNavigation.WordJigsaw.name) {
//            WordJigsawPage(navController)
            DragDropWordPage(navController,DifficultyLevel.MEDIUM)
        }
        composable(RouteNavigation.ArticlesAAn.name) {
            ArticlesAAnPage(navController)
        }
        composable(RouteNavigation.SightWords.name) {
            SightWordsPage(navController)
        }
        composable(RouteNavigation.ArticleChoice.name) {
            ArticleChoicePage(navController)
        }
        composable(RouteNavigation.SightWordChoice.name) {
            SightWordChoicePage(navController)
        }
    }
}
