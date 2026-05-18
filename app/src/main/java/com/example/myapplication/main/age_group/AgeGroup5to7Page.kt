package com.example.myapplication.main.age_group

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.components.ActivityTileCard
import com.example.myapplication.main.age_group.presentation.model.activities_age_5_7
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.sheets.LocalAccessSheetViewModel
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.ToolbarIconSize
import com.example.myapplication.utils.AudioPlayerManager
import kotlinx.coroutines.launch

@Composable
fun AgeGroup5to7Page(navController: NavController) {
    val accessVM = LocalAccessSheetViewModel.current
    val scope = rememberCoroutineScope()

    val screenHeight = with(LocalDensity.current) {
        LocalWindowInfo.current.containerSize.height.toDp()
    }
    val headerHeight = DeviceInfo.screenTopPadding() + Dimens8 + ToolbarIconSize
    val gridOverhead = Dimens16 + Dimens12
    val tileHeight = (screenHeight - headerHeight - gridOverhead) * if (DeviceInfo.isTablet) 0.33f else 0.45f

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundUI(isGreenGrassShow = false)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            BackButtonWithText(
                title = stringResource(R.string.level2_title),
                onBackClick = { navController.popBackStack() },
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            )

            LazyHorizontalGrid(
                rows = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    start = DeviceInfo.screenHorizontalPadding(),
                    end = DeviceInfo.screenHorizontalPadding(),
                    bottom = Dimens16
                ),
                horizontalArrangement = Arrangement.spacedBy(Dimens12, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(Dimens12),
                modifier = Modifier.fillMaxSize()
            ) {
                items(activities_age_5_7) { activity ->
                    ActivityTileCard(
                        activity = activity,
                        tileHeight = tileHeight,
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
