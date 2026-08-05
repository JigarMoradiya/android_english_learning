package com.example.myapplication.main.age_group.phonics.first_sentences

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.first_sentences.view_model.FirstSentence
import com.example.myapplication.main.age_group.phonics.first_sentences.view_model.firstSentenceHelperSet
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.PhonicsIntroAudioViewModel
import com.example.myapplication.main.common.PhonicsIntroBtnConfig
import com.example.myapplication.main.common.PhonicsIntroRightPanel
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.extensions.scaled

/**
 * MILESTONE · Read Your First Sentences — screen 1, "What is a sentence?"
 *
 * The child has just finished Level 4 and can read a word. The only genuinely new idea
 * here is that words sit in a ROW and are read left to right, and that the row starts
 * with a capital and ends with a dot. Everything else is Level 4's blending, one step up:
 * say each word, then say them together.
 *
 * Follows the shape of CvcWordsIntroPage so it reads as the next page of the same book.
 * Keep identical to iOS FirstSentencesIntroView.swift.
 */
private val demoLine = listOf("The", "cat", "sat", "on", "a", "mat.")

@Composable
fun FirstSentencesIntroPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.peachCoral, shape = KidsFloatingShape.sparkles)

        BoxWithConstraints(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            val screenH = maxHeight

            Row(modifier = Modifier.fillMaxSize()) {

                // ── LEFT: info panel ─────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .weight(0.52f)
                        .fillMaxHeight()
                ) {
                    // NOT "Level 5" — this is a milestone, and the back title has to say so
                    BackButtonWithText(title = "Milestone", onBackClick = { navController.popBackStack() })

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = Dimens24),
                        verticalArrangement = Arrangement.spacedBy(Dimens20, Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "Read Your First Sentences",
                            style = MaterialTheme.typography.headlineLarge.scaled(),
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFE65100)
                        )

                        SentenceStrip()

                        Column(verticalArrangement = Arrangement.spacedBy(Dimens8)) {
                            IntroRow("📖", "Words sit in a row — read them left to right")
                            IntroRow("🔠", "A sentence starts with a BIG letter")
                            IntroRow("⏹️", "And it ends with a dot")
                            IntroRow("🔊", "Say each word, then say them together")
                        }

                        // key words bold + tinted
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(color = Color(0xFF1565C0), fontWeight = FontWeight.Bold)) { append("Word") }
                                withStyle(SpanStyle(color = Color(0xFF37474F))) { append(" + ") }
                                withStyle(SpanStyle(color = Color(0xFF1565C0), fontWeight = FontWeight.Bold)) { append("word") }
                                withStyle(SpanStyle(color = Color(0xFF37474F))) { append(" + ") }
                                withStyle(SpanStyle(color = Color(0xFF1565C0), fontWeight = FontWeight.Bold)) { append("word") }
                                withStyle(SpanStyle(color = Color(0xFF37474F))) { append(" = a ") }
                                withStyle(SpanStyle(color = Color(0xFFE65100), fontWeight = FontWeight.Bold)) { append("sentence") }
                            },
                            style = MaterialTheme.typography.labelMedium.scaled()
                        )
                    }
                }

                // ── RIGHT: start card ─────────────────────────────────────────
                PhonicsIntroRightPanel(
                    screenHeight = screenH,
                    strokeColor = Color(0xFFEF6C00),
                    title = "Ready to read?",
                    titleColor = Color(0xFFE65100),
                    descLine1 = "Thirty-five sentences,",
                    descLine2 = "made only of words you know!",
                    // one door: the menu. Five activities cannot hang off three buttons, and
                    // splitting them across intro and menu would hide two of them.
                    learnButton = PhonicsIntroBtnConfig(
                        text = "Let's Read",
                        icon = Icons.Default.ArrowCircleRight,
                        type = ButtonType.GREEN,
                        // KidsActionButton already plays the click — a second call here
                        // doubled it
                        onClick = { navController.navigate(RouteNavigation.FirstSentencesMenu.route) }
                    ),
                    modifier = Modifier.weight(0.48f).fillMaxHeight()
                )
            }
        }
    }
}

/** the demo line, every word tappable — the tap-to-play rule applies to words too */
@Composable
private fun SentenceStrip() {
    val introAudioVm: PhonicsIntroAudioViewModel = hiltViewModel()
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(Dimens6),
        verticalArrangement = Arrangement.spacedBy(Dimens8)
    ) {
        demoLine.forEach { word ->
            val plain = FirstSentence.key(word)
            // helper words are a different colour from the first page onward, so
            // "these you just learn" is visible before it is ever said
            val isHelper = firstSentenceHelperSet.contains(plain)
            Box(
                modifier = Modifier
                    .background(
                        if (isHelper) Color(0xFFF3E5F5) else Color(0xFFE3F2FD),
                        RoundedCornerShape(Dimens8)
                    )
                    .clip(RoundedCornerShape(Dimens8))
                    .clickable { introAudioVm.play(plain) }
                    .padding(horizontal = Dimens10, vertical = Dimens6)
            ) {
                Text(
                    text = word,
                    style = MaterialTheme.typography.titleLarge.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = if (isHelper) Color(0xFF8E24AA) else Color(0xFF1565C0)
                )
            }
        }
    }
}

@Composable
private fun IntroRow(icon: String, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, style = MaterialTheme.typography.bodyLarge.scaled())
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.scaled(),
            color = Color(0xFF37474F)
        )
    }
}
