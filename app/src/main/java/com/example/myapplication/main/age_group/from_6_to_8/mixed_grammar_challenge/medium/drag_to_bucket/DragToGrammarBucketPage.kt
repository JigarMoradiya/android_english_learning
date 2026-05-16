package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.medium.drag_to_bucket

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.data.generation.loader.WordDragItem
import com.example.myapplication.data.generation.loader.WrongCorrection
import com.example.myapplication.data.model.WordType
import com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.medium.drag_to_bucket.view_model.DragToGrammarBucketViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DragToGrammarBucketPage(
    navController: NavController,
    viewModel: DragToGrammarBucketViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var rootCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { rootCoords = it }
    ) {
        BackgroundUI(isGreenGrassShow = false)

        // ── Result screen ─────────────────────────────────────────────────────
        if (uiState.isCompleted) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("🎉 All Done!", style = MaterialTheme.typography.headlineMedium.scaled(), fontWeight = FontWeight.Black)
                Spacer(Modifier.height(Dimens8))
                Text("Score: ${uiState.score} / ${uiState.questions.size}", style = MaterialTheme.typography.titleLarge.scaled(), fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(Dimens24))
                Row(horizontalArrangement = Arrangement.spacedBy(Dimens16)) {
                    KidsActionButton(text = "Play Again", type = ButtonType.POSITIVE, onClick = { viewModel.restart() })
                    KidsActionButton(text = "Go Back", type = ButtonType.NEGATIVE, onClick = { navController.popBackStack() })
                }
            }
            return@Box
        }

        val question = uiState.currentQuestion ?: return@Box

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            // ── Header ────────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButtonWithText(
                    title = "Grammar Challenge",
                    modifier = Modifier.weight(1f),
                    onBackClick = { navController.popBackStack() }
                )
                Box(
                    modifier = Modifier
                        .padding(end = Dimens8)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF7B2D8B).copy(alpha = 0.15f))
                        .padding(horizontal = Dimens12, vertical = Dimens4)
                ) {
                    Text(
                        text = "Q ${uiState.currentIndex + 1} / ${uiState.questions.size}",
                        style = MaterialTheme.typography.bodyMedium.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7B2D8B)
                    )
                }
                AnimatedVisibility(visible = uiState.showNext) {
                    KidsActionButton(
                        text = if (uiState.isLastIndex) "Check Result" else "Next",
                        icon = if (uiState.isLastIndex) null else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        isIconStart = false,
                        type = if (uiState.isLastIndex) ButtonType.POSITIVE else ButtonType.ORANGE,
                        isSmall = true,
                        modifier = Modifier.padding(end = Dimens16),
                        onClick = { viewModel.moveToNext() }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Dimens16),
                verticalArrangement = Arrangement.spacedBy(Dimens12)
            ) {
                Spacer(Modifier.weight(0.5f))

                // ── Image + Sentence + Instruction ────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens12),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.weight(1f))
                    val context = LocalContext.current
                    val resId = context.resources.getIdentifier(question.imageName, "drawable", context.packageName)
                    if (resId != 0) {
                        Image(
                            painter = painterResource(resId),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(110.dp)
                                .clip(RoundedCornerShape(Dimens8))
                                .shadow(4.dp, RoundedCornerShape(Dimens8))
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(Dimens8)) {
                        Text(
                            text = question.sentence,
                            style = MaterialTheme.typography.bodyLarge.scaled(),
                            fontWeight = FontWeight.Bold,
                            color = Color.Black.copy(alpha = 0.85f)
                        )
                        InstructionBadge()
                    }
                    Spacer(Modifier.weight(1f))
                }

                // ── Buckets ───────────────────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens8, Alignment.CenterHorizontally)
                ) {
                    question.bucketTypes.forEach { type ->
                        BucketCard(
                            type = type,
                            placedWords = uiState.buckets[type] ?: emptyList(),
                            isComplete = uiState.isQuestionComplete,
                            label = viewModel.typeLabel(type),
                            color = viewModel.typeColor(type),
                            isCorrectlyPlaced = viewModel::isCorrectlyPlaced,
                            modifier = Modifier.weight(1f),
                            onPositioned = { coords ->
                                val root = rootCoords ?: return@BucketCard
                                val pos = root.localPositionOf(coords, Offset.Zero)
                                val sz = coords.size
                                viewModel.updateBucketFrame(type, Rect(pos.x, pos.y, pos.x + sz.width, pos.y + sz.height))
                            }
                        )
                    }
                }

                // ── Word pool ─────────────────────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(Dimens8, Alignment.CenterHorizontally)
                ) {
                    uiState.wordPool.forEach { item ->
                        WordChip(
                            word = item.word,
                            isVisible = uiState.draggingItem?.id != item.id,
                            onPositioned = { coords ->
                                val root = rootCoords ?: return@WordChip
                                val pos = root.localPositionOf(coords, Offset.Zero)
                                val sz = coords.size
                                viewModel.updateItemFrame(item.id, Rect(pos.x, pos.y, pos.x + sz.width, pos.y + sz.height))
                            },
                            onDragStart = { viewModel.startDrag(item) },
                            onDrag = { delta -> viewModel.updateDrag(delta) },
                            onDragEnd = { viewModel.endDrag() }
                        )
                    }
                }

                // ── Feedback + corrections ────────────────────────────────────
                AnimatedVisibility(
                    visible = uiState.isQuestionComplete,
                    enter = fadeIn() + slideInVertically { it },
                    exit = fadeOut() + slideOutVertically { it }
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Dimens8)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(Dimens12))
                                .background(if (uiState.isAnswerCorrect) Color(0xFF2E7D32).copy(alpha = 0.12f) else Color(0xFFB71C1C).copy(alpha = 0.12f))
                                .border(1.dp, if (uiState.isAnswerCorrect) Color(0xFF2E7D32).copy(alpha = 0.4f) else Color(0xFFB71C1C).copy(alpha = 0.4f), RoundedCornerShape(Dimens12))
                                .padding(Dimens12),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (uiState.isAnswerCorrect) "⭐ Excellent! All correct!" else "❌ Some words are in the wrong bucket",
                                style = MaterialTheme.typography.bodyMedium.scaled(),
                                fontWeight = FontWeight.Bold,
                                color = if (uiState.isAnswerCorrect) Color(0xFF2E7D32) else Color(0xFFB71C1C),
                                textAlign = TextAlign.Center
                            )
                        }
                        if (!uiState.isAnswerCorrect && uiState.wrongCorrections.isNotEmpty()) {
                            CorrectionsRow(
                                corrections = uiState.wrongCorrections,
                                typeLabel = viewModel::typeLabel,
                                typeColor = viewModel::typeColor
                            )
                        }
                    }
                }

                Spacer(Modifier.weight(0.5f))
            }
        }

        // ── Floating chip ─────────────────────────────────────────────────────
        val dragging = uiState.draggingItem
        val startCenter = uiState.dragStartCenter
        if (dragging != null && startCenter != null) {
            val density = LocalDensity.current
            val dx = with(density) { (startCenter.x + uiState.dragOffset.x).toDp() }
            val dy = with(density) { (startCenter.y + uiState.dragOffset.y).toDp() }
            FloatingWordChip(
                word = dragging.word,
                modifier = Modifier.offset(dx - 40.dp, dy - 20.dp)
            )
        }
    }
}

