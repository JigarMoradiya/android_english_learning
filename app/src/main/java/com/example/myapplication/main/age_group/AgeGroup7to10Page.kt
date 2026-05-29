package com.example.myapplication.main.age_group

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.common.AgeGroup710Background
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.MascotPanel

@Composable
fun AgeGroup7to10Page(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        AgeGroup710Background()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            BackButtonWithText(
                title = stringResource(R.string.level4_title),
                onBackClick = { navController.popBackStack() },
                modifier = Modifier
            )

            Row(modifier = Modifier.fillMaxSize()) {
                // ── Left mascot panel (22%) ───────────────────────────────────
                MascotPanel(
                    message = "Master\ngrammar! 🚀",
                    textColor = Color(0xFF6D28D9),
                    borderColor = Color(0xFFA78BFA),
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.22f)
                )

                // ── Right content (78%) — activities to be added ──────────────
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.78f)
                )
            }
        }
    }
}
