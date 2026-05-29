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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens18
import com.example.myapplication.ui.theme.AppDimens.Dimens32
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.SheetDividerHeight
import com.example.myapplication.ui.theme.AppDimens.SheetDividerWidth
import com.example.myapplication.ui.theme.AppDimens.SheetIconCircle
import com.example.myapplication.ui.theme.AppDimens.SheetIconSizeLg
import com.example.myapplication.ui.theme.AppDimens.ShadowOffsetText
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled

/**
 * Bottom sheet shown when a user hits their daily attempt limit.
 *
 * - Guest user  (canUnlockWithLogin = true)  → ONLY "Login Now" button
 * - Free user   (canUnlockWithLogin = false) → ONLY "Go Premium!" button
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

        // ── Main content — landscape: left icon + divider + right text ─
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimens16, vertical = Dimens8),
            horizontalArrangement = Arrangement.spacedBy(Dimens16),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ── Left: icon illustration ─────────────────────────────
            Column(
                modifier = Modifier.weight(0.30f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(SheetIconCircle)
                        .clip(CircleShape)
                        .background(Color(0xFFFFF3E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "⏰", fontSize = SheetIconSizeLg)
                }
                Spacer(modifier = Modifier.height(Dimens6))
                Text(text = "⭐ ⭐ ⭐", style = MaterialTheme.typography.labelLarge.scaled())
            }

            // Vertical divider
            Box(
                modifier = Modifier
                    .width(SheetDividerWidth)
                    .height(SheetDividerHeight)
                    .background(Color(0xFFEEEEEE))
            )

            // ── Right: text + single CTA ────────────────────────────
            Column(
                modifier = Modifier.weight(0.70f),
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
                        "You've used all your free tries for today.\nSign in to get more attempts!"
                    else
                        "You've used all your attempts for today.\nGo Premium for unlimited access!",
                    color = Color(0xFF555555),
                    style = MaterialTheme.typography.bodyMedium.scaled(),
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(Dimens12))

                // Guest → Login Now only  |  Free user → Go Premium only
                if (canUnlockWithLogin) {
                    KidsActionButton(
                        text = "Login Now",
                        type = ButtonType.BLUE,
                        onClick = onLoginClick
                    )
                } else {
                    KidsActionButton(
                        text = "Go Premium!",
                        type = ButtonType.ORANGE,
                        onClick = onPremiumClick
                    )
                }
            }
        }
    }
}