// ── Bucket Card ───────────────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BucketCard(
    type: WordType,
    placedWords: List<WordDragItem>,
    isComplete: Boolean,
    label: String,
    color: Color,
    isCorrectlyPlaced: (WordDragItem, WordType) -> Boolean,
    modifier: Modifier = Modifier,
    onPositioned: (LayoutCoordinates) -> Unit
) {
    Column(
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(Dimens16))
            .clip(RoundedCornerShape(Dimens16))
            .background(color.copy(alpha = 0.07f))
            .border(2.dp, color.copy(alpha = 0.4f), RoundedCornerShape(Dimens16))
            .padding(Dimens12)
            .onGloballyPositioned { onPositioned(it) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens8)
    ) {
        // Label pill
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(color)
                .padding(horizontal = Dimens12, vertical = Dimens4),
            horizontalArrangement = Arrangement.spacedBy(Dimens4),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = typeIcon(type), contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
            Text(text = label, style = MaterialTheme.typography.bodySmall.scaled(), fontWeight = FontWeight.Bold, color = Color.White)
        }
        // Placed words
        Box(modifier = Modifier.fillMaxWidth().height(50.dp), contentAlignment = Alignment.Center) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(Dimens4, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(Dimens4)
            ) {
                placedWords.forEach { item ->
                    val correct = isCorrectlyPlaced(item, type)
                    val chipColor = if (isComplete) (if (correct) Color(0xFF2E7D32) else Color(0xFFB71C1C)) else color
                    Row(
                        modifier = Modifier.clip(RoundedCornerShape(Dimens8)).background(chipColor.copy(alpha = 0.85f)).padding(horizontal = Dimens8, vertical = Dimens4),
                        horizontalArrangement = Arrangement.spacedBy(Dimens4),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isComplete) Text(if (correct) "✓" else "✗", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(item.word.replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.bodySmall.scaled(), fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }
            }
        }
    }
}

