package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush as BrushIcon
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.BrushTexture
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.ColoringAlphabetsUiState
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens28
import com.example.myapplication.ui.theme.AppDimens.Dimens32
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.DimensColorCircles
import com.example.myapplication.ui.theme.realGradientBrushList

// Which family of brushes the tray is currently showing. Same 5 tools as
// the reference (crayon / paint / glitter / rainbow / stamp) — each one
// gets its own distinct color set (rainbow uses our real gradients; the
// rest are separate solid palettes so switching tools actually shows
// something different, not the same colors relabeled).
private enum class ColorToolType(val icon: ImageVector, val tint: Color, val texture: BrushTexture) {
    CRAYON(Icons.Filled.Palette, Color(0xFFE67639), BrushTexture.FLAT),
    PAINT(Icons.Filled.BrushIcon, Color(0xFF3674B5), BrushTexture.GRAIN),
    GLITTER(Icons.Filled.AutoAwesome, Color(0xFFD81B60), BrushTexture.SPARKLE),
    RAINBOW(Icons.Filled.Colorize, Color(0xFF8E24AA), BrushTexture.FLAT),
    STAMP(Icons.Filled.Star, Color(0xFF00897B), BrushTexture.DOTTED)
}

// Classic crayon-box colors
private val crayonColors = listOf(
    Color(0xFFEE4035), Color(0xFFF37736), Color(0xFFFDF498),
    Color(0xFF7BC043), Color(0xFF0392CF), Color(0xFF7558A5),
    Color(0xFF8C5A32), Color(0xFF2E2E2E), Color(0xFFF06292)
)

// Richer, deeper paint-tube colors
private val paintColors = listOf(
    Color(0xFFA31621), Color(0xFF0D5C63), Color(0xFF283350),
    Color(0xFF1B512D), Color(0xFFB08A2E), Color(0xFF6A1B4D),
    Color(0xFF274156), Color(0xFFC1440E), Color(0xFF5B7553)
)

// Shimmery metallic tones
private val glitterColors = listOf(
    Color(0xFFFFD700), Color(0xFFC0C0C0), Color(0xFFB76E79),
    Color(0xFFB9F2FF), Color(0xFFE0B0FF), Color(0xFFCD7F32),
    Color(0xFF50C878), Color(0xFFFF66CC), Color(0xFFF5F5F5)
)

// Soft pastel sticker tones
private val stampColors = listOf(
    Color(0xFFFFD1DC), Color(0xFFAEC6CF), Color(0xFFFFF5BA),
    Color(0xFFBFEFBF), Color(0xFFD7BFFF), Color(0xFFFFDAB9),
    Color(0xFFB5EAD7), Color(0xFFC7CEEA), Color(0xFFFFE5EC)
)

// A drawer-style panel attached to the side of the canvas: a scrollable
// tray of swatches on the left, and brush-type category icons on the right
// that switch what the tray shows — same shape as reference coloring apps,
// using system icons as stand-ins until real crayon/brush artwork exists.
@Composable
fun ColoringColorRail(
    state: ColoringAlphabetsUiState,
    onBrushSelect: (Brush, BrushTexture) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTool by remember { mutableStateOf(ColorToolType.CRAYON) }
    val isSelectedEraser = state.isEraser
    val selectedBrush = state.selectedBrush

    fun brushesFor(tool: ColorToolType) = when (tool) {
        ColorToolType.RAINBOW -> realGradientBrushList
        ColorToolType.CRAYON -> crayonColors.map { Brush.linearGradient(listOf(it, it)) }
        ColorToolType.PAINT -> paintColors.map { Brush.linearGradient(listOf(it, it)) }
        ColorToolType.GLITTER -> glitterColors.map { Brush.linearGradient(listOf(it, it)) }
        ColorToolType.STAMP -> stampColors.map { Brush.linearGradient(listOf(it, it)) }
    }

    val trayBrushes = brushesFor(selectedTool)

    // Default tool's brush never got pushed to the ViewModel on first show
    // (only tapping a category or a swatch did), so nothing in the tray
    // matched state.selectedBrush and no swatch looked selected at launch.
    LaunchedEffect(Unit) {
        onBrushSelect(trayBrushes.first(), selectedTool.texture)
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(Dimens16))
            .background(Color.Black.copy(alpha = 0.85f))
            .padding(Dimens6),
        horizontalArrangement = Arrangement.spacedBy(Dimens6),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tray of swatches for the selected brush type
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(vertical = Dimens8),
            verticalArrangement = Arrangement.spacedBy(Dimens8),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            trayBrushes.forEach { brush ->
                val isSelected = !isSelectedEraser && brush == selectedBrush

                val innerSize by animateDpAsState(
                    targetValue = if (isSelected) DimensColorCircles else Dimens28,
                    label = ""
                )
                val alphaValue = if (isSelectedEraser) 0.4f else 1f

                Box(
                    modifier = Modifier.size(DimensColorCircles),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(innerSize)
                            .alpha(alphaValue)
                            .clip(CircleShape)
                            .background(brush)
                            .then(
                                if (isSelected) Modifier.border(Dimens2, Color.White, CircleShape)
                                else Modifier
                            )
                            .clickable { onBrushSelect(brush, selectedTool.texture) }
                    )
                }
            }
        }

        // Brush-type selector
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens8),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ColorToolType.entries.forEach { tool ->
                val isSelected = tool == selectedTool
                Box(
                    modifier = Modifier
                        .size(Dimens32)
                        .clip(CircleShape)
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.White)
                        .clickable {
                            selectedTool = tool
                            // Switching category left the old brush selected even
                            // though it belongs to a different palette, so no swatch
                            // in the new tray showed as selected — pick its first
                            // color right away so one is always highlighted.
                            brushesFor(tool).firstOrNull()?.let { onBrushSelect(it, tool.texture) }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = tool.icon,
                        contentDescription = null,
                        tint = if (isSelected) Color.White else tool.tint,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
