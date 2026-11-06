package com.example.myapplication.main.age_group.phonics.worksheets

import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground

/**
 * One worksheet, full screen, pinch-to-zoom, with the level's other sheets a swipe
 * away. Mirrors iOS `WorksheetViewerView`. (Double-tap-to-zoom from iOS is deferred so
 * single taps always reach the picture words for audio.)
 */
@Composable
fun WorksheetViewerPage(levelRaw: String, startId: String, navController: NavController) {
    val level = WorksheetLevelKey.from(levelRaw)
    val specs = remember(level) { WorksheetCatalog.worksheets(level) }
    if (specs.isEmpty()) return

    val vm: WorksheetViewModel = hiltViewModel()
    val startIndex = specs.indexOfFirst { it.id == startId }.coerceAtLeast(0)
    val pagerState = rememberPagerState(initialPage = startIndex) { specs.size }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.skyLavender, shape = KidsFloatingShape.sparkles)

        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            BackButtonWithText(
                title = specs.getOrNull(pagerState.currentPage)?.kind?.title ?: "Worksheet",
                onBackClick = { navController.popBackStack() }
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                ZoomableWorksheet(
                    spec = specs[page],
                    onWordTap = { word, letter -> vm.playWord(word, letter) }
                )
            }
        }
    }
}

@Composable
private fun ZoomableWorksheet(
    spec: WorksheetSpec,
    onWordTap: (word: String, letter: String) -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val transformState = rememberTransformableState { zoomChange, panChange, _ ->
        scale = (scale * zoomChange).coerceIn(1f, 4f)
        offset = if (scale <= 1.01f) Offset.Zero else offset + panChange
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .transformable(transformState),
        contentAlignment = Alignment.Center
    ) {
        ScaledWorksheet(
            spec = spec,
            tappable = true,
            onWordTap = onWordTap,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                }
        )
    }
}
