package com.example.myapplication.main

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.base.BaseActivity
import com.example.myapplication.main.common.sheets.AccessBottomSheetHost
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.data.auth.AuthManager
import com.example.myapplication.utilities.BGMusicManager
import com.example.myapplication.utilities.TextToSpeechManager
import com.example.myapplication.utilities.pref.AppPreferencesHelper
import com.example.myapplication.utils.AudioPlayerManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AgeCategoryActivity : BaseActivity() {
    @Inject
    lateinit var ttsManager: TextToSpeechManager

    @Inject
    lateinit var bgMusicManager: BGMusicManager

    @Inject
    lateinit var authManager: AuthManager

    @Inject
    lateinit var prefs: AppPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Restore previous login/subscription session on every launch
        authManager.restoreSession()

        // Safety net: if Android killed this activity while the Apple Sign-In browser
        // was open, Firebase may have a pending result waiting. Pick it up so the
        // user doesn't have to start over.
        FirebaseAuth.getInstance().pendingAuthResult
            ?.addOnSuccessListener { /* session restored by restoreSession() above */ }
            ?.addOnFailureListener { /* silently ignore — user can try again */ }

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
            DeviceInfo.isLargeTablet = resources.configuration.smallestScreenWidthDp >= 840 && resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
            DeviceInfo.isTablet = resources.configuration.smallestScreenWidthDp >= 600 && resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

            // initialize audio player
            AudioPlayerManager.init(this)

            // Review only (debuggable builds): `--ei onb_start N [--ez onb_done true]` opens the
            // onboarding at page N, optionally already solved, without replaying the flow.
            val debuggable = (applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0
            val onbStart = intent.getIntExtra("onb_start", -1)
            if (debuggable && onbStart >= 0) {
                com.example.myapplication.main.onboarding.OnboardingDebug.force = true
                com.example.myapplication.main.onboarding.OnboardingDebug.startPage = onbStart
                com.example.myapplication.main.onboarding.OnboardingDebug.forceDone = intent.getBooleanExtra("onb_done", false)
            }

            // Show the first-launch tutorial only until the user has seen it once.
            val startDestination = if (prefs.isOnboardingSeen() && !com.example.myapplication.main.onboarding.OnboardingDebug.force) {
                RouteNavigation.AgeCategories.route
            } else {
                RouteNavigation.Onboarding.route
            }

            setContent {
                val navController = rememberNavController()
                MyApplicationTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AccessBottomSheetHost {
                            AppNavGraph(
                                navController = navController,
                                startDestination = startDestination,
                                onOnboardingSeen = { prefs.setOnboardingSeen(true) }
                            )
                        }
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
        bgMusicManager.start()
    }

    override fun onPause() {
        super.onPause()
        bgMusicManager.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        bgMusicManager.stop()
        ttsManager.shutdown()
    }
}