package com.example.myapplication.main.age_group.phonics.l5_short_vowel_rules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l5_short_vowel_rules.view_model.spellingRulesData
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.PhonicsIntroBtnConfig
import com.example.myapplication.main.common.PhonicsIntroRightPanel
import com.example.myapplication.main.common.kidsGlassCard
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled

@Composable
fun ShortVowelRulesIntroPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.oceanBlue, shape = KidsFloatingShape.waves)

        BoxWithConstraints(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            val screenH = maxHeight

            Row(modifier = Modifier.fillMaxSize()) {

                // ── LEFT: info panel ──────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .weight(0.52f)
                        .fillMaxHeight()
                ) {
                    BackButtonWithText(
                        title = "Level 5",
                        onBackClick = { navController.popBackStack() }
                    )

                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = Dimens20, vertical = Dimens12),
                        verticalArrangement = Arrangement.spacedBy(Dimens12)
                    ) {
                        Text(
                            text = "Short Vowel Spelling Rules",
                            style = MaterialTheme.typography.titleLarge.scaled(),
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0D47A1)
                        )

                        // Rule badges row 1
                        Row(horizontalArrangement = Arrangement.spacedBy(Dimens8)) {
                            val rule0 = spellingRulesData[0]
                            val rule1 = spellingRulesData[1]
                            RuleBadge(
                                emoji = rule0.emoji,
                                label = "${rule0.name}\n${rule0.pattern}",
                                color = rule0.color,
                                modifier = Modifier.weight(1f)
                            )
                            RuleBadge(
                                emoji = rule1.emoji,
                                label = "${rule1.name}\n${rule1.pattern}",
                                color = rule1.color,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Rule badges row 2
                        Row(horizontalArrangement = Arrangement.spacedBy(Dimens8)) {
                            val rule2 = spellingRulesData[2]
                            val rule3 = spellingRulesData[3]
                            RuleBadge(
                                emoji = rule2.emoji,
                                label = "${rule2.name}\n${rule2.pattern}",
                                color = rule2.color,
                                modifier = Modifier.weight(1f)
                            )
                            RuleBadge(
                                emoji = rule3.emoji,
                                label = "${rule3.name}\n${rule3.pattern}",
                                color = rule3.color,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Rule badges row 3 — X Rule + W Rule
                        if (spellingRulesData.size > 5) {
                            Row(horizontalArrangement = Arrangement.spacedBy(Dimens8)) {
                                val rule4 = spellingRulesData[4]
                                val rule5 = spellingRulesData[5]
                                RuleBadge(
                                    emoji = rule4.emoji,
                                    label = "${rule4.name}\n${rule4.pattern}",
                                    color = rule4.color,
                                    modifier = Modifier.weight(1f)
                                )
                                RuleBadge(
                                    emoji = rule5.emoji,
                                    label = "${rule5.name}\n${rule5.pattern}",
                                    color = rule5.color,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // Description bullets
                        Column(
                            modifier = Modifier.padding(top = Dimens4),
                            verticalArrangement = Arrangement.spacedBy(Dimens10)
                        ) {
                            BulletRow(
                                icon = Icons.Default.Book,
                                color = Color(0xFF00897B),
                                text = "${spellingRulesData.size} essential spelling rules for short vowel words"
                            )
                            BulletRow(
                                icon = Icons.Default.RecordVoiceOver,
                                color = Color(0xFFF57C00),
                                text = "Detailed explanations with comparisons — learn WHY each rule works"
                            )
                            BulletRow(
                                icon = Icons.Default.Hearing,
                                color = Color(0xFF7B1FA2),
                                text = "Tap any example word to hear it pronounced"
                            )
                            BulletRow(
                                icon = Icons.Default.Lightbulb,
                                color = Color(0xFFF9A825),
                                text = "Memory tips and highlighted word endings for each rule"
                            )
                        }
                    }
                }

                // ── RIGHT: start card ─────────────────────────────────────────
                PhonicsIntroRightPanel(
                    screenHeight = screenH,
                    strokeColor = Color(0xFF0D47A1),
                    title = "Ready to learn the rules?",
                    titleColor = Color(0xFF0D47A1),
                    descLine1 = "Pick a rule, read the explanation,",
                    descLine2 = "then tap examples to hear them!",
                    learnButton = PhonicsIntroBtnConfig(
                        text = "Start Learning",
                        icon = Icons.Default.ArrowCircleRight,
                        type = ButtonType.GREEN,
                        onClick = { navController.navigate(RouteNavigation.ShortVowelRulesLearn.route) }
                    ),
                    practiceButton = PhonicsIntroBtnConfig(
                        text = "Practice",
                        icon = Icons.Default.CheckCircle,
                        type = ButtonType.BLUE,
                        onClick = { navController.navigate(RouteNavigation.ShortVowelRulesPractice.route) }
                    ),
                    listenButton = PhonicsIntroBtnConfig(
                        text = "Listen",
                        icon = Icons.Default.Hearing,
                        type = ButtonType.TEAL,
                        onClick = { navController.navigate(RouteNavigation.PhonicsListen.createRoute("shortVowelRules")) }
                    ),
                    modifier = Modifier.weight(0.48f).fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun RuleBadge(
    emoji: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = color.copy(alpha = 0.35f))
            .background(color.copy(alpha = 0.08f), RoundedCornerShape(Dimens12))
            .padding(horizontal = Dimens12, vertical = Dimens8)
    ) {
        Text(
            text = emoji,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.scaled(),
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
private fun BulletRow(icon: ImageVector, color: Color, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens10),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(Dimens20).padding(top = Dimens2)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.scaled(),
            color = Color(0xFF0D47A1)
        )
    }
}
