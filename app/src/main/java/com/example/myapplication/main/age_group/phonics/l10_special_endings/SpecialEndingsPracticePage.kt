package com.example.myapplication.main.age_group.phonics.l10_special_endings

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.phonics.l10_special_endings.view_model.SpecialEndingsPracticeViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.kidsGlassCapsule
import com.example.myapplication.main.common.kidsGlassCard
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens14
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens32
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun SpecialEndingsPracticePage(
    navController: NavController,
    viewModel: SpecialEndingsPracticeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    val question = viewModel.currentQuestion
    val accentColor = Color(0xFF33691E)

    DisposableEffect(Unit) { onDispose { viewModel.stop() } }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.aquaGreen, shape = KidsFloatingShape.musicNotes)

        if (uiState.isFinished) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens24),
                    modifier = Modifier.kidsGlassCard(cornerRadius = Dimens20, strokeColor = accentColor).padding(Dimens32)
                ) {
                    Image(painterResource(R.drawable._mascot_), null, contentScale = ContentScale.Fit, modifier = Modifier.height(160.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Dimens8)) {
                        Text(if (uiState.score >= viewModel.totalQuestions / 2) "Well done! 🎉" else "Good try! 💪", style = MaterialTheme.typography.displaySmall.scaled(), fontWeight = FontWeight.Bold, color = accentColor)
                        Text("You got ${uiState.score} out of ${viewModel.totalQuestions}", style = MaterialTheme.typography.titleMedium.scaled(), color = Color(0xFF546E7A))
                    }
                    KidsActionButton(text = "Try Again", icon = Icons.Default.ArrowBack, type = ButtonType.GREEN, isIconStart = true, isSmall = true, onClick = { viewModel.restart() })
                }
            }
        } else if (question != null) {
            AnimatedContent(
                targetState = uiState.currentIndex,
                transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                label = "specialEndingPractice"
            ) { _ ->
                BoxWithConstraints(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing).fillMaxSize()) {
                    val screenH = maxHeight
                    Row(modifier = Modifier.fillMaxSize()) {
                        // Left 45%
                        Column(modifier = Modifier.weight(0.45f).fillMaxHeight()) {
                            BackButtonWithText(title = "Special Endings", onBackClick = { navController.popBackStack() })
                            Spacer(modifier = Modifier.weight(1f))
                            Column(verticalArrangement = Arrangement.spacedBy(Dimens16), modifier = Modifier.padding(horizontal = Dimens20)) {
                                // Progress
                                Column(verticalArrangement = Arrangement.spacedBy(Dimens4)) {
                                    Text("Question ${uiState.currentIndex + 1} of ${viewModel.totalQuestions}", style = MaterialTheme.typography.labelMedium.scaled(), color = Color(0xFF546E7A))
                                    Box(modifier = Modifier.fillMaxWidth().height(Dimens8).clip(RoundedCornerShape(Dimens4)).background(Color.White.copy(0.4f))) {
                                        Box(modifier = Modifier.fillMaxWidth((uiState.currentIndex + 1).toFloat() / viewModel.totalQuestions).fillMaxHeight().clip(RoundedCornerShape(Dimens4)).background(Brush.linearGradient(listOf(Color(0xFF66BB6A), accentColor))))
                                    }
                                }
                                // Instruction
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(Dimens10),
                                    modifier = Modifier.fillMaxWidth().kidsGlassCard(cornerRadius = Dimens12, strokeColor = accentColor).padding(Dimens16)
                                ) {
                                    Text("Complete the Word", style = MaterialTheme.typography.titleLarge.scaled(), fontWeight = FontWeight.Bold, color = accentColor)
                                    Text("Which special ending finishes this word?", style = MaterialTheme.typography.bodyMedium.scaled(), color = Color(0xFF546E7A), textAlign = TextAlign.Center)
                                }
                                // Word display (special ending at end: base + BLANK)
                                val shakeOffset = if (uiState.shakeWrong) (sin(System.currentTimeMillis() / 50.0) * 7).roundToInt() else 0
                                Box(modifier = Modifier.fillMaxWidth().kidsGlassCard(cornerRadius = Dimens12, strokeColor = accentColor).padding(Dimens16), contentAlignment = Alignment.Center) {
                                    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(Dimens4)) {
                                        Text(question.word.dropLast(question.endingLength), style = MaterialTheme.typography.displaySmall.scaled(), fontWeight = FontWeight.Bold, color = Color(0xFF263238))
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.offset { IntOffset(shakeOffset, 0) }.background(when { uiState.isCorrect == true -> Color(0xFFC8E6C9); uiState.isCorrect == false -> Color(0xFFFFCDD2); else -> Color(0xFFECEFF1) }, RoundedCornerShape(Dimens8)).padding(horizontal = Dimens8, vertical = Dimens4)
                                        ) {
                                            Text(uiState.selectedAnswer ?: "___", style = MaterialTheme.typography.displaySmall.scaled(), fontWeight = FontWeight.Bold, color = when { uiState.isCorrect == true -> Color(0xFF2E7D32); uiState.isCorrect == false -> Color(0xFFC62828); else -> Color(0xFF90A4AE) })
                                        }
                                    }
                                }
                                // Score
                                Row(horizontalArrangement = Arrangement.spacedBy(Dimens6), verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.kidsGlassCapsule(strokeColor = accentColor).padding(horizontal = Dimens14, vertical = Dimens6)) {
                                    Icon(Icons.Default.Star, null, tint = Color(0xFFF9A825), modifier = Modifier.size(Dimens16))
                                    Text("Score: ${uiState.score}", style = MaterialTheme.typography.bodyMedium.scaled(), fontWeight = FontWeight.Bold, color = accentColor)
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }
                        // Right 55%
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(Dimens20),
                            modifier = Modifier.weight(0.55f).fillMaxHeight().padding(vertical = Dimens20)
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            // Image card
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(horizontal = Dimens20).fillMaxWidth().height(screenH * 0.38f).kidsGlassCard(cornerRadius = Dimens20, strokeColor = accentColor)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Dimens8)) {
                                    val ctx = androidx.compose.ui.platform.LocalContext.current
                                    val imgId = ctx.resources.getIdentifier(question.imageName, "drawable", ctx.packageName)
                                    if (imgId != 0) {
                                        Image(painterResource(imgId), question.imageName, contentScale = ContentScale.Fit, modifier = Modifier.fillMaxWidth().weight(1f).padding(top = Dimens10))
                                    } else {
                                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth().weight(1f)) {
                                            Text(question.word, style = MaterialTheme.typography.displayMedium.scaled(), fontWeight = FontWeight.Bold, color = accentColor)
                                        }
                                    }
                                    Text(question.word.replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.labelMedium.scaled(), color = Color(0xFF78909C), modifier = Modifier.padding(bottom = Dimens8))
                                }
                            }
                            // Options
                            Row(horizontalArrangement = Arrangement.spacedBy(Dimens12), modifier = Modifier.padding(horizontal = Dimens20)) {
                                question.options.forEach { option ->
                                    val answered = uiState.selectedAnswer != null
                                    val selected = uiState.selectedAnswer == option
                                    val isCorrect = option == question.correctEnding
                                    val textColor = when { !answered -> Color(0xFF263238); isCorrect -> Color(0xFF1B5E20); selected -> Color(0xFFB71C1C); else -> Color(0xFF90A4AE) }
                                    val borderColor = when { !answered -> Color(0xFFB0BEC5); isCorrect -> Color(0xFF2E7D32); selected -> Color(0xFFC62828); else -> Color(0xFFB0BEC5).copy(0.5f) }
                                    val fillColor = when { !answered -> Color.Transparent; selected && isCorrect -> Color(0xFFC8E6C9); selected && !isCorrect -> Color(0xFFFFCDD2); isCorrect -> Color(0xFFC8E6C9); else -> Color.White.copy(0.6f) }
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(Dimens4),
                                        modifier = Modifier.weight(1f).scale(if (selected) 1.03f else 1.0f)
                                            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = if (answered) borderColor else accentColor.copy(0.4f))
                                            .background(fillColor, RoundedCornerShape(Dimens12))
                                            .border(2.dp, borderColor, RoundedCornerShape(Dimens12))
                                            .then(if (!answered) Modifier.clickable { viewModel.onAnswerTap(option) } else Modifier)
                                            .padding(vertical = Dimens16, horizontal = Dimens8)
                                    ) {
                                        Text("-${option.uppercase()}", style = MaterialTheme.typography.titleLarge.scaled(), fontWeight = FontWeight.Bold, color = textColor)
                                        if (answered && isCorrect) Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(Dimens20))
                                        else if (answered && selected && !isCorrect) Icon(Icons.Default.CheckCircle, null, tint = Color(0xFFC62828), modifier = Modifier.size(Dimens20))
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
