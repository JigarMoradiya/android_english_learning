package com.example.myapplication.main.age_group.phonics.worksheets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.navigation.NavController
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground

/**
 * The worksheets for one level, grouped Learn / Reading / Practice / Activity. Each
 * card's preview is the REAL worksheet drawn small — no thumbnail asset to maintain,
 * and it can never fall out of step with the page it opens. Mirrors iOS
 * `LevelWorksheetsView`.
 */
@Composable
fun LevelWorksheetsPage(levelRaw: String, navController: NavController) {
    val level = WorksheetLevelKey.from(levelRaw)
    val specs = remember(level) { WorksheetCatalog.worksheets(level) }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.skyLavender, shape = KidsFloatingShape.sparkles)

        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            BackButtonWithText(
                title = "${level.title} Worksheets",
                onBackClick = { navController.popBackStack() }
            )

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 165.dp),
                contentPadding = PaddingValues(14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = "🖨️ Tap a sheet to open it big. Tap any word to hear it.",
                        style = androidx.compose.ui.text.TextStyle(
                            fontFamily = com.example.myapplication.ui.theme.customFonts,
                            fontWeight = FontWeight.Medium
                        ),
                        color = Color(0xFF546E7A),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                WorksheetKind.entries.forEach { kind ->
                    val items = specs.filter { it.kind == kind }
                    if (items.isNotEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            SectionHeader(kind = kind, count = items.size)
                        }
                        items(items = items, key = { it.id }) { spec ->
                            WorksheetCard(
                                spec = spec,
                                onClick = {
                                    navController.navigate(
                                        RouteNavigation.WorksheetViewer.createRoute(level.raw, spec.id)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(kind: WorksheetKind, count: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = kind.title,
            style = androidx.compose.ui.text.TextStyle(
                fontFamily = com.example.myapplication.ui.theme.customFonts,
                fontWeight = FontWeight.Bold
            ),
            color = kind.tint
        )
        Spacer(Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(kind.tint.copy(alpha = 0.8f))
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = "$count",
                style = androidx.compose.ui.text.TextStyle(
                    fontFamily = com.example.myapplication.ui.theme.customFonts,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )
        }
    }
}

@Composable
private fun WorksheetCard(spec: WorksheetSpec, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.6f))
            .border(1.dp, spec.kind.tint.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ScaledWorksheet(
            spec = spec,
            tappable = false,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(WorksheetMetrics.ASPECT)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, Color.Black.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
        )
        Text(
            text = spec.title,
            style = androidx.compose.ui.text.TextStyle(
                fontFamily = com.example.myapplication.ui.theme.customFonts,
                fontWeight = FontWeight.Bold
            ),
            color = Color(0xFF263238),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
