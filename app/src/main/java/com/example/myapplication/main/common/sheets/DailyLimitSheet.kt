package com.example.myapplication.main.common.sheets

import androidx.compose.foundation.background
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
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.ShadowOffsetText
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.utils.extensions.scaled

/**
 * Bottom sheet shown when a user hits their daily attempt limit.
 *
 * - Guest user  → CTA "Login Now"  (more attempts after login)
 * - Free user   → CTA "Get Premium" (unlimited with subscription)
 */
@Composable
fun DailyLimitSheet(
    canUnlockWithLogin: Boolean,
    onLoginClick: () -> Unit,
    onPremiumClick: () -> Unit,
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

        // ── Main content — landscape: left illustration + right text ─
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimens24, vertical = Dimens8),
            horizontalArrangement = Arrangement.spacedBy(Dimens20),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ── Left: emoji illustration ─────────────────────────────
            Column(
                modifier = Modifier.weight(0.35f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Clock emoji in a colored circle
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFF3E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "⏰", fontSize = 40.sp)
                }
                Spacer(modifier = Modifier.height(Dimens8))

                // Stars decoration
                Text(text = "⭐ ⭐ ⭐", fontSize = 18.sp)
            }

            // ── Right: text + CTA ────────────────────────────────────
            Column(
                modifier = Modifier.weight(0.65f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {

                // Title with shadow
                Box {
                    Text(
                        text = "Daily Limit Reached!",
                        color = Color.Black.copy(alpha = 0.2f),
                        style = MaterialTheme.typography.headlineSmall.scaled(),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.offset(ShadowOffsetText, ShadowOffsetText)
                    )
                    Text(
                        text = "Daily Limit Reached!",
                        color = Color(0xFFE65100),
                        style = MaterialTheme.typography.headlineSmall.scaled(),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(Dimens8))

                Text(
                    text = if (canUnlockWithLogin)
                        "You've used all your free attempts for today.\nSign in to get more attempts! 🚀"
                    else
                        "You've used all your attempts for today.\nGo Premium for unlimited access! 🌟",
                    color = Color(0xFF555555),
                    style = MaterialTheme.typography.bodyMedium.scaled(),
                    fontWeight = FontWeight.Normal,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(Dimens16))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens12),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (canUnlockWithLogin) {
                        KidsActionButton(
                            text = "Login Now",
                            type = ButtonType.BLUE,
                            onClick = onLoginClick
                        )
                    }

                    KidsActionButton(
                        text = if (canUnlockWithLogin) "Get Premium" else "Go Premium! ⭐",
                        type = ButtonType.ORANGE,
                        onClick = onPremiumClick
                    )
                }
            }
        }
    }
}
