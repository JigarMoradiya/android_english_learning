package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.presentation.model.activities_age_6_8_grammar_basics
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens50
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.extensions.scaled


@Composable
fun GrammarBasicPage(
    navController: NavController
) {

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundUI()
        Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)) {
            BackButtonWithText(
                title = stringResource(R.string.grammarBasics),
                onBackClick = { navController.popBackStack() },
                modifier = Modifier
            )
            Spacer(Modifier.weight(1f))
            BoxWithConstraints(modifier = Modifier.padding(bottom = Dimens50)) {

                val totalSpacing = Dimens16 * 3 // 3 gaps between 4 items
                val horizontalPadding = DeviceInfo.screenHorizontalPadding() + Dimens16
                val itemWidth = (maxWidth - horizontalPadding - totalSpacing) / 4

                LazyRow(
                    contentPadding = PaddingValues(
                        start = DeviceInfo.screenHorizontalPadding(),
                        end = Dimens16,
                        top = Dimens16
                    ),
                    horizontalArrangement = Arrangement.spacedBy(Dimens16)
                ) {

                    items(activities_age_6_8_grammar_basics) { activity ->

                        Column(
                            modifier = Modifier.width(itemWidth)
                                .clip(RoundedCornerShape(Dimens16))
                                .background(activity.txtColor.copy(alpha = 0.15f))
                                .clickable {
                                    AudioPlayerManager.playSoundMenuClick()
                                    navController.navigate(activity.destination)
                                }
                                .padding(vertical = Dimens16),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Image(
                                painter = painterResource(id = activity.img),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)   // makes width == height
                            )

                            Text(
                                text = stringResource(activity.titleRes),
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.headlineMedium.scaled().copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.weight(1f))
        }
    }
}
