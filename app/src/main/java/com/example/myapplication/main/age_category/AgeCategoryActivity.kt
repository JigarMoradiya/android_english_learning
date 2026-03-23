package com.example.myapplication.main.age_category

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.main.age_category.presentation.components.MainLearningAgesCategoriesScreen
import com.example.myapplication.main.age_group.AgeGroup3to5Page
import com.example.myapplication.main.age_group.AgeGroup5to7Page
import com.example.myapplication.main.age_group.AgeGroup6to9Page
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.AlphabetTrackingPage
import com.example.myapplication.main.base.BaseActivity
import com.example.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AgeCategoryActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide Status Bar
        WindowCompat.setDecorFitsSystemWindows(window, false)

        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        setContent {
            LearnEnglishApp()
        }
    }

    override fun onResume() {
        super.onResume()
        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.systemBars())
        }
    }
}

@Composable
fun LearnEnglishApp() {
    val navController = rememberNavController()

    MyApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background // will be white if you set it in LightColors
        ) {
            AppNavGraph(navController = navController)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
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
        composable("alphabet_tracking") {
            AlphabetTrackingPage(navController)
        }
    }
}
