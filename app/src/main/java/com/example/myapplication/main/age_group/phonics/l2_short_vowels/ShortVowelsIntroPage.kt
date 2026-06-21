package com.example.myapplication.main.age_group.phonics.l2_short_vowels

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.min
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.phonics.l2_short_vowels.view_model.shortVowelData
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens28
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled

@Composable
fun ShortVowelsIntroPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.peachCoral, shape = KidsFloatingShape.sparkles)

        BoxWithConstraints(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            val screenW = maxWidth
            val screenH = maxHeight

            Row(modifier = Modifier.fillMaxSize()) {

                // ── LEFT: info panel ─────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .weight(0.52f)
                        .fillMaxHeight()
                ) {
                    BackButtonWithText(title = "Level 2", onBackClick = { navController.popBackStack() })

                    Spacer(modifier = Modifier.weight(1f))

                    Column(
                        modifier = Modifier.padding(horizontal = Dimens24),
                        verticalArrangement = Arrangement.spacedBy(Dimens20)
                    ) {
                        Text(
                            text = "Short Vowels",
                            style = MaterialTheme.typography.headlineLarge.scaled(),
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFB71C1C)
                        )

                        // Vowel tiles row
                        Row(horizontalArrangement = Arrangement.spacedBy(Dimens10)) {
                            val tileSize = min(screenH * 0.08f, screenW * 0.06f)
                            shortVowelData.forEach { v ->
                                val color = Color(v.colorHex)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(tileSize)
                                        .background(color, RoundedCornerShape(tileSize * 0.22f))
                                ) {
                                    Text(
                                        text = v.vowel,
                                        style = MaterialTheme.typography.titleLarge.scaled(),
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        // Description rows
                        Column(verticalArrangement = Arrangement.spacedBy(Dimens8)) {
                            IntroDescRow(icon = "🔤", text = "5 vowels: A, E, I, O, U")
                            IntroDescRow(icon = "👂", text = "Tap each vowel to hear its sound")
                            IntroDescRow(icon = "✨", text = "See example words with each vowel")
                        }

                        // Example anchor words
                        Row(horizontalArrangement = Arrangement.spacedBy(Dimens8)) {
                            listOf("ant", "egg", "ink", "ox", "up").forEach { word ->
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFFFEBEE), RoundedCornerShape(Dimens8))
                                        .padding(horizontal = Dimens10, vertical = Dimens6)
                                ) {
                                    Text(
                                        text = word,
                                        style = MaterialTheme.typography.labelLarge.scaled(),
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFB71C1C)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                }

                // ── RIGHT: start card ─────────────────────────────────────────
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(0.48f)
                        .fillMaxHeight()
                        .padding(horizontal = Dimens24)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Dimens24),
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.88f), RoundedCornerShape(Dimens20))
                            .padding(Dimens28)
                    ) {
                        Image(
                            painter = painterResource(R.drawable._mascot_),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(screenH * 0.25f)
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(Dimens8)
                        ) {
                            Text(
                                text = "Ready to learn?",
                                style = MaterialTheme.typography.titleLarge.scaled(),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB71C1C)
                            )
                            Text(
                                text = "Tap each vowel to hear\nits sound and see examples!",
                                style = MaterialTheme.typography.bodyMedium.scaled(),
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }

                        KidsActionButton(
                            text = "Start Learning",
                            icon = Icons.AutoMirrored.Filled.ArrowForward,
                            type = ButtonType.BLUE,
                            isIconStart = false,
                            isSmall = true,
                            onClick = { navController.navigate(RouteNavigation.ShortVowelsLearn.route) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun IntroDescRow(icon: String, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, style = MaterialTheme.typography.bodyLarge)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.scaled(),
            color = Color(0xFF37474F)
        )
    }
}
