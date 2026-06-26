package com.example.myapplication.main.age_group.from_3_to_5.letter_phonics_sound

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.from_3_to_5.letter_phonics_sound.view_model.LetterPhonicsSoundItem
import com.example.myapplication.main.age_group.from_3_to_5.letter_phonics_sound.view_model.LetterPhonicsSoundViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.LetterRecognitionLetterSize
import com.example.myapplication.ui.theme.AppDimens.isTablet
import com.example.myapplication.ui.theme.PrimaryBlue
import com.example.myapplication.ui.theme.PrimaryOrange
import com.example.myapplication.utils.extensions.scaled
import kotlinx.coroutines.delay
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.sheets.LocalAccessSheetViewModel
import com.example.myapplication.ui.theme.AppDimens.FillBlankLetterBoxSize
import kotlinx.coroutines.launch

@Composable
fun LetterPhonicsSoundPage(
    navController: NavController,
    viewModel: LetterPhonicsSoundViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    val accessVM = LocalAccessSheetViewModel.current
    val scope = rememberCoroutineScope()
    val userState by accessVM.userState.collectAsState()

    // Letters K–Z start at index 10 (A=0 … J=9, K=10 … Z=25)
    val lockedFromIndex = 10

    DisposableEffect(Unit) {
        onDispose { viewModel.stopAudio() }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        KidsGradientBackground(gradient = KidsGradient.mintLime, shape = KidsFloatingShape.musicNotes)

        Row(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {

            // ── LEFT: back button + letter grid ──────────────────────────────
            Column(modifier = Modifier.weight(1f)) {

                BackButtonWithText(
                    title = stringResource(R.string.menu_letter_phonics_sound),
                    onBackClick = { navController.popBackStack() }
                )

                Spacer(modifier = Modifier)

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    BoxWithConstraints(
                        modifier = Modifier.padding(start = DeviceInfo.screenHorizontalPadding())
                    ) {
                        val totalCols = if (isTablet) 6 else 7
                        val totalRows = if (isTablet) 5 else 4
                        val spacing = Dimens8

                        val boxSize = min(
                            (maxWidth - spacing * (totalCols - 1)) / totalCols,
                            (maxHeight - spacing * (totalRows - 1)) / totalRows
                        )

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(totalCols),
                            verticalArrangement = Arrangement.spacedBy(spacing),
                            horizontalArrangement = Arrangement.spacedBy(spacing),
                            contentPadding = PaddingValues(vertical = Dimens24),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            itemsIndexed(uiState.letters) { index, item ->

                                val isSelected = uiState.selectedLetter == item.letter
                                val isLocked = index >= lockedFromIndex
                                val showLock = isLocked && !userState.isLoggedIn && !uiState.isDevMode

                                val scale by animateFloatAsState(
                                    targetValue = if (isSelected) 1.15f else 1f,
                                    label = "letterScale"
                                )

                                // Outer box: handles scale only (no clip), so lock badge stays visible
                                Box(
                                    modifier = Modifier
                                        .size(boxSize)
                                        .graphicsLayer { scaleX = scale; scaleY = scale },
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Inner box: clip + background + click
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(Dimens12))
                                            .background(
                                                if (isSelected) PrimaryOrange else PrimaryBlue.copy(alpha = 0.2f)
                                            )
                                            .clickable {
                                                if (isLocked) {
                                                    scope.launch {
                                                        val allowed = accessVM.checkAccess(ModuleID.LETTER_PHONICS_KZ)
                                                        if (allowed) viewModel.onLetterTap(item)
                                                    }
                                                } else {
                                                    viewModel.onLetterTap(item)
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = item.letter,
                                            style = MaterialTheme.typography.headlineLarge.copy(
                                                fontSize = (boxSize.value * 0.7).sp,
                                            ),
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else Color.Black
                                        )
                                    }

                                    // Lock badge — centered on top-right corner (half outside tile)
                                    if (showLock) {
                                        val badgeSize = (boxSize.value * 0.32f).dp
                                        Box(
                                            modifier = Modifier
                                                .size(badgeSize)
                                                .align(Alignment.TopEnd)
                                                .offset(
                                                    x = badgeSize / 3,
                                                    y = -badgeSize / 3
                                                )
                                                .background(Color(0xFFE65100), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Lock,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size((boxSize.value * 0.17f).dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier)
            }

            // ── RIGHT: phonics detail or hint ─────────────────────────────────
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                val item = uiState.selectedItem
                if (item != null) {
                    PhonicsDetailPanel(
                        item = item,
                        animateObjectImage = uiState.animateObjectImage,
                        phonicsAnimationTrigger = uiState.phonicsAnimationTrigger,
                        onAnimationEnd = { viewModel.setAnimateObjectImage(true) },
                    )
                } else {
                    Text(
                        text = stringResource(R.string.tap_a_letter),
                        style = MaterialTheme.typography.headlineLarge.scaled(),
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

// ── Phonics detail panel ──────────────────────────────────────────────────────

@Composable
private fun PhonicsDetailPanel(
    item: LetterPhonicsSoundItem,
    animateObjectImage: Boolean,
    phonicsAnimationTrigger: Int,
    onAnimationEnd: () -> Unit,
) {
    var highlightedIndex by remember { mutableIntStateOf(-1) }

    // Fix: timings are ABSOLUTE offsets from audio start — subtract previous to get inter-word delay
    LaunchedEffect(phonicsAnimationTrigger) {
        highlightedIndex = -1
        val timings = item.highlightTimings ?: return@LaunchedEffect
        var previousMs = 0L
        timings.forEachIndexed { index, timingSec ->
            val targetMs = (timingSec * 1000).toLong()
            delay(targetMs - previousMs)
            previousMs = targetMs
            highlightedIndex = index
        }
        delay(400)
        highlightedIndex = -1
        onAnimationEnd()
    }

    // Infinite wiggle — only applied when animateObjectImage = true
    val infiniteTransition = rememberInfiniteTransition(label = "objectWiggle")
    val wiggleScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse),
        label = "wiggleScale",
    )
    val wiggleRotation by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse),
        label = "wiggleRotation",
    )

    val phonicsWords = remember(item.phonicsSoundText) { item.phonicsSoundText.split(" ") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxHeight()
            .padding(Dimens16)
    ) {

        // "A says"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens8)
        ) {
            Text(
                text = item.letter,
                style = MaterialTheme.typography.displayLarge,
                fontSize = LetterRecognitionLetterSize,
                fontWeight = FontWeight.Bold,
                color = if (highlightedIndex == 0) Color(0xFF7B1FA2) else PrimaryOrange,
                modifier = Modifier.graphicsLayer {
                    scaleX = if (highlightedIndex == 0) 1.2f else 1f
                    scaleY = if (highlightedIndex == 0) 1.2f else 1f
                }
            )
            Text(
                text = "says",
                style = MaterialTheme.typography.headlineMedium.scaled(),
                fontWeight = FontWeight.Medium,
                color = if (highlightedIndex == 1) Color(0xFF7B1FA2) else Color.Black.copy(alpha = 0.8f),
            )
        }

        Spacer(Modifier.height(Dimens8))

        // "Aah Aah Aah"
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens8),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            phonicsWords.forEachIndexed { index, word ->
                val isHighlighted = highlightedIndex == 2 + index
                Text(
                    text = word.lowercase(),
                    style = MaterialTheme.typography.displaySmall.scaled(),
                    fontWeight = FontWeight.SemiBold,
                    color = if (isHighlighted) Color(0xFF7B1FA2) else Color.Black.copy(alpha = 0.5f),
                    modifier = Modifier.graphicsLayer {
                        scaleX = if (isHighlighted) 1.2f else 1f
                        scaleY = if (isHighlighted) 1.2f else 1f
                    }
                )
            }
        }

        Spacer(Modifier.height(Dimens24))

        // "A for Ant"
        val startIndex = 2 + phonicsWords.size
        Text(
            text = item.letter,
            style = MaterialTheme.typography.displayLarge,
            fontSize = LetterRecognitionLetterSize,
            fontWeight = FontWeight.Bold,
            color = if (highlightedIndex == startIndex) Color(0xFF7B1FA2) else PrimaryOrange,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "for",
                style = MaterialTheme.typography.headlineSmall.scaled(),
                fontWeight = FontWeight.Medium,
                color = if (highlightedIndex == startIndex + 1) Color(0xFF7B1FA2) else Color.Black.copy(alpha = 0.8f),
            )
            Text(
                text = " ${item.word}",
                style = MaterialTheme.typography.headlineLarge.scaled(),
                fontWeight = FontWeight.Medium,
                color = if (highlightedIndex == startIndex + 2) Color(0xFF7B1FA2) else Color.Black.copy(alpha = 0.8f),
            )
        }

        Spacer(Modifier.height(Dimens16))

        // Image — same size pattern as LetterRecognitionPage
        getImageResFromWord(item.word)?.let { resId ->
            Image(
                painter = painterResource(resId),
                contentDescription = item.word,
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .graphicsLayer {
                        if (animateObjectImage) {
                            scaleX = wiggleScale
                            scaleY = wiggleScale
                            rotationZ = wiggleRotation
                        }
                    }
            )
        }
    }
}
