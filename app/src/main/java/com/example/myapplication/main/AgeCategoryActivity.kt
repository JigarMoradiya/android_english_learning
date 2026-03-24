package com.example.myapplication.main

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.base.nav.AppNavGraph
import com.example.myapplication.main.base.BaseActivity
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.utilities.TextToSpeechManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AgeCategoryActivity : BaseActivity() {
    @Inject
    lateinit var ttsManager: TextToSpeechManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide Status Bar
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        // Detect notch
        window.decorView.post {
            DeviceInfo.hasNotch = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                window.decorView.rootWindowInsets?.displayCutout != null
            } else {
                false
            }
            // Detect tablet
            DeviceInfo.isTablet = resources.configuration.smallestScreenWidthDp >= 600 && resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
            setContent {
                val navController = rememberNavController()
                MyApplicationTheme {
                    Surface(
                        modifier = Modifier.Companion.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background // will be white if you set it in LightColors
                    ) {
                        AppNavGraph(navController = navController)
                    }
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.systemBars())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsManager.shutdown()
    }
}