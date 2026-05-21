package com.example.myapplication.main.age_group.from_5_to_7.singular_plural.match_form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_3_to_5.match_letter_with_image.components.drawDragConnection
import com.example.myapplication.main.age_group.from_5_to_7.singular_plural.match_form.view_model.MatchSingularPluralViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.CustomPopupView
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.MatchWordBoxHeight
import com.example.myapplication.ui.theme.AppDimens.MatchWordBoxWidth
import com.example.myapplication.ui.theme.AppDimens.isLargeTablet
import com.example.myapplication.ui.theme.AppDimens.isTablet
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.ui.theme.PrimaryGreen
import com.example.myapplication.ui.theme.colorList
import com.example.myapplication.utils.extensions.scaled
import kotlin.collections.set
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground

@Composable
fun MatchSingularPluralPage(
    navController: NavController,
    viewModel: MatchSingularPluralViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // ── Root coordinate holder — same pattern as MatchLetterWithImageContent ──
    var rootCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }

    // Chip positions stored in root-local space (so Canvas lines match exactly)
    val wordFrames     = remember { mutableStateMapOf<String, Rect>() }
    val oppositeFrames = remember { mutableStateMapOf<String, Rect>() }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.skyLavender, shape = KidsFloatingShape.diamonds)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            BackButtonWithText(
                title = stringResource(R.string.match_singular_plural_),
                onBackClick = { navController.popBackStack() }
            )

            Spacer(Modifier.weight(1f))

            // ── Main match area ──────────────────────────────────────────────
            // 🔥 rootCoords must be on THIS Box — Canvas is a direct child here,
            //    so Canvas coordinate space == this Box's local space
            val padding = if(isLargeTablet) 100.dp else if (isTablet) 60.dp else 0.dp
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding, bottom = padding + Dimens12)
                    .onGloballyPositioned { rootCoords = it }
            ) {

                // ── Canvas: all lines in root-local coordinates ───────────────
                Canvas(modifier = Modifier.matchParentSize()) {
                    // Confirmed match lines
                    uiState.matchedOrder.forEachIndexed { idx, word ->
                        val pair      = uiState.leftWords.find { it.singular == word } ?: return@forEachIndexed
                        val startRect = wordFrames[word]               ?: return@forEachIndexed
                        val endRect   = oppositeFrames[pair.plural]  ?: return@forEachIndexed

                        val lineColor = colorList[idx % colorList.size]
                        val start = Offset(startRect.center.x, startRect.bottom)
                        val end   = Offset(endRect.center.x, endRect.top)

                        // Glow layer
                        drawLine(
                            color = lineColor.copy(alpha = 0.25f),
                            start = start, end = end,
                            strokeWidth = 14f,
                            cap = StrokeCap.Round
                        )
                        // Main line
                        drawLine(
                            color = lineColor,
                            start = start, end = end,
                            strokeWidth = 6f,
                            cap = StrokeCap.Round
                        )
                    }

                    // ✅ Drag line
                    drawDragConnection(
                        start = viewModel.dragStart,
                        end = viewModel.dragEnd,
                        color = Color.Black
                    )
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    // ── TOP ROW: draggable word chips ─────────────────────────
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimens12, Alignment.CenterHorizontally)
                    ) {
                        uiState.leftWords.forEachIndexed { index, pair ->
                            val word      = pair.singular
                            val isMatched = uiState.matchedWords.contains(word)

                            Box(contentAlignment = Alignment.BottomCenter) {
                                Text(
                                    text = word,
                                    modifier = Modifier
                                        .width(MatchWordBoxWidth)
                                        .height(MatchWordBoxHeight)
                                        .clip(RoundedCornerShape(Dimens12))
                                        .background(
                                            colorList[index % colorList.size].copy(
                                                alpha = if (isMatched) 0.15f else 0.25f
                                            )
                                        )
                                        .border(
                                            2.dp,
                                            if (isMatched) Color(0xFF2E7D32) else Color.Transparent,
                                            RoundedCornerShape(Dimens12)
                                        )
                                        .alpha(if (isMatched) 0.5f else 1f)
                                        // 🔥 FIX: root-local coords instead of boundsInWindow
                                        .onGloballyPositioned { coords ->
                                            val root = rootCoords ?: return@onGloballyPositioned
                                            val pos = root.localPositionOf(coords, Offset.Zero)
                                            val sz = coords.size
                                            wordFrames[word] = Rect(
                                                left = pos.x,
                                                top = pos.y,
                                                right = pos.x + sz.width,
                                                bottom = pos.y + sz.height
                                            )
                                        }
                                        .pointerInput(word, isMatched) {
                                            if (isMatched) return@pointerInput
                                            detectDragGestures(
                                                onDragStart = { _ ->
                                                    val f = wordFrames[word] ?: return@detectDragGestures
                                                    viewModel.startDrag(
                                                        word,
                                                        Offset(f.center.x, f.bottom)
                                                    )
                                                },
                                                onDrag = { change, dragAmount ->
                                                    change.consume()
                                                    viewModel.updateDrag(dragAmount)
                                                },
                                                onDragEnd = {
                                                    val end = viewModel.dragEnd
                                                    val target = end?.let { pt ->
                                                        oppositeFrames.entries
                                                            .firstOrNull { it.value.contains(pt) }
                                                            ?.key
                                                    }
                                                    viewModel.endDrag(target)
                                                },
                                                onDragCancel = { viewModel.endDrag(null) }
                                            )
                                        }
                                        .padding(horizontal = Dimens8, vertical = Dimens8),
                                    style = MaterialTheme.typography.titleSmall.scaled(),
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    color = Color.Black,
                                    maxLines = 1
                                )
                                // Bottom-centre dot connector anchor
                                Box(
                                    Modifier
                                        .size(6.dp)
                                        .offset(y = 3.dp)
                                        .clip(CircleShape)
                                        .background(Color.DarkGray)
                                )
                            }
                        }
                    }

                    Spacer(Modifier.weight(1f))

                    // ── BOTTOM ROW: opposite drop targets ─────────────────────
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens24),
                        horizontalArrangement = Arrangement.spacedBy(Dimens12, Alignment.CenterHorizontally)
                    ) {
                        uiState.rightWords.forEachIndexed { index, pair ->
                            val opp       = pair.plural
                            val isMatched = viewModel.isOppositeMatched(opp)

                            Box(contentAlignment = Alignment.TopCenter) {
                                // Top-centre dot connector anchor
                                Box(
                                    Modifier
                                        .size(6.dp)
                                        .offset(y = (-3).dp)
                                        .clip(CircleShape)
                                        .background(Color.DarkGray)
                                )
                                Text(
                                    text = opp,
                                    modifier = Modifier
                                        .width(MatchWordBoxWidth)
                                        .height(MatchWordBoxHeight)
                                        .clip(RoundedCornerShape(Dimens12))
                                        .background(
                                            colorList[index % colorList.size].copy(
                                                alpha = if (isMatched) 0.15f else 0.25f
                                            )
                                        )
                                        .border(
                                            2.dp,
                                            if (isMatched) Color(0xFF2E7D32) else Color.Transparent,
                                            RoundedCornerShape(Dimens12)
                                        )
                                        .alpha(if (isMatched) 0.5f else 1f)
                                        // 🔥 FIX: root-local coords
                                        .onGloballyPositioned { coords ->
                                            val root = rootCoords ?: return@onGloballyPositioned
                                            val pos = root.localPositionOf(coords, Offset.Zero)
                                            val sz = coords.size
                                            oppositeFrames[opp] = Rect(
                                                left = pos.x,
                                                top = pos.y,
                                                right = pos.x + sz.width,
                                                bottom = pos.y + sz.height
                                            )
                                        }
                                        .padding(horizontal = Dimens8, vertical = Dimens8),
                                    style = MaterialTheme.typography.titleSmall.scaled(),
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    color = Color.Black,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))
        }

        // ── Completion popup ──────────────────────────────────────────────────
        AnimatedVisibility(
            visible = uiState.showPopup,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            CustomPopupView(
                title = uiState.feedbackTitleRes.takeIf { it != 0 }
                    ?.let { stringResource(it) } ?: "⭐ Well Done!",
                description = stringResource(R.string.you_matched_all_pairs),
                positiveButtonText = stringResource(R.string.continue_to_play),
                negativeButtonText = stringResource(R.string.no_i_want_to_close),
                icon = R.drawable.ic_complete,
                onPositiveTapped = {
                    wordFrames.clear()
                    oppositeFrames.clear()
                    viewModel.loadPairs()
                },
                onNegativeTapped = {
                    viewModel.closePopup()
                    navController.popBackStack()
                }
            )
        }
    }
}

