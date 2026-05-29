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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens18
import com.example.myapplication.ui.theme.AppDimens.Dimens32
import com.example.myapplication.ui.theme.AppDimens.Dimens40
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.ShadowOffsetText
import com.example.myapplication.ui.theme.AppDimens.SheetDividerHeight
import com.example.myapplication.ui.theme.AppDimens.SheetDividerWidth
import com.example.myapplication.ui.theme.AppDimens.SheetIconCircle
import com.example.myapplication.ui.theme.AppDimens.SheetIconSizeLg
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled
import com.revenuecat.purchases.Package

/**
 * Paywall bottom sheet — landscape layout.
 * Left: branding (crown icon + title + subtitle).
 * Right: vertical plan rows (monthly + yearly).
 */
@Composable
fun PaywallSheet(
    packages: List<Package>,
    isLoading: Boolean,
    onPurchase: (Package) -> Unit,
    onRestore: () -> Unit,
    onDismiss: () -> Unit
) {
    val monthlyPackage = packages.firstOrNull {
        it.identifier.contains("monthly", ignoreCase = true) ||
        it.packageType.name.contains("MONTHLY", ignoreCase = true)
    }
    val yearlyPackage = packages.firstOrNull {
        it.identifier.contains("yearly", ignoreCase = true) ||
        it.identifier.contains("annual", ignoreCase = true) ||
        it.packageType.name.contains("ANNUAL", ignoreCase = true)
    }

    Column (modifier = Modifier.fillMaxSize()) {

        // ── Close button ─────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .padding(end = Dimens12)
                    .size(Dimens32)
                    .clip(CircleShape)
                    .background(Color(0xFFF0F0F0))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onDismiss()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color(0xFF666666),
                    modifier = Modifier.size(Dimens18)
                )
            }
        }

        // ── Main landscape layout ────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimens16, vertical = Dimens8),
            horizontalArrangement = Arrangement.spacedBy(Dimens16),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ── Left: branding ───────────────────────────────────────
            Column(
                modifier = Modifier.weight(0.32f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(SheetIconCircle)
                        .clip(CircleShape)
                        .background(Color(0xFFFFF8F0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "👑", fontSize = SheetIconSizeLg)
                }

                Spacer(modifier = Modifier.height(Dimens6))

                Box {
                    Text(
                        text = "Go Premium!",
                        color = Color.Black.copy(alpha = 0.2f),
                        style = MaterialTheme.typography.titleLarge.scaled(),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.offset(ShadowOffsetText, ShadowOffsetText)
                    )
                    Text(
                        text = "Go Premium!",
                        color = Color(0xFFE65100),
                        style = MaterialTheme.typography.titleLarge.scaled(),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(Dimens6))

                Text(
                    text = "Unlock ALL activities\nNo daily limits\nLearn without limits!",
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

            // ── Right: plan rows + restore ───────────────────────────
            Column(
                modifier = Modifier.weight(0.68f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color(0xFF0074D5),
                        modifier = Modifier.size(Dimens40)
                    )
                } else {
                    PlanRowCard(
                        emoji = "📅",
                        title = "Monthly",
                        price = monthlyPackage?.product?.price?.formatted ?: "--",
                        period = "/ month",
                        isHighlighted = false,
                        badge = null,
                        pkg = monthlyPackage,
                        onPurchase = onPurchase
                    )

                    Spacer(modifier = Modifier.height(Dimens16))

                    PlanRowCard(
                        emoji = "🏆",
                        title = "Yearly",
                        price = yearlyPackage?.product?.price?.formatted ?: "--",
                        period = "/ year",
                        isHighlighted = true,
                        badge = "Best Value!",
                        pkg = yearlyPackage,
                        onPurchase = onPurchase
                    )

                    Spacer(modifier = Modifier.height(Dimens8))

                    Text(
                        text = stringResource(R.string.restore_purchases),
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
                }
            }
        }
    }
}

// ── Horizontal plan row card ──────────────────────────────────────────

@Composable
private fun PlanRowCard(
    emoji: String,
    title: String,
    price: String,
    period: String,
    isHighlighted: Boolean,
    badge: String?,
    pkg: Package?,
    onPurchase: (Package) -> Unit
) {
    val borderColor = if (isHighlighted) Color(0xFFFF8400) else Color(0xFFDDDDDD)
    val bgColor     = if (isHighlighted) Color(0xFFFFF8F0) else Color(0xFFFAFAFA)

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(bgColor)
                .border(
                    width = if (isHighlighted) 2.dp else 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = Dimens12, vertical = Dimens10),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens8)
        ) {
            Text(text = emoji, style = MaterialTheme.typography.headlineLarge.scaled())

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = if (isHighlighted) Color(0xFFE65100) else Color(0xFF333333),
                    style = MaterialTheme.typography.titleSmall.scaled(),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = price + " " + period,
                    color = Color(0xFF0074D5),
                    style = MaterialTheme.typography.bodyMedium.scaled(),
                    fontWeight = FontWeight.Bold
                )
            }

            KidsActionButton(
                text = "Subscribe",
                type = if (isHighlighted) ButtonType.ORANGE else ButtonType.BLUE,
                onClick = { pkg?.let { onPurchase(it) } },
                disable = pkg == null
            )
        }

        // Best Value badge
        if (badge != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-8).dp, y = (-10).dp)
                    .clip(RoundedCornerShape(100f))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF22A927), Color(0xFF049D06))
                        )
                    )
                    .padding(horizontal = Dimens8, vertical = 3.dp),
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
