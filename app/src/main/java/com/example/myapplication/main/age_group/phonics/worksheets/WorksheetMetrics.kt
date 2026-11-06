package com.example.myapplication.main.age_group.phonics.worksheets

import androidx.compose.ui.graphics.Color

/**
 * The worksheet is drawn on a FIXED canvas and then scaled as one piece to fit the
 * device, rather than laid out responsively — a worksheet is a *page*, not a screen.
 * Its proportions ARE the design. Mirrors iOS `WorksheetMetrics`.
 */
object WorksheetMetrics {
    /** A4 is 210 × 297 mm — ratio 1.4143. 744 × 1052 keeps that to 0.02%. */
    const val PAGE_WIDTH = 744f
    const val PAGE_HEIGHT = 1052f
    val ASPECT = PAGE_WIDTH / PAGE_HEIGHT

    /** Thickness of the decorated border, the same on all four sides. */
    const val BORDER_WIDTH = 14f

    const val MARGIN = 34f
    const val CONTENT_WIDTH = PAGE_WIDTH - MARGIN * 2

    /** The rainbow band under the header. */
    val RAINBOW = listOf(
        Color(0xFFE63946), Color(0xFFF4A261), Color(0xFFFFD166),
        Color(0xFF2A9D8F), Color(0xFF4361EE), Color(0xFF7209B7)
    )
}
