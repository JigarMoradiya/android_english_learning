package com.example.myapplication.main.age_group.from_3_to_5.letter_tracking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.common.AppToolbar
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.presentation.components.GuidelineCanvas
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.presentation.components.TracingCanvas
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.view_model.LetterTracingViewModel

@Composable
fun AlphabetTrackingPage(
    navController: NavController,
    viewModel: LetterTracingViewModel = hiltViewModel()
) {
    Box{
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // ✅ HEADER (fixed)
            AppToolbar(
                title = "Trace Alphabet",
                onBackClick = {
                    navController.popBackStack()
                }
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        // ✅ CENTER AREA (THIS IS IMPORTANT)
        Box(
            modifier = Modifier.fillMaxSize().padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {

            BoxWithConstraints {

                val size = min(maxWidth, maxHeight)

                Box(
                    modifier = Modifier.size(size),
                    contentAlignment = Alignment.Center
                ) {

                    // ✅ CARD BACKGROUND WITH ELEVATION
                    Card(
                        modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {

                        // 👇 ONLY LINES (no background now)
                        GuidelineCanvas()
                    }

                    // 👇 TRACING ON TOP
                    TracingCanvas(viewModel)
                }
            }
        }
    }

}
