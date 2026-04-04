package com.example.myapplication.main.base.nav

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.layout.LazyLayoutCacheWindow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        // Age Category
        composable(RouteNavigation.AgeGroup3to5.name) {
            AgeGroup3to5Page(navController)
        }
        composable(RouteNavigation.AgeGroup5to7.name) {
            AgeGroup5to7Page()
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
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ComplexList(items: List<String>) {
    val cacheWindow = remember { LazyLayoutCacheWindow(ahead = 150.dp, behind = 100.dp) }
    val listState = rememberLazyListState(cacheWindow = cacheWindow)
    LazyColumn(modifier = Modifier.fillMaxSize(),state = listState) {
        items(items) { item ->
            // With PausableComposition enabled, the runtime can pre-compose
            // these items across multiple frames before they enter the screen.
            ComplexListItem(text = item)
        }
    }
}

@Composable
fun ComplexListItem(text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = text, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            // Nested layouts and multiple elements typically benefit most
            // from incremental preparation.
            repeat(3) {
                Text("Detailed sub-information line $it", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}