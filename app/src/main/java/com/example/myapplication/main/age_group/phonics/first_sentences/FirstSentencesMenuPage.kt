package com.example.myapplication.main.age_group.phonics.first_sentences

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.kidsShadow
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens32
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.extensions.scaled

/**
 * MILESTONE · Read Your First Sentences — the activity menu.
 *
 * Five activities, and PhonicsIntroRightPanel holds three buttons — so the module needs
 * its own menu the way Level 4 does. Without it two of the six screens are built and
 * routed but unreachable.
 *
 * The order is the teaching order: meet the helper words, watch a sentence build, read
 * one yourself, then two games that check you understood rather than just decoded.
 * Keep identical to iOS FirstSentencesMenuView.swift.
 */
private data class FirstSentencesMenuItem(
    val title: String,
    val subtitle: String,
    val emoji: String,
    val color: Color,
    val route: String,
)

private val firstSentencesMenuItems = listOf(
    FirstSentencesMenuItem(
        "Helper Words", "The words you just learn", "✨",
        Color(0xFF8E24AA), RouteNavigation.FirstSentencesHelpers.route
    ),
    FirstSentencesMenuItem(
        "Build a Sentence", "Watch the words line up", "🧩",
        Color(0xFFE65100), RouteNavigation.FirstSentencesBuild.route
    ),
    FirstSentencesMenuItem(
        "Read It Yourself", "Your turn to read out loud", "📖",
        Color(0xFF1565C0), RouteNavigation.FirstSentencesRead.route
    ),
    FirstSentencesMenuItem(
        "Which Picture?", "Show me what it means", "🖼️",
        Color(0xFF00897B), RouteNavigation.FirstSentencesPicture.route
    ),
    FirstSentencesMenuItem(
        "Missing Word", "Which word fits the gap?", "🔍",
        Color(0xFF6A1B9A), RouteNavigation.FirstSentencesMissing.route
    ),
)

@Composable
fun FirstSentencesMenuPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.peachCoral, shape = KidsFloatingShape.sparkles)

        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            BackButtonWithText(
                title = "Read Your First Sentences",
                onBackClick = { navController.popBackStack() }
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens12, Alignment.CenterVertically),
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Dimens32, vertical = Dimens8)
            ) {
                firstSentencesMenuItems.forEach { item ->
                    MenuCard(item) {
                        AudioPlayerManager.playSoundMenuClick()
                        navController.navigate(item.route)
                    }
                }
            }
        }
    }
}

@Composable
private fun MenuCard(item: FirstSentencesMenuItem, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens16),
        modifier = Modifier
            .fillMaxWidth()
            .kidsShadow(color = item.color, shape = RoundedCornerShape(Dimens20), elevation = Dimens4)
            .background(
                Brush.linearGradient(listOf(item.color, item.color.copy(alpha = 0.82f))),
                RoundedCornerShape(Dimens20)
            )
            .clip(RoundedCornerShape(Dimens20))
            .clickable { onClick() }
            .padding(horizontal = Dimens20, vertical = Dimens16)
    ) {
        Text(text = item.emoji, style = MaterialTheme.typography.displaySmall.scaled())

        Column(verticalArrangement = Arrangement.spacedBy(Dimens2), modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = item.subtitle,
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = Color.White.copy(alpha = 0.92f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.size(Dimens4))

        Icon(
            imageVector = Icons.Default.ArrowCircleRight,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(Dimens24)
        )
    }
}
