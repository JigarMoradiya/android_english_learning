package com.example.myapplication.main.age_group

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.components.ActivityTileCard
import com.example.myapplication.main.age_group.presentation.model.activities_age_6_8
import com.example.myapplication.main.common.AgeGroup68Background
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.MascotPanel
import com.example.myapplication.main.common.sheets.LocalAccessSheetViewModel
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.buttons.KidsIconButton
import com.example.myapplication.ui.theme.AppDimens.KidIconMedium
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.ToolbarIconSize
import com.example.myapplication.utils.AudioPlayerManager
import kotlinx.coroutines.launch

@Composable
fun AgeGroup6to8Page(navController: NavController) {
    val accessVM = LocalAccessSheetViewModel.current
    val scope = rememberCoroutineScope()

    val screenHeight = with(LocalDensity.current) {
        LocalWindowInfo.current.containerSize.height.toDp()
    }
    val headerHeight = DeviceInfo.screenTopPadding() + Dimens8 + ToolbarIconSize
    val gridOverhead = Dimens16 + Dimens12
    val tileHeight = (screenHeight - headerHeight - gridOverhead) / 2

    // Feeds grid scroll movement to the mascot so it can lean and bounce
    var mascotScrollOffset by remember { mutableFloatStateOf(0f) }
    val mascotScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                mascotScrollOffset += consumed.x + consumed.y
                return Offset.Zero
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AgeGroup68Background()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButtonWithText(
                    title = stringResource(R.string.level3_title),
                    onBackClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                )
                // My Progress entry (item 5.3)
                KidsIconButton(
                    icon = Icons.Rounded.BarChart,
                    onClick = { navController.navigate(RouteNavigation.SentenceProgress.route) },
                    type = ButtonType.PURPLE,
                    size = KidIconMedium,
                    modifier = Modifier.padding(end = Dimens16)
                )
            }

            Row(modifier = Modifier.fillMaxSize()) {
                // ── Left mascot panel (22%) ───────────────────────────────────
                MascotPanel(
                    message = "Sentences\nstart here! ✍️",
                    textColor = Color(0xFF15803D),
                    borderColor = Color(0xFF86EFAC),
                    scrollOffset = { mascotScrollOffset },
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
                        val tileSizeDp = (gridW - hPad * 2 - Dimens12 * 2) / 3
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            contentPadding = PaddingValues(start = hPad, end = hPad, top = Dimens8, bottom = Dimens16),
                            horizontalArrangement = Arrangement.spacedBy(Dimens12),
                            modifier = Modifier
                                .fillMaxSize()
                                .nestedScroll(mascotScrollConnection)
                        ) {
                            items(activities_age_6_8) { activity ->
                                ActivityTileCard(
                                    activity = activity,
                                    tileHeight = tileSizeDp,
                                    showCard = true,
                                    onClick = {
                                        AudioPlayerManager.playSoundMenuClick()
                                        scope.launch {
                                            val allowed = if (activity.moduleId.isNotEmpty())
                                                accessVM.checkAccess(activity.moduleId)
                                            else true
                                            if (allowed) navController.navigate(activity.destination)
                                        }
                                    }
                                )
                            }
                        }
                    } else {
                        LazyHorizontalGrid(
                            rows = GridCells.Fixed(2),
                            contentPadding = PaddingValues(start = hPad, end = hPad, bottom = Dimens16),
                            horizontalArrangement = Arrangement.spacedBy(Dimens12, Alignment.CenterHorizontally),
                            verticalArrangement = Arrangement.spacedBy(Dimens12),
                            modifier = Modifier
                                .fillMaxSize()
                                .nestedScroll(mascotScrollConnection)
                        ) {
                            items(activities_age_6_8) { activity ->
                                ActivityTileCard(
                                    activity = activity,
                                    tileHeight = tileHeight,
                                    showCard = true,
                                    onClick = {
                                        AudioPlayerManager.playSoundMenuClick()
                                        scope.launch {
                                            val allowed = if (activity.moduleId.isNotEmpty())
                                                accessVM.checkAccess(activity.moduleId)
                                            else true
                                            if (allowed) navController.navigate(activity.destination)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
