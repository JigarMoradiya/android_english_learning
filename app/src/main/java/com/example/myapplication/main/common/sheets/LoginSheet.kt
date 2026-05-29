package com.example.myapplication.main.common.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens18
import com.example.myapplication.ui.theme.AppDimens.Dimens32
import com.example.myapplication.ui.theme.AppDimens.Dimens40
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.SheetDividerHeight
import com.example.myapplication.ui.theme.AppDimens.SheetDividerWidth
import com.example.myapplication.ui.theme.AppDimens.SheetIconCircle
import com.example.myapplication.ui.theme.AppDimens.SheetIconSizeLg
import com.example.myapplication.ui.theme.AppDimens.ShadowOffsetText
import com.example.myapplication.utils.extensions.scaled

/**
 * Bottom sheet for sign-in.
 * Landscape layout: left = branding, divider, right = sign-in buttons.
 */
@Composable
fun LoginSheet(
    isLoading: Boolean,
    onGoogleSignIn: () -> Unit,
    onAppleSignIn: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // ── Close button ─────────────────────────────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(Dimens12)
                .size(Dimens32)
                .clip(CircleShape)
                .background(Color(0xFFF0F0F0))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color(0xFF666666),
                modifier = Modifier.size(Dimens18)
            )
        }

        // ── Main content ─────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimens16, vertical = Dimens8),
            horizontalArrangement = Arrangement.spacedBy(Dimens16),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ── Left: branding ───────────────────────────────────────
            Column(
                modifier = Modifier.weight(0.35f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(SheetIconCircle)
                        .clip(CircleShape)
                        .background(Color(0xFFEFF6FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🎓", fontSize = SheetIconSizeLg)
                }

                Spacer(modifier = Modifier.height(Dimens6))

                Box {
                    Text(
                        text = "Join the Fun!",
                        color = Color.Black.copy(alpha = 0.2f),
                        style = MaterialTheme.typography.titleLarge.scaled(),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.offset(ShadowOffsetText, ShadowOffsetText)
                    )
                    Text(
                        text = "Join the Fun!",
                        color = Color(0xFF0074D5),
                        style = MaterialTheme.typography.titleLarge.scaled(),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(Dimens6))

                Text(
                    text = "Sign in to unlock more\nactivities and track\nlearning progress!",
                    color = Color(0xFF666666),
                    style = MaterialTheme.typography.bodySmall.scaled(),
                    textAlign = TextAlign.Center
                )
            }

            // Vertical divider
            Box(
                modifier = Modifier
                    .width(SheetDividerWidth)
                    .height(SheetDividerHeight)
                    .background(Color(0xFFEEEEEE))
            )

            // ── Right: sign-in buttons ───────────────────────────────
            Column(
                modifier = Modifier.weight(0.65f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens12, Alignment.CenterVertically)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color(0xFF0074D5),
                        modifier = Modifier.size(Dimens40)
                    )
                } else {
                    SignInProviderButton(
                        text = "Continue with Google",
                        emoji = "G",
                        emojiBackground = Color(0xFFEA4335),
                        onClick = onGoogleSignIn
                    )

                    SignInProviderButton(
                        text = "Continue with Apple",
                        emoji = "",
                        emojiBackground = Color(0xFF1C1C1C),
                        onClick = onAppleSignIn,
                        isDark = true
                    )
                }
            }
        }
    }
}

// ── Reusable provider button ──────────────────────────────────────────

@Composable
private fun SignInProviderButton(
    text: String,
    emoji: String,
    emojiBackground: Color,
    onClick: () -> Unit,
    isDark: Boolean = false
) {
    val bgColor    = if (isDark) Color(0xFF1C1C1C) else Color.White
    val textColor  = if (isDark) Color.White else Color(0xFF333333)
    val borderColor = if (isDark) Color.Transparent else Color(0xFFDDDDDD)
    val shadowColor = if (isDark) Color(0xFF000000) else Color(0xFF333333)

    Box(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .drawBehind {
                drawRoundRect(
                    color = shadowColor.copy(alpha = 0.15f),
                    size = size,
                    cornerRadius = CornerRadius(100f, 100f),
                    topLeft = Offset(0f, 2.dp.toPx())
                )
            }
            .clip(RoundedCornerShape(100f))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(100f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(horizontal = Dimens16, vertical = Dimens12),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Provider icon circle
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(emojiBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    style = MaterialTheme.typography.bodyMedium.scaled(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(Dimens8))
            Text(
                text = text,
                color = textColor,
                style = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
