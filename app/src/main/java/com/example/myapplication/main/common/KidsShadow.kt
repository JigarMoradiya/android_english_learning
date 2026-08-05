package com.example.myapplication.main.common

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * The app's soft, tinted card shadow — the one thing `Modifier.shadow` will not give you
 * by default.
 *
 * Compose's raw `Modifier.shadow(elevation, shape)` has three traps, and hitting any one
 * of them produces the hard grey slab that never looks like the iOS shadow:
 *
 *  1. **`spotColor` defaults to black.** Tinting only `ambientColor` leaves the spot
 *     shadow — the dominant one — pure black.
 *  2. **`clip` defaults to `elevation > 0.dp`**, i.e. true. The shadow is then clipped to
 *     the shape and reads as a hard edge rather than a soft falloff.
 *  3. **Elevation is not a blur radius.** iOS `shadow(radius: 12)` is a 12pt *blur*;
 *     Android elevation 12dp is a physically raised surface and casts a far heavier
 *     shadow. The app's cards live at 2–4dp.
 *
 * This wraps all three correctly, so call sites only choose a colour and a shape.
 * Matches the shadow already baked into [kidsGlassCard] / [kidsGlassCapsule].
 */
fun Modifier.kidsShadow(
    color: Color,
    shape: Shape,
    elevation: Dp = 3.dp,
    /** raise for a card that should read as lifted (a pressed or speaking chip) */
    strength: Float = 1f,
): Modifier = this.shadow(
    elevation = elevation,
    shape = shape,
    clip = false,
    ambientColor = color.copy(alpha = 0.10f * strength),
    spotColor = color.copy(alpha = 0.22f * strength),
)
