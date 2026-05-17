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
import androidx.compose.material3.Divider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.ShadowOffsetText
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.utils.extensions.scaled

/**
 * Bottom sheet for sign-in.
 * Landscape layout: left = branding / title, right = sign-in buttons.
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
                .size(32.dp)
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
                modifier = Modifier.size(18.dp)
            )
        }

        // ── Main content ─────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimens24, vertical = Dimens8),
            horizontalArrangement = Arrangement.spacedBy(Dimens24),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ── Left: branding ───────────────────────────────────────
            Column(
                modifier = Modifier.weight(0.4f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "🌟", fontSize = 52.sp)

                Spacer(modifier = Modifier.height(Dimens8))

                Box {
                    Text(
                        text = "Join the Fun!",
                        color = Color.Black.copy(alpha = 0.2f),
                        style = MaterialTheme.typography.headlineMedium.scaled(),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.offset(ShadowOffsetText, ShadowOffsetText)
                    )
                    Text(
                        text = "Join the Fun!",
                        color = Color(0xFF0074D5),
                        style = MaterialTheme.typography.headlineMedium.scaled(),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(Dimens8))

                Text(
                    text = "Sign in to unlock more\nactivities and track your\nlearning progress! 🚀",
                    color = Color(0xFF666666),
                    style = MaterialTheme.typography.bodyMedium.scaled(),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }

            // Vertical divider
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(160.dp)
                    .background(Color(0xFFEEEEEE))
            )

            // ── Right: sign-in buttons ───────────────────────────────
            Column(
                modifier = Modifier.weight(0.6f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color(0xFF0074D5),
                        modifier = Modifier.size(40.dp)
                    )
                } else {
                    // Google Sign-In button
                    SignInProviderButton(
                        text = "Continue with Google",
                        emoji = "G",
                        emojiBackground = Color(0xFFEA4335),
                        onClick = onGoogleSignIn
                    )

                    Spacer(modifier = Modifier.height(Dimens12))

                    // Apple Sign-In button
                    SignInProviderButton(
                        text = "Continue with Apple",
                        emoji = "",
                        emojiBackground = Color(0xFF1C1C1C),
                        onClick = onAppleSignIn,
                        isDark = true
                    )

                    Spacer(modifier = Modifier.height(Dimens16))

                    // Divider with OR
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(0.75f)
                    ) {
                        Box(Modifier.weight(1f).height(1.dp).background(Color(0xFFDDDDDD)))
                        Text(
                            text = "  or  ",
                            color = Color(0xFF999999),
                            style = MaterialTheme.typography.labelMedium.scaled()
                        )
                        Box(Modifier.weight(1f).height(1.dp).background(Color(0xFFDDDDDD)))
                    }

                    Spacer(modifier = Modifier.height(Dimens12))

                    Text(
                        text = "Privacy protected · No spam",
                        color = Color(0xFFAAAAAA),
                        style = MaterialTheme.typography.labelSmall.scaled(),
                        textAlign = TextAlign.Center
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
                    fontSize = 13.sp,
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
