package com.example.myapplication.main.age_group

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.graphics.Brush
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.components.ActivityTileCard
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode
import com.example.myapplication.main.age_group.from_3_to_5.fill_blank.CaseCard
import com.example.myapplication.main.age_group.from_3_to_5.fill_blank.SectionLabel
import com.example.myapplication.main.age_group.presentation.model.activities_age_3_5
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.AgeGroup35Background
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.MascotPanel
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.sheets.LocalAccessSheetViewModel
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.ToolbarIconSize
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.AudioPlayerManager
import kotlinx.coroutines.launch

@Composable
fun AgeGroup3to5Page(
    navController: NavController,
    viewModel: AgeGroup3to5ViewModel = hiltViewModel()
) {
    val accessVM = LocalAccessSheetViewModel.current
    val scope = rememberCoroutineScope()

    val screenHeight = with(LocalDensity.current) {
        LocalWindowInfo.current.containerSize.height.toDp()
    }
    val headerHeight = DeviceInfo.screenTopPadding() + Dimens8 + ToolbarIconSize
    val gridOverhead = Dimens16 + Dimens12
    val tileHeight = (screenHeight - headerHeight - gridOverhead) / 2

    var showArrangeSheet by rememberSaveable { mutableStateOf(false) }
    var arrangeMode by rememberSaveable { mutableStateOf(viewModel.getArrangeMode()) }

    Box(modifier = Modifier.fillMaxSize()) {
        AgeGroup35Background()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            BackButtonWithText(
                title = stringResource(R.string.level1_title),
                onBackClick = { navController.popBackStack() },
                modifier = Modifier
            )

            Row(modifier = Modifier.fillMaxSize()) {
                // ── Left mascot panel (22%) ───────────────────────────────────
                MascotPanel(
                    message = "Let's learn\nletters! 🌈",
                    textColor = Color(0xFFC2530A),
                    borderColor = Color(0xFFFDBA74),
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.22f)
                )

                // ── Right activity grid (78%) ─────────────────────────────────
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.78f)
                ) {
                    val gridW = maxWidth
                    val hPad  = DeviceInfo.screenHorizontalPadding()

                    if (DeviceInfo.isTablet) {
                        // Tablet: vertical scroll, 3 columns, tile size from width
                        val tileSizeDp = (gridW - hPad * 2 - Dimens12 * 2) / 3

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            contentPadding = PaddingValues(
                                start = hPad,
                                end = hPad,
                                bottom = Dimens16
                            ),
                            horizontalArrangement = Arrangement.spacedBy(Dimens12),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(activities_age_3_5) { activity ->
                                ActivityTileCard(
                                    activity = activity,
                                    tileHeight = tileSizeDp,
                                    onClick = {
                                        AudioPlayerManager.playSoundMenuClick()
                                        scope.launch {
                                            val allowed = if (activity.moduleId.isNotEmpty())
                                                accessVM.checkAccess(activity.moduleId)
                                            else true
                                            if (allowed) {
                                                if (activity.moduleId == ModuleID.ARRANGE_LETTER_SEQUENCE) {
                                                    showArrangeSheet = true
                                                } else {
                                                    navController.navigate(activity.destination)
                                                }
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    } else {
                        // Phone: horizontal scroll, 2 rows, tile size from height
                        LazyHorizontalGrid(
                            rows = GridCells.Fixed(2),
                            contentPadding = PaddingValues(
                                start = hPad,
                                end = hPad,
                                bottom = Dimens16
                            ),
                            horizontalArrangement = Arrangement.spacedBy(Dimens12, Alignment.CenterHorizontally),
                            verticalArrangement = Arrangement.spacedBy(Dimens12),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(activities_age_3_5) { activity ->
                                ActivityTileCard(
                                    activity = activity,
                                    tileHeight = tileHeight,
                                    onClick = {
                                        AudioPlayerManager.playSoundMenuClick()
                                        scope.launch {
                                            val allowed = if (activity.moduleId.isNotEmpty())
                                                accessVM.checkAccess(activity.moduleId)
                                            else true
                                            if (allowed) {
                                                if (activity.moduleId == ModuleID.ARRANGE_LETTER_SEQUENCE) {
                                                    showArrangeSheet = true
                                                } else {
                                                    navController.navigate(activity.destination)
                                                }
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Inline sheet overlay
        AnimatedVisibility(
            visible = showArrangeSheet,
            enter = fadeIn(animationSpec = tween(200)),
            exit  = fadeOut(animationSpec = tween(200))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { showArrangeSheet = false }
            ) {
                AnimatedVisibility(
                    visible = showArrangeSheet,
                    enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(320)),
                    exit  = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(260)),
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFD4ECFC),
                                        Color(0xFFEBF7FF),
                                        Color(0xFFF7FCFF)
                                    ),
                                    start = Offset(0f, 0f),
                                    end = Offset(1000f, 1000f)
                                ),
                                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                            )
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { }
                    ) {
                        ArrangeModePickerContent(
                            selectedMode = arrangeMode,
                            onModeSelected = { arrangeMode = it },
                            onStart = {
                                viewModel.saveArrangeMode(arrangeMode)
                                showArrangeSheet = false
                                navController.navigate(
                                    RouteNavigation.ArrangeLetterInSequence.createRoute(arrangeMode.name)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ArrangeModePickerContent(
    selectedMode: LetterMode,
    onModeSelected: (LetterMode) -> Unit,
    onStart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens20),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(Dimens12))
        Box(
            modifier = Modifier
                .width(36.dp)
                .height(4.dp)
                .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(2.dp))
        )
        Spacer(Modifier.height(Dimens12))

        SectionLabel(stringResource(R.string.fill_blank_letter_style))
        Spacer(Modifier.height(Dimens8))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens12)
        ) {
            LetterMode.entries.forEach { mode ->
                CaseCard(
                    mode = mode,
                    isSelected = selectedMode == mode,
                    modifier = Modifier.weight(1f),
                    onClick = { onModeSelected(mode) }
                )
            }
        }

        Spacer(Modifier.height(Dimens20))

        KidsActionButton(
            text = stringResource(R.string.lets_go),
            type = ButtonType.BLUE,
            isSmall = true,
            isIconStart = false,
            onClick = onStart
        )

        Spacer(Modifier.height(Dimens20))
    }
}
