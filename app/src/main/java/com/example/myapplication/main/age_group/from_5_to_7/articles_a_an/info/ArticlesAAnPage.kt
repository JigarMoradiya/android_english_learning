package com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.info.view_model.ArticlesAAnViewModel
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.ButtonType
import java.util.Locale


@Composable
fun ArticlesAAnPage(
    navController: NavController,
    viewModel: ArticlesAAnViewModel = hiltViewModel()
) {


    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)
        Column(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {
            BackButtonWithText(
                title = stringResource(R.string.articles_a_an),
                onBackClick = { navController.popBackStack() }
            )

            val state = viewModel.uiState

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens16)
            ) {

                // 🔹 LEFT SIDE (GRID 2x2)
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(Dimens8)
                ) {

                    val items = state.examples.take(4)

                    items.chunked(2).forEach { rowItems ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f), // 🔥 equal height rows
                            horizontalArrangement = Arrangement.spacedBy(Dimens8)
                        ) {

                            rowItems.forEach { item ->

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable {
                                            viewModel.speak("${item.article}, ${item.word}")
                                        }
                                        .padding(Dimens8),
                                    verticalArrangement = Arrangement.SpaceBetween, // 🔥 KEY
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    // 🔹 IMAGE (TOP)
                                    val res = getImageResFromWord(item.word)
                                    res?.let {
                                        Image(
                                            painter = painterResource(id = it),
                                            contentDescription = null,
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier
                                                .weight(1f) // 🔥 take top space
                                        )
                                    }
                                    Spacer(Modifier.height(Dimens4))
                                    // 🔹 TEXT (BOTTOM)
                                    Text(
                                        text = "${item.article} ${item.word}".lowercase(Locale.ROOT),
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                            }

                            // If odd items, fill empty space
                            if (rowItems.size < 2) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                // 🔹 RIGHT SIDE (GUIDE + BUTTON)
                Column(
                    modifier = Modifier
                        .weight(1.4f)
                        .padding(start = Dimens16),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Column(
                        modifier = Modifier
                            .background(Color(0xFFCAE4F6), RoundedCornerShape(12.dp))
                            .padding(Dimens16)
                    ) {

                        Text(
                            text = stringResource(R.string.learning_guide),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center //
                        )

                        Spacer(Modifier.height(Dimens8))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Dimens8),
                            modifier = Modifier.clickable {
                                viewModel.speak("Use a, before words that start with, a consonant sound.")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = null
                            )

                            Text(
                                text = buildAnnotatedString {
                                    append("1. Use ")

                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("'a'")
                                    }

                                    append(" before words that start with a ")

                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("consonant sound.")
                                    }
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Spacer(Modifier.height(Dimens4))

                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Example:")
                                }

                                append(" a cat, a dog, a ball.")
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(Modifier.height(Dimens12))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.clickable {
                                viewModel.speak("Use an, before words that start with, a vowel sound, (a, e, i, o, u).")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = null
                            )

                            Text(
                                text = buildAnnotatedString {
                                    append("2. Use ")

                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("'an'")
                                    }

                                    append(" before words that start with a ")

                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("vowel sound")
                                    }

                                    append(" (a, e, i, o, u).")
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Spacer(Modifier.height(Dimens4))

                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Example:")
                                }

                                append(" an apple, an orange, an ice cream.")
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(Modifier.height(Dimens16))

                    KidsActionButton(
                        text = stringResource(R.string.see_more_examples),
                        icon = Icons.AutoMirrored.Filled.List,
                        type = ButtonType.BLUE,
                        onClick = {
                            navController.navigate(RouteNavigation.ArticlesAAnExample.route)
                        }
                    )
                }
            }
        }
    }
}
