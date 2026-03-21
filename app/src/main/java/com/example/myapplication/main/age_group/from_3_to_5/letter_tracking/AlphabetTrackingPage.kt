package com.example.myapplication.main.age_group.from_3_to_5.letter_tracking

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.min
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.common.AppToolbarDropDownOnRight
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.presentation.components.GuidelineCanvas
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.presentation.components.TracingCanvas
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.view_model.LetterMode
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.view_model.LetterTracingViewModel
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens8

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
            AppToolbarDropDownOnRight(
                title = "Alphabet Tracking",
                currentSelected = viewModel.uiState.mode.title,
                modes = LetterMode.entries.map { it.title },
                onItemChange = { selected ->
                    val mode = LetterMode.entries.first { it.title == selected }
                    viewModel.changeMode(mode)
                },
                onBackClick = { navController.popBackStack() },
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(modifier = Modifier.padding(Dimens16), verticalAlignment = Alignment.Bottom){
                Button(
                    onClick = { viewModel.previous() },
                    shape = RoundedCornerShape(50),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = Dimens4,
                        pressedElevation = Dimens2
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = Dimens16, vertical = Dimens8)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = Color.Black
                    )
                    Spacer(Modifier.width(Dimens4))
                    Text(
                        text = stringResource(R.string.previous),
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = { viewModel.clear() },
                        shape = RoundedCornerShape(50),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = Dimens4,
                            pressedElevation = Dimens2
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = Dimens16, vertical = Dimens8)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = Color.Black
                        )
                        Spacer(Modifier.width(Dimens4))
                        Text(
                            stringResource(R.string.clear),
                            color = Color.Black
                        )
                    }

                    Button(
                        onClick = { viewModel.next() },
                        shape = RoundedCornerShape(50),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = Dimens4,
                            pressedElevation = Dimens2
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = Dimens16, vertical = Dimens8)
                    ) {
                        
                        
                        Text(
                            text = stringResource(id = R.string.next),
                            color = Color.Black
                        )
                        Spacer(Modifier.width(Dimens4))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                }

            }
        }

        // ✅ CENTER AREA (THIS IS IMPORTANT)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = Dimens16),
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
                        shape = RoundedCornerShape(Dimens16),
                        elevation = CardDefaults.cardElevation(Dimens4),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {

                        // 👇 ONLY LINES (no background now)
                        GuidelineCanvas(viewModel)
                    }

                    // 👇 TRACING ON TOP
                    TracingCanvas(viewModel)
                }
            }
        }
    }

}
