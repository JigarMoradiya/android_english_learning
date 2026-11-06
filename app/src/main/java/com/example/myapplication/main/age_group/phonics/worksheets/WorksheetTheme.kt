package com.example.myapplication.main.age_group.phonics.worksheets

import androidx.compose.ui.graphics.Color

/**
 * Everything that changes the *look* of a sheet. The header and footer layout never
 * change — only these colours and the mascot do. Mirrors iOS `WorksheetTheme`.
 */
data class WorksheetTheme(
    val accent: Color,
    val mascot: String
) {
    companion object {
        /**
         * Every sheet gets its own colour, cycling this palette by position. Ten muted
         * hues so a 26-letter level does not repeat every six pages and the border
         * never shouts over the work.
         */
        val PALETTE = listOf(
            Color(0xFFB85C6E), Color(0xFF5E8C6A), Color(0xFF4A6FA5),
            Color(0xFFC97F4E), Color(0xFF7B6BA8), Color(0xFF3E8E93),
            Color(0xFFBE6A93), Color(0xFF6E8F4A), Color(0xFF56688F),
            Color(0xFFA8763F)
        )

        val MASCOTS = listOf("🐻", "🦊", "🐰", "🦁", "🐼", "🐨")

        /**
         * Assigned by POSITION in the catalogue, never by hashing the slug. [accent] is
         * the level's own colour; the palette is only a fallback for a level with none.
         */
        fun forIndex(i: Int, accent: Color? = null): WorksheetTheme =
            WorksheetTheme(
                accent = accent ?: PALETTE[i % PALETTE.size],
                mascot = MASCOTS[i % MASCOTS.size]
            )
    }
}
