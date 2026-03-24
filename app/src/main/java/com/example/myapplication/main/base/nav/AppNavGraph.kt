package com.example.myapplication.main.base.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.main.age_category.MainLearningAgesCategoriesScreen
import com.example.myapplication.main.age_group.AgeGroup3to5Page
import com.example.myapplication.main.age_group.AgeGroup5to7Page
import com.example.myapplication.main.age_group.AgeGroup6to8Page
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.AlphabetTracingPage
import com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.LetterRecognitionPage

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "age_categories"
    ) {
        // Home
        composable("age_categories") {
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
    }
}