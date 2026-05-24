package com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class LetterTracingUiState(
    val mode: LetterMode = LetterMode.UPPERCASE,
    val currentIndex: Int = 0,
    val strokeIndex: Int = 0,
    val progressIndex: Int = 0,
    val isOnStroke: Boolean = false,
    val drawnPoints: List<Offset> = emptyList(),
    val finishedStrokes: List<List<Offset>> = emptyList(),
    val isWaitingForNextStroke: Boolean = false,
)

enum class LetterMode(val title: String) {
    UPPERCASE("Uppercase"),
    LOWERCASE("Lowercase");

    fun displayString(index: Int): String {
        return when (this) {
            UPPERCASE -> {
                ('A' + index).toString()
            }
            LOWERCASE -> {
                val upper = ('A' + index)
                val lower = ('a' + index)
                "$upper$lower"
            }
        }
    }
}

val letterPalettes: List<List<Color>> = listOf(
    // A – Ant        (reddish-brown → avoid red/orange)
    listOf(Color(0xFFFFB300), Color(0xFF43A047), Color(0xFFFB8C00), Color(0xFF00ACC1), Color(0xFF028576)),
    // B – Bat        (dark/black → any bright)
    listOf(Color(0xFF8D6253), Color(0xFFD81B60),Color(0xFF48721C),Color(0xFF21308F),Color(0xFFAB221F)),
    // C – Cat        (orange → avoid orange)
    listOf(Color(0xFF3949AB), Color(0xFF8E24AA), Color(0xFF00ACC1), Color(0xFF774CCE), Color(0xFF028576)),
    // D – Drum       (red → avoid red)
    listOf(Color(0xFF3949AB), Color(0xFFECC720), Color(0xFF43A047), Color(0xFF059BAD), Color(0xFF8E24AA)),
    // E – Elephant   (gray → any color works)
    listOf(Color(0xFFE53935), Color(0xFFFF7043), Color(0xFF3949AB), Color(0xFF8E24AA), Color(0xFF43A047)),
    // F – Fish       (blue → avoid blue/cyan)
    listOf(Color(0xFFE53935), Color(0xFF7CB342), Color(0xFFFF7043), Color(0xFF8E24AA), Color(0xFF5E35B1)),
    // G – Goat       (white/tan → avoid yellow)
    listOf(Color(0xFFE53935), Color(0xFF3949AB), Color(0xFF8E24AA), Color(0xFFFB8C00), Color(0xFF028576)),
    // H – Hat        (dark → any bright)
    listOf(Color(0xFF3949AB), Color(0xFFECC720), Color(0xFF653670), Color(0xFF00ACC1), Color(0xFFD81B60)),
    // I – Icecream   (pink/pastel → avoid pink)
    listOf(Color(0xFF3949AB), Color(0xFF8E24AA), Color(0xFF028576), Color(0xFF512DA8), Color(0xFF067C8A)),
    // J – Joker      (colorful → cool tones)
    listOf(Color(0xFF4457CB), Color(0xFF028576), Color(0xFF8E24AA), Color(0xFF512DA8), Color(0xFF43A047)),
    // K – Kiwi       (brown/green → avoid green)
    listOf(Color(0xFF3949AB), Color(0xFF8E24AA), Color(0xFFE53935), Color(0xFFD81B60), Color(0xFF09850F)),
    // L – Lion       (orange/gold → avoid orange/yellow)
    listOf(Color(0xFF3949AB), Color(0xFF8E24AA), Color(0xFF067C8A), Color(0xFF7CB342), Color(0xFF6939E0)),
    // M – Monkey     (brown → avoid brown)
    listOf(Color(0xFFF8A742), Color(0xFFC848EA), Color(0xFF0CB014), Color(0xFF1E88E5), Color(0xFF00897B)),
    // N – Nest       (brown → avoid brown/orange)
    listOf(Color(0xFFFFB300), Color(0xFFDA6844), Color(0xFF067C8A), Color(0xFFE53935), Color(0xFF29BE2F)),
    // O – Orange     (orange → avoid orange/yellow)
    listOf(Color(0xFF3949AB), Color(0xFF8E24AA), Color(0xFF09850F), Color(0xFF028576), Color(0xFF512DA8)),
    // P – Parrot     (green/red → avoid green/red)
    listOf(Color(0xFF6777DC), Color(0xFF98DC51), Color(0xFF21B9CB), Color(0xFF724BD2), Color(0xFFECC720)),
    // Q – Queen      (purple/gold → avoid purple/yellow)
    listOf(Color(0xFF88D934), Color(0xFFA91BD0), Color(0xFF43A047), Color(0xFF00ACC1), Color(0xFF028576)),
    // R – Rose       (red/pink → avoid red/pink)
    listOf(Color(0xFF3850E1), Color(0xFF43A047), Color(0xFF00ACC1), Color(0xFF028576), Color(0xFF512DA8)),
    // S – Sun        (yellow → avoid yellow/orange)
    listOf(Color(0xFF3949AB), Color(0xFF8E24AA), Color(0xFFE53935), Color(0xFF028576), Color(0xFFF4511E)),
    // T – Tiger      (orange/black → avoid orange)
    listOf(Color(0xFFC0CA33), Color(0xFFFDD835), Color(0xFF00ACC1), Color(0xFFC851E7), Color(0xFF028576)),
    // U – Umbrella   (varied → all work)
    listOf(Color(0xFF41505E), Color(0xFF5E4150), Color(0xFF5E5141), Color(0xFF415E5E), Color(0xFF3D573F)),
    // V – Van        (red/blue → avoid red/blue)
    listOf(Color(0xFFECC720), Color(0xFF43A047), Color(0xFF8E24AA), Color(0xFF06717E), Color(0xFF028576)),
    // W – Watermelon (red/green → avoid red/green)
    listOf(Color(0xFF3949AB), Color(0xFF8E24AA), Color(0xFF00ACC1), Color(0xFFFF7347), Color(0xFF9A6E61)),
    // X – Xylophone  (colorful → cool tones)
    listOf(Color(0xFF8E24AA), Color(0xFF512DA8), Color(0xFF028576), Color(0xFF438880), Color(0xFF3949AB)),
    // Y – Yoga       (varied → all work)
    listOf(Color(0xFF6E2A29), Color(0xFF263172), Color(0xFF27692A), Color(0xFF642275), Color(0xFF7A3722)),
    // Z – Zebra      (black/white → any color pops)
    listOf(Color(0xFF7C4544), Color(0xFF3B447E), Color(0xFF257529), Color(0xFF8E24AA), Color(0xFF25636B)),
)