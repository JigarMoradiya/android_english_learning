package com.example.myapplication.main.age_category

import android.content.Context
import android.content.Intent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.generation.letter.ImageAlphabetMapper
import com.example.myapplication.main.age_category.view_model.AgeCategoriesViewModel
import com.example.myapplication.main.base.force_update.ForceUpdateHandler
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.base.notification.OneSignalSubscriptionWatcher
import com.example.myapplication.main.common.HomePageBackground
import com.example.myapplication.main.common.permission.NotificationPermissionHandler
import com.example.myapplication.main.common.sheets.ParentalGateDialog
import com.example.myapplication.ui.theme.AppDimens
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens14
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.extensions.scaled
import kotlin.math.min

// ── Pill shape ────────────────────────────────────────────────────────────────

private val PillShape = RoundedCornerShape(percent = 50)

// ── Speech bubble shape ────────────────────────────────────────────────────────

private data class SpeechBubbleShapeImpl(
    private val cornerRadiusPx: Float,
    private val tailWidthPx: Float,
    private val tailHeightPx: Float,
    private val tailOffsetXPx: Float
) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path()
        val cr = min(cornerRadiusPx, (size.height - tailHeightPx) / 2)
        val bH = size.height - tailHeightPx
        val midX = size.width / 2f + tailOffsetXPx
        val tailL = midX - tailWidthPx / 2f
        val tailR = midX + tailWidthPx / 2f

        path.moveTo(cr, 0f)
        path.lineTo(size.width - cr, 0f)
        path.arcTo(Rect(size.width - 2 * cr, 0f, size.width, 2 * cr), -90f, 90f, false)
        path.lineTo(size.width, bH - cr)
        path.arcTo(Rect(size.width - 2 * cr, bH - 2 * cr, size.width, bH), 0f, 90f, false)
        path.lineTo(tailR, bH)
        path.lineTo(midX, size.height)
        path.lineTo(tailL, bH)
        path.lineTo(cr, bH)
        path.arcTo(Rect(0f, bH - 2 * cr, 2 * cr, bH), 90f, 90f, false)
        path.lineTo(0f, cr)
        path.arcTo(Rect(0f, 0f, 2 * cr, 2 * cr), 180f, 90f, false)
        path.close()
        return Outline.Generic(path)
    }
}

@Composable
private fun rememberSpeechBubbleShape(
    cornerRadius: Dp,
    tailWidth: Dp = 18.dp,
    tailHeight: Dp = 10.dp,
    tailOffsetX: Dp = 36.dp
): Shape {
    val density = LocalDensity.current
    return remember(density, cornerRadius, tailWidth, tailHeight, tailOffsetX) {
        with(density) {
            SpeechBubbleShapeImpl(
                cornerRadiusPx = cornerRadius.toPx(),
                tailWidthPx = tailWidth.toPx(),
                tailHeightPx = tailHeight.toPx(),
                tailOffsetXPx = tailOffsetX.toPx()
            )
        }
    }
}

// ── Letter accent palette ─────────────────────────────────────────────────────

private val letterAccentPalette = listOf(
    Color(0xFFE53935), Color(0xFFF57C00), Color(0xFFF9A825),
    Color(0xFF00ACC1), Color(0xFF00897B), Color(0xFF7B1FA2),
    Color(0xFFC2185B), Color(0xFF5E35B1), Color(0xFF00695C),
    Color(0xFFE64A19), Color(0xFF1565C0), Color(0xFF2E7D32),
    Color(0xFFE84393), Color(0xFF0984E3), Color(0xFF546E7A),
    Color(0xFFB71C1C), Color(0xFFE65100), Color(0xFF006064),
    Color(0xFF558B2F), Color(0xFFF57F17), Color(0xFF37474F),
    Color(0xFF880E4F), Color(0xFF0D47A1), Color(0xFF4A148C),
    Color(0xFF1B5E20), Color(0xFFBF360C)
)

// ── Main Screen ───────────────────────────────────────────────────────────────

@Composable
fun MainLearningAgesCategoriesScreen(
    navController: NavController,
    viewModel: AgeCategoriesViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsState()
    val context = LocalContext.current
    var showParentalGate by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.refresh() }

    NotificationPermissionHandler()
    OneSignalSubscriptionWatcher()

    val infiniteTransition = rememberInfiniteTransition(label = "mascot")
    val mascotOffsetY by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = -7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "mascotFloat"
    )

    val bubbleShape = rememberSpeechBubbleShape(cornerRadius = 16.dp)

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val totalH = maxHeight
        val totalW = maxWidth
        val mascotMaxH = min(totalH.value * 0.50f, 240f).dp

        // Card dimensions calculated once from full-screen size (same formula as iOS)
        val hPad = Dimens16
        val cardGap = Dimens12
        val rightPanelW = totalW * 0.78f

        HomePageBackground()

        Column(modifier = Modifier.fillMaxSize()) {

            // ── Full-width top bar ─────────────────────────────────────────
            HomeTopBar(
                onParentClick = {
                    AudioPlayerManager.playSoundMenuClick()
                    showParentalGate = true
                },
                onSettingsClick = {
                    AudioPlayerManager.playSoundMenuClick()
                    navController.navigate(RouteNavigation.Settings.route)
                }
            )

            // ── Body: mascot left (26%) + content right (74%) ─────────────
            Row(modifier = Modifier.fillMaxSize()) {

                // ── Left panel: speech bubble + mascot at bottom ───────────
                Box(
                    modifier = Modifier
                        .width(totalW * 0.22f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens10)
                            .padding(bottom = Dimens12),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = Dimens4,
                                    shape = bubbleShape,
                                    ambientColor = Color(0xFF5532D2).copy(alpha = 0.12f),
                                    spotColor = Color(0xFF5532D2).copy(alpha = 0.12f)
                                )
                                .background(Color.White.copy(alpha = 0.95f), bubbleShape)
                                .border(1.5.dp, Color(0xFF9374EF).copy(alpha = 0.4f), bubbleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🌟 What do you want\nto learn today?",
                                color = Color(0xFF3D2B9E),
                                fontSize = 11.sp.scaled(),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                lineHeight = 16.sp.scaled(),
                                modifier = Modifier
                                    .padding(horizontal = Dimens12, vertical = Dimens8)
                                    .padding(bottom = Dimens12)
                            )
                        }

                        Image(
                            painter = painterResource(R.drawable._mascot_lion),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .height(mascotMaxH)
                                .fillMaxWidth()
                                .graphicsLayer { translationY = mascotOffsetY }
                        )
                    }
                }

                // ── Right panel: streak | divider | cards | banner ─────────
                Column(
                    modifier = Modifier
                        .width(rightPanelW)
                        .fillMaxHeight()
                ) {
                    // Streak + today's letter
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = hPad)
                            .padding(bottom = Dimens8),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StreakCard(
                            currentStreak = viewModel.currentStreak,
                            bestStreak = viewModel.bestStreak
                        )
                        Spacer(modifier = Modifier.width(Dimens12))
                        TodayLetterCard(letter = viewModel.todayLetter)
                    }

                    // "Pick your level" divider
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = hPad)
                            .padding(bottom = Dimens4),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color(0xFF9374EF).copy(alpha = 0.35f),
                            thickness = 1.5.dp
                        )
                        Spacer(modifier = Modifier.width(Dimens6))
                        Icon(
                            painter = painterResource(R.drawable.ic_sparkles),
                            contentDescription = null,
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(12.dp.scaled())
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Pick your level and start learning!",
                            color = Color(0xFF5532D2).copy(alpha = 0.85f),
                            fontSize = 11.sp.scaled(),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(Dimens6))
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color(0xFF9374EF).copy(alpha = 0.35f),
                            thickness = 1.5.dp
                        )
                    }

                    // Age cards — fill remaining space between header and footer
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = hPad)
                            .padding(bottom = Dimens4),
                        horizontalArrangement = Arrangement.spacedBy(cardGap),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        categories.forEach { category ->
                            val interactionSource = remember { MutableInteractionSource() }
                            val isPressed by interactionSource.collectIsPressedAsState()
                            val scale by animateFloatAsState(
                                targetValue = if (isPressed) 0.93f else 1f,
                                animationSpec = spring(
                                    dampingRatio = 0.5f,
                                    stiffness = Spring.StiffnessMedium
                                ),
                                label = "cardScale"
                            )
                            Image(
                                painter = painterResource(id = category.img),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .graphicsLayer { scaleX = scale; scaleY = scale }
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        AudioPlayerManager.playSoundMenuClick()
                                        navController.navigate(category.destination)
                                    }
                            )
                        }
                    }

                    // Footer — always at bottom
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = hPad)
                            .padding(bottom = Dimens8),
                        horizontalArrangement = Arrangement.End
                    ) {
                        AbacusBanner(context = context)
                    }
                }
            }
        }

        ForceUpdateHandler()

        if (showParentalGate) {
            ParentalGateDialog(
                onPassed = {
                    showParentalGate = false
                    navController.navigate(RouteNavigation.ParentProgress.route)
                },
                onCancelled = { showParentalGate = false }
            )
        }
    }
}

// ── Top Bar ───────────────────────────────────────────────────────────────────

@Composable
private fun HomeTopBar(onParentClick: () -> Unit, onSettingsClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens16)
            .padding(top = Dimens10, bottom = Dimens8),
        contentAlignment = Alignment.Center
    ) {
        // Center: app title (mixed case, large — matching iOS font36)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Vedaavi English",
                fontSize = AppDimens.FontExtraLarge36,
                fontWeight = FontWeight.Black,
                color = Color(0xFF3D2B9E),
                letterSpacing = 0.sp
            )
            Text(
                text = "Learn • Play • Grow 🎓",
                fontSize = 11.sp.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6C5CE7).copy(alpha = 0.75f)
            )
        }

        // Left: Parent pill
        GradientPill(
            text = "Parent",
            icon = R.drawable.ic_parent_report,
            gradient = Brush.linearGradient(
                listOf(Color(0xFF9374EF), Color(0xFF5532D2))
            ),
            shadowColor = Color(0xFF5532D2).copy(alpha = 0.45f),
            modifier = Modifier.align(Alignment.CenterStart),
            onClick = onParentClick
        )

        // Right: Settings pill
        GradientPill(
            text = "Settings",
            icon = R.drawable.ic_settings_gear,
            gradient = Brush.linearGradient(
                listOf(Color(0xFF43A047), Color(0xFF2E7D32))
            ),
            shadowColor = Color(0xFF2E7D32).copy(alpha = 0.45f),
            modifier = Modifier.align(Alignment.CenterEnd),
            onClick = onSettingsClick
        )
    }
}

@Composable
private fun GradientPill(
    text: String,
    icon: Int,
    gradient: Brush,
    shadowColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium),
        label = "pillScale"
    )
    Row(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .shadow(elevation = Dimens6, shape = PillShape, spotColor = shadowColor, ambientColor = shadowColor)
            .background(gradient, PillShape)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .padding(horizontal = Dimens12, vertical = Dimens4),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens6)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(14.dp.scaled())
        )
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp.scaled(),
            fontWeight = FontWeight.Bold
        )
    }
}

// ── Streak Card ───────────────────────────────────────────────────────────────

@Composable
private fun StreakCard(currentStreak: Int, bestStreak: Int) {
    Row(
        modifier = Modifier
            .shadow(
                elevation = Dimens4,
                shape = PillShape,
                ambientColor = Color(0xFFFF9800).copy(alpha = 0.28f),
                spotColor = Color(0xFFFF9800).copy(alpha = 0.28f)
            )
            .background(
                brush = Brush.verticalGradient(listOf(Color(0xFFFFF8E1), Color(0xFFFFF3E0))),
                shape = PillShape
            )
            .border(1.5.dp, Color(0xFFFFB74D).copy(alpha = 0.7f), PillShape)
            .padding(horizontal = Dimens14, vertical = Dimens8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens8)
    ) {
        Text(
            text = "🔥",
            fontSize = 18.sp.scaled(),
            lineHeight = 20.sp.scaled()
        )
        Column {
            Text(
                text = "$currentStreak Day${if (currentStreak == 1) "" else "s"}",
                fontSize = 13.sp.scaled(),
                fontWeight = FontWeight.Black,
                color = Color(0xFFE65100),
                lineHeight = 15.sp.scaled()
            )
            Text(
                text = "Streak  •  Best: $bestStreak",
                fontSize = 9.sp.scaled(),
                fontWeight = FontWeight.Medium,
                color = Color(0xFFBF360C),
                lineHeight = 11.sp.scaled()
            )
        }
    }
}