// ── Word Chip ─────────────────────────────────────────────────────────────────

@Composable
private fun WordChip(
    word: String,
    isVisible: Boolean,
    onPositioned: (LayoutCoordinates) -> Unit,
    onDragStart: () -> Unit,
    onDrag: (Offset) -> Unit,
    onDragEnd: () -> Unit
) {
    Box(
        modifier = Modifier
            .onGloballyPositioned { onPositioned(it) }
            .shadow(if (isVisible) 2.dp else 0.dp, RoundedCornerShape(Dimens12))
            .clip(RoundedCornerShape(Dimens12))
            .background(if (isVisible) Color.White else Color.Transparent)
            .border(1.5.dp, if (isVisible) Color.Gray.copy(alpha = 0.3f) else Color.Transparent, RoundedCornerShape(Dimens12))
            .padding(horizontal = Dimens12, vertical = Dimens8)
            .pointerInput(word) {
                detectDragGestures(
                    onDragStart = { onDragStart() },
                    onDrag = { change, dragAmount -> change.consume(); onDrag(dragAmount) },
                    onDragEnd = { onDragEnd() },
                    onDragCancel = { onDragEnd() }
                )
            }
    ) {
        if (isVisible) {
            Text(word.replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.titleSmall.scaled(), fontWeight = FontWeight.SemiBold, color = Color.Black)
        }
    }
}

// ── Floating Chip ─────────────────────────────────────────────────────────────

@Composable
private fun FloatingWordChip(word: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shadow(10.dp, RoundedCornerShape(Dimens12))
            .clip(RoundedCornerShape(Dimens12))
            .background(Color.White)
            .border(2.dp, Color(0xFF5C6BC0).copy(alpha = 0.5f), RoundedCornerShape(Dimens12))
            .padding(horizontal = Dimens12, vertical = Dimens8)
            .scale(1.08f)
    ) {
        Text(word.replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.titleSmall.scaled(), fontWeight = FontWeight.SemiBold, color = Color.Black)
    }
}

// ── Instruction Badge ─────────────────────────────────────────────────────────

@Composable
private fun InstructionBadge() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(Dimens12))
            .background(Color(0xFF5C6BC0).copy(alpha = 0.10f))
            .border(1.5.dp, Color(0xFF5C6BC0).copy(alpha = 0.3f), RoundedCornerShape(Dimens12))
            .padding(horizontal = Dimens12, vertical = Dimens4),
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = Color(0xFF5C6BC0), modifier = Modifier.size(16.dp))
        Text("Drag each word to the correct category", style = MaterialTheme.typography.bodySmall.scaled(), color = Color.Black.copy(alpha = 0.75f))
    }
}

// ── Corrections Row ───────────────────────────────────────────────────────────

@Composable
private fun CorrectionsRow(
    corrections: List<WrongCorrection>,
    typeLabel: (WordType) -> String,
    typeColor: (WordType) -> Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White.copy(alpha = 0.95f))
            .padding(horizontal = Dimens12, vertical = Dimens8),
        horizontalArrangement = Arrangement.spacedBy(Dimens12, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        corrections.forEach { correction ->
            Text(
                text = correction.word.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodySmall.scaled(),
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier.clip(RoundedCornerShape(50)).background(Color.Red.copy(alpha = 0.85f)).padding(horizontal = Dimens8, vertical = 2.dp)
            )
            Text("→", color = Color.Black.copy(alpha = 0.4f), fontSize = 12.sp)
            val color = typeColor(correction.correctType)
            Row(
                modifier = Modifier.clip(RoundedCornerShape(50)).background(color).padding(horizontal = Dimens8, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(Dimens4),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = typeIcon(correction.correctType), contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                Text(typeLabel(correction.correctType), style = MaterialTheme.typography.bodySmall.scaled(), fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

private fun typeIcon(type: WordType): ImageVector = when (type) {
    WordType.NOUN      -> Icons.Default.Image
    WordType.VERB      -> Icons.Default.Edit
    WordType.ADJECTIVE -> Icons.Default.Brush
    WordType.PRONOUN   -> Icons.Default.Person
}
