package com.example.myapplication.main.age_category.view_model

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.main.age_category.presentation.components.MainLearningAgesCategoriesScreen
import com.example.myapplication.main.age_group.AgeGroup3to5Page
import com.example.myapplication.main.age_group.AgeGroup5to7Page
import com.example.myapplication.main.age_group.AgeGroup6to9Page
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
        composable("age_group_3_5") {
            AgeGroup3to5Page(navController)
        }
        composable("age_group_5_7") {
            AgeGroup5to7Page()
        }
        composable("age_group_6_9") {
            AgeGroup6to9Page()
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