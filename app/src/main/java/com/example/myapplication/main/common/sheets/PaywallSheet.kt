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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.ShadowOffsetText
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.ui.theme.ButtonType
import com.revenuecat.purchases.Package
import com.example.myapplication.utils.extensions.scaled

/**
 * Paywall bottom sheet — shows monthly and yearly subscription options.
 * Landscape layout: two plan cards side by side.
 */
@Composable
fun PaywallSheet(
    packages: List<Package>,
    isLoading: Boolean,
    onPurchase: (Package) -> Unit,
    onRestore: () -> Unit,
    onDismiss: () -> Unit
) {
    // Find monthly and yearly packages from RevenueCat offerings
    val monthlyPackage = packages.firstOrNull {
        it.identifier.contains("monthly", ignoreCase = true) ||
        it.packageType.name.contains("MONTHLY", ignoreCase = true)
    }
    val yearlyPackage = packages.firstOrNull {
        it.identifier.contains("yearly", ignoreCase = true) ||
        it.identifier.contains("annual", ignoreCase = true) ||
        it.packageType.name.contains("ANNUAL", ignoreCase = true)
    }

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimens24, vertical = Dimens4),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── Header ───────────────────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "⭐", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(Dimens8))
                Box {
                    Text(
                        text = "Go Premium!",
                        color = Color.Black.copy(alpha = 0.2f),
                        style = MaterialTheme.typography.headlineSmall.scaled(),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.offset(ShadowOffsetText, ShadowOffsetText)
                    )
                    Text(
                        text = "Go Premium!",
                        color = Color(0xFF0074D5),
                        style = MaterialTheme.typography.headlineSmall.scaled(),
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(Dimens8))
                Text(text = "⭐", fontSize = 22.sp)
            }

            Text(
                text = "Unlock ALL activities · No daily limits · Learn without limits! 🚀",
                color = Color(0xFF666666),
                style = MaterialTheme.typography.bodySmall.scaled(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Dimens12))

            if (isLoading) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF0074D5),
                        modifier = Modifier.size(48.dp)
                    )
                }
            } else {
                // ── Plan cards ───────────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(Dimens16),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Monthly card
                    PlanCard(
                        modifier = Modifier.weight(1f),
                        emoji = "📅",
                        title = "Monthly",
                        price = monthlyPackage?.product?.price?.formatted ?: "--",
                        period = "per month",
                        badge = null,
                        isHighlighted = false,
                        pkg = monthlyPackage,
                        onPurchase = onPurchase
                    )

                    // Yearly card — highlighted as best value
                    PlanCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🏆",
                        title = "Yearly",
                        price = yearlyPackage?.product?.price?.formatted ?: "--",
                        period = "per year",
                        badge = "Best Value!",
                        isHighlighted = true,
                        pkg = yearlyPackage,
                        onPurchase = onPurchase
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens8))

            // ── Restore purchases ────────────────────────────────────
            Text(
                text = "Restore Purchases",
                color = Color(0xFF0074D5),
                style = MaterialTheme.typography.bodySmall.scaled(),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onRestore() }
                    .padding(Dimens8)
            )

            Spacer(modifier = Modifier.height(Dimens4))
        }
    }
}

// ── Plan card ─────────────────────────────────────────────────────────

@Composable
private fun PlanCard(
    modifier: Modifier = Modifier,
    emoji: String,
    title: String,
    price: String,
    period: String,
    badge: String?,
    isHighlighted: Boolean,
    pkg: Package?,
    onPurchase: (Package) -> Unit
) {
    val borderColor = if (isHighlighted) Color(0xFFFF8400) else Color(0xFFDDDDDD)
    val bgColor     = if (isHighlighted) Color(0xFFFFF8F0) else Color(0xFFFAFAFA)

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(bgColor)
                .border(
                    width = if (isHighlighted) 2.dp else 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(Dimens16),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens8)
        ) {
            Text(text = emoji, fontSize = 28.sp)

            Box {
                Text(
                    text = title,
                    color = Color.Black.copy(alpha = 0.15f),
                    style = MaterialTheme.typography.titleMedium.scaled(),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(ShadowOffsetText, ShadowOffsetText)
                )
                Text(
                    text = title,
                    color = if (isHighlighted) Color(0xFFE65100) else Color(0xFF333333),
                    style = MaterialTheme.typography.titleMedium.scaled(),
                    fontWeight = FontWeight.Bold
                )
            }

            // Price
            Text(
                text = price,
                color = Color(0xFF0074D5),
                style = MaterialTheme.typography.headlineMedium.scaled(),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = period,
                color = Color(0xFF999999),
                style = MaterialTheme.typography.labelMedium.scaled()
            )

            KidsActionButton(
                text = "Subscribe",
                type = if (isHighlighted) ButtonType.ORANGE else ButtonType.BLUE,
                onClick = { pkg?.let { onPurchase(it) } },
                disable = pkg == null,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Best Value badge
        if (badge != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 8.dp, y = (-10).dp)
                    .clip(RoundedCornerShape(100f))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFFEEAA47), Color(0xFFFB8C00))
                        )
                    )
                    .padding(horizontal = Dimens8, vertical = Dimens4),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = badge,
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