// ── Today's Letter Card ───────────────────────────────────────────────────────

@Composable
private fun TodayLetterCard(letter: Char) {
    val dayOfYear = remember { java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR) }
    val accentColor = letterAccentPalette[(dayOfYear - 1) % letterAccentPalette.size]
    val imgRes = remember(letter) {
        ImageAlphabetMapper.get("${letter.lowercaseChar()}_outline_c")
    }

    Row(
        modifier = Modifier
            .shadow(
                elevation = 4.dp,
                shape = PillShape,
                ambientColor = Color.Black.copy(alpha = 0.09f),
                spotColor = Color.Black.copy(alpha = 0.09f)
            )
            .background(Color.White.copy(alpha = 0.88f), PillShape)
            .border(1.5.dp, accentColor.copy(alpha = 0.4f), PillShape)
            .padding(horizontal = Dimens14, vertical = Dimens8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens8)
    ) {
        Box(
            modifier = Modifier
                .size(AppDimens.Dimens40)
                .background(accentColor.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (imgRes != null) {
                Image(
                    painter = painterResource(imgRes),
                    contentDescription = null,
                    modifier = Modifier.size(AppDimens.Dimens32)
                )
            } else {
                Text(
                    text = letter.toString(),
                    fontSize = 18.sp.scaled(),
                    lineHeight = 20.sp.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            }
        }
        Column {
            Text(
                text = "Today's Letter",
                fontSize = 9.sp.scaled(),
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                lineHeight = 11.sp.scaled()
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = "Let's practice '$letter' today!",
                    fontSize = 11.sp.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black.copy(alpha = 0.7f),
                    lineHeight = 13.sp.scaled()
                )
                Icon(
                    painter = painterResource(R.drawable.ic_sparkles),
                    contentDescription = null,
                    tint = Color(0xFFF59E0B),
                    modifier = Modifier.size(11.dp.scaled())
                )
            }
        }
    }
}

// ── Abacus Banner ─────────────────────────────────────────────────────────────

@Composable
private fun AbacusBanner(context: Context) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium),
        label = "bannerScale"
    )

    Row(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = Dimens4,
                shape = RoundedCornerShape(Dimens12),
                clip = false,
                ambientColor = Color(0xFF3D52D5).copy(alpha = 0.35f),
                spotColor = Color(0xFF3D52D5).copy(alpha = 0.35f)
            )
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        Color(0xFF3949AB),
                        Color(0xFF5568D8)
                    )
                ),
                shape = RoundedCornerShape(Dimens12)
            )
            .drawWithContent {
                drawContent()
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.22f),
                    cornerRadius = CornerRadius(Dimens12.toPx()),
                    style = Stroke(width = 1.dp.toPx())
                )
            }
            .clickable(interactionSource = interactionSource, indication = null) {
                runCatching {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = "https://play.google.com/store/apps/details?id=com.abacus.puzzle&hl=en".toUri()
                        addCategory(Intent.CATEGORY_BROWSABLE)
                    }
                    context.startActivity(intent)
                }
            }
            .padding(horizontal = Dimens10, vertical = Dimens6),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens6)
    ) {
        Image(
            painter = painterResource(R.drawable._abacus_logo),
            contentDescription = null,
            modifier = Modifier.size(24.dp.scaled())
        )
        Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Text(
                text = "Also try: Abacus Math! 🧮",
                color = Color.White,
                fontSize = 9.sp.scaled(),
                lineHeight = 9.sp.scaled(),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Fun counting for kids",
                color = Color.White.copy(alpha = 0.82f),
                fontSize = 9.sp.scaled(),
                lineHeight = 9.sp.scaled(),
                fontWeight = FontWeight.Medium
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.size(12.dp.scaled())
        )
    }
}
