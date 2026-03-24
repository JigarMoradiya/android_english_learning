package com.example.myapplication.main.age_group

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.presentation.model.activities_age_3_5
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.ui.theme.AppDimens
import com.example.myapplication.ui.theme.AppDimens.Dimens16

@Composable
fun AgeGroup3to5Page(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundUI(isGreenGrassShow = false)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            BackButtonWithText(
                title = stringResource(R.string.level1_title),
                onBackClick = { navController.popBackStack() }
            )

            // Grid of activities
            LazyVerticalGrid(
                contentPadding = PaddingValues(start = DeviceInfo.screenPadding(), end = Dimens16, top = Dimens16, bottom = Dimens16),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(AppDimens.Dimens8),   // 👈 8dp vertical spacing
                horizontalArrangement = Arrangement.spacedBy(AppDimens.Dimens8), // 👈 8dp horizontal spacing
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true)
            ) {
                items(activities_age_3_5) { activity ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = AppDimens.Dimens8,
                                shape = RoundedCornerShape(12.dp),
                                clip = false
                            ),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Button(
                            onClick = { navController.navigate(activity.destination) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), // make button use card color
                            elevation = null // remove default button elevation
                        ) {
                            Text(
                                text = stringResource(activity.titleRes),
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )
                        }
                    }
                }

            }
        }
    }
}