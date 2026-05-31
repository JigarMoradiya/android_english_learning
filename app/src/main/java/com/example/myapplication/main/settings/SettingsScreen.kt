package com.example.myapplication.main.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import android.app.Activity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.myapplication.data.access.UserAccessState
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.sheets.AccessSheetState
import com.example.myapplication.main.common.sheets.LocalAccessSheetViewModel
import com.example.myapplication.main.common.sheets.KidsBottomSheet
import com.example.myapplication.main.common.sheets.ParentalGateDialog
import com.example.myapplication.ui.theme.AppDimens
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens14
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.ui.theme.getButtonColors
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.utils.extensions.scaled
import androidx.core.net.toUri
import com.example.myapplication.ui.theme.AppDimens.Dimens2

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val userState by viewModel.userState.collectAsState()
    val musicVolume = viewModel.musicVolume
    val showParentalGate by viewModel.showParentalGate.collectAsState()
    val subscriptionInfo by viewModel.subscriptionInfo.collectAsState()
    val isRestoring by viewModel.isRestoring.collectAsState()
    val restoreMessage by viewModel.restoreMessage.collectAsState()
    val navigateToParentProgress by viewModel.navigateToParentProgress.collectAsState()
    val sheetViewModel = LocalAccessSheetViewModel.current
    rememberCoroutineScope()
    val context = LocalContext.current
    var showRateSheet by remember { mutableStateOf(false) }

    LaunchedEffect(navigateToParentProgress) {
        if (navigateToParentProgress) {
            viewModel.consumeParentProgressNavigation()
            navController.navigate(RouteNavigation.ParentProgress.route)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.indigoPurple, shape = KidsFloatingShape.curveLines)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            BackButtonWithText(
                title = "Settings",
                onBackClick = { navController.popBackStack() }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Dimens16),
                verticalArrangement = Arrangement.spacedBy(Dimens12)
            ) {
                // ── Hero user status banner ───────────────────────────────
                UserStatusHeroCard(
                    state = userState,
                    subscriptionInfo = subscriptionInfo,
                    onSignIn = {
                        sheetViewModel.requestState(AccessSheetState.Login(moduleId = "signin_from_settings"))
                    },
                    onUpgrade = {
                        sheetViewModel.requestState(AccessSheetState.Paywall(moduleId = "upgrade_from_settings"))
                    }
                )

                // ── Sound section ─────────────────────────────────────────
                SectionHeader(emoji = "🎵", title = "SOUND")
                MusicVolumeCard(
                    volume = musicVolume,
                    onVolumeChange = { viewModel.updateMusicVolume(it) }
                )

                // ── Explore quick actions ─────────────────────────────────
                SectionHeader(emoji = "🚀", title = "EXPLORE")
                QuickActionsRow(
                    onParentReport = {
                        viewModel.requestParentalGate(SettingsViewModel.ParentalAction.ParentProgress)
                    },
                    onAccessPlan = { navController.navigate(RouteNavigation.AccessPlan.route) },
                    onRateApp = { showRateSheet = true },
                    onPrivacyPolicy = {
                        val uri = "https://docs.google.com/document/d/1dfclCk6Hklv-RbiYi5EYbdx5i65g0YPv/".toUri()
                        context.startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, uri))
                    }
                )

                // ── Account section ───────────────────────────────────────
                SectionHeader(emoji = "👤", title = "ACCOUNT")
                AccountCard(
                    isRestoring = isRestoring,
                    restoreMessage = restoreMessage,
                    isLoggedIn = userState.isLoggedIn,
                    onRestore = {
                        if (!isRestoring) {
                            if (!userState.isLoggedIn) {
                                sheetViewModel.requestLoginForRestore()
                            } else {
                                viewModel.requestParentalGate(SettingsViewModel.ParentalAction.Restore)
                            }
                        }
                    },
                    onLogout = {
                        viewModel.requestParentalGate(SettingsViewModel.ParentalAction.Logout)
                    }
                )

                Spacer(Modifier.height(Dimens16))
            }
        }

        if (showParentalGate) {
            ParentalGateDialog(
                onPassed    = { viewModel.executeAction() },
                onCancelled = { viewModel.dismissParentalGate() }
            )
        }

        KidsBottomSheet(
            visible = showRateSheet,
            onDismiss = { showRateSheet = false },
            wrapContent = true
        ) {
            RateAppSheetContent(
                onLoveIt = {
                    showRateSheet = false
                    val marketUri = "market://details?id=com.vedaavi.english.learning".toUri()
                    val fallbackUri = "https://play.google.com/store/apps/details?id=com.vedaavi.english.learning".toUri()
                    try {
                        context.startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, marketUri).apply {
                            addFlags(android.content.Intent.FLAG_ACTIVITY_NO_HISTORY or android.content.Intent.FLAG_ACTIVITY_NEW_DOCUMENT or android.content.Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                        })
                    } catch (e: android.content.ActivityNotFoundException) {
                        context.startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, fallbackUri))
                    }
                },
                onNotReally = {
                    showRateSheet = false
                    openFeedbackEmail(context)
                }
            )
        }
    }
}

// ── Section header ────────────────────────────────────────────────────────────

@Composable
private fun SectionHeader(emoji: String, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = Dimens4, top = Dimens4),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens6)
    ) {
        Text(text = emoji, style = MaterialTheme.typography.bodySmall.scaled())
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color = Color.Black.copy(alpha = 0.45f)
        )
    }
}

// ── Hero user status card ─────────────────────────────────────────────────────

@Composable
private fun UserStatusHeroCard(
    state: UserAccessState,
    subscriptionInfo: SubscriptionInfo?,
    onSignIn: () -> Unit,
    onUpgrade: () -> Unit
) {
    val (icon, name, type, desc) = when {
        state.isPremium  -> Quad(Icons.Filled.Star,          "Premium ✨",   ButtonType.ORANGE,   "Full access to all activities")
        state.isLoggedIn -> Quad(Icons.Filled.Person,        "Free Account", ButtonType.BLUE,     "3 activities/day")
        else             -> Quad(Icons.Filled.AccountCircle, "Guest",        ButtonType.NEGATIVE, "Sign in to track your progress")
    }
    val colors = getButtonColors(type)

    Box(
        modifier = Modifier
            .padding(top = Dimens4)
            .fillMaxWidth()
            .shadow(elevation = Dimens8, shape = RoundedCornerShape(Dimens16))
            .clip(RoundedCornerShape(Dimens16))
            .background(brush = colors.gradient)
    ) {
        // Decorative circles — Canvas matches iOS GeometryReader proportions exactly
        // iOS uses offset(x,y) for top-left corner, so center = (x + radius, y + radius)
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height
            // Big circle: diameter = h*1.5, top-left at (w*0.55, -h*0.5)
            drawCircle(
                color = Color.White.copy(alpha = 0.10f),
                radius = h * 0.75f,
                center = androidx.compose.ui.geometry.Offset(
                    x = w * 0.55f + h * 0.75f,
                    y = -h * 0.5f + h * 0.75f
                )
            )
            // Small circle: diameter = h, top-left at (w*0.65, h*0.15)
            drawCircle(
                color = Color.White.copy(alpha = 0.07f),
                radius = h * 0.5f,
                center = androidx.compose.ui.geometry.Offset(
                    x = w * 0.65f + h * 0.5f,
                    y = h * 0.15f + h * 0.5f
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens16),
            verticalArrangement = Arrangement.spacedBy(Dimens12)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens16)
            ) {
                // Tier icon circle
                Box(
                    modifier = Modifier
                        .size(AppDimens.Dimens50)
                        .background(Color.White.copy(alpha = 0.22f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(Dimens24)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(Dimens4)
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleLarge.scaled(),
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    if (state.isPremium && subscriptionInfo != null) {
                        Text(
                            text = subscriptionInfo.planName,
                            style = MaterialTheme.typography.bodyLarge.scaled(),
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                        Text(
                            text = subscriptionInfo.renewalDate,
                            style = MaterialTheme.typography.bodySmall.scaled(),
                            color = Color.White.copy(alpha = 0.70f)
                        )
                    } else {
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodyLarge.scaled(),
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            if (!state.isPremium) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(Dimens4, RoundedCornerShape(Dimens12))
                        .clip(RoundedCornerShape(Dimens12))
                        .background(Color.White)
                        .clickable {
                            AudioPlayerManager.playSoundMenuClick()
                            if (state.isLoggedIn) onUpgrade() else onSignIn()
                        }
                        .padding(vertical = Dimens10),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Dimens6),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (state.isLoggedIn) Icons.Filled.Star else Icons.Filled.Person,
                            contentDescription = null,
                            tint = colors.base,
                            modifier = Modifier.size(Dimens16)
                        )
                        Text(
                            text = if (state.isLoggedIn) "Upgrade to Premium" else "Sign In",
                            style = MaterialTheme.typography.titleSmall.scaled(),
                            fontWeight = FontWeight.Bold,
                            color = colors.base
                        )
                    }
                }
            }
        }
    }
}

// ── Music volume card (horizontal) ────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MusicVolumeCard(volume: Float, onVolumeChange: (Float) -> Unit) {
    val purpleLight = Color(0xFFAB47BC)
    val purpleDark  = Color(0xFF6A1B9A)
    val purpleBrush = Brush.linearGradient(listOf(purpleLight, purpleDark))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(Dimens4, RoundedCornerShape(Dimens12))
            .clip(RoundedCornerShape(Dimens12))
            .background(Color.White.copy(alpha = 0.92f))
            .padding(Dimens12),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens12)
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(AppDimens.ToolbarIconSize)
                .shadow(Dimens4, RoundedCornerShape(Dimens10))
                .clip(RoundedCornerShape(Dimens10))
                .background(purpleBrush),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (volume == 0f) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(Dimens20)
            )
        }

        // Title
        Text(
            text = "Background Music",
            style = MaterialTheme.typography.titleSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        // Slider
        Slider(
            value = volume,
            onValueChange = onVolumeChange,
            valueRange = 0f..1f,
            modifier = Modifier.weight(1f),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(Dimens20)
                        .graphicsLayer(shadowElevation = 12f, shape = CircleShape, clip = true)
                        .background(purpleLight, CircleShape)
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    modifier = Modifier.height(Dimens4),
                    colors = SliderDefaults.colors(
                        activeTrackColor = purpleLight,
                        inactiveTrackColor = purpleLight.copy(alpha = 0.2f),
                        thumbColor = purpleLight
                    )
                )
            }
        )

        // Percentage
        Text(
            text = "${(volume * 100).toInt()}%",
            style = MaterialTheme.typography.titleSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color = purpleDark.copy(alpha = 0.85f),
            modifier = Modifier.widthIn(min = AppDimens.Dimens40)
        )
    }
}

// ── Quick actions horizontal row ──────────────────────────────────────────────

@Composable
private fun QuickActionsRow(
    onParentReport: () -> Unit,
    onAccessPlan: () -> Unit,
    onRateApp: () -> Unit,
    onPrivacyPolicy: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens8)
    ) {
        QuickActionCard(
            emoji = "🏆",
            title = "Parent Report",
            gradient = Brush.linearGradient(listOf(Color(0xFF8E24AA), Color(0xFF4A148C))),
            shadowColor = Color(0xFF8E24AA),
            modifier = Modifier.weight(1f),
            onClick = onParentReport
        )
        QuickActionCard(
            emoji = "🎯",
            title = "Access Plan",
            gradient = Brush.linearGradient(listOf(Color(0xFF1E88E5), Color(0xFF0D47A1))),
            shadowColor = Color(0xFF1E88E5),
            modifier = Modifier.weight(1f),
            onClick = onAccessPlan
        )
        QuickActionCard(
            emoji = "❤️",
            title = "Rate the App",
            gradient = Brush.linearGradient(listOf(Color(0xFFFB8C00), Color(0xFFE65100))),
            shadowColor = Color(0xFFFB8C00),
            modifier = Modifier.weight(1f),
            onClick = onRateApp
        )
        QuickActionCard(
            emoji = "🛡️",
            title = "Privacy Policy",
            gradient = Brush.linearGradient(listOf(Color(0xFF00897B), Color(0xFF004D40))),
            shadowColor = Color(0xFF00897B),
            modifier = Modifier.weight(1f),
            onClick = onPrivacyPolicy
        )
    }
}

@Composable
private fun QuickActionCard(
    emoji: String,
    title: String,
    gradient: Brush,
    shadowColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = Dimens8,
                shape = RoundedCornerShape(Dimens16),
                ambientColor = shadowColor.copy(alpha = 0.4f),
                spotColor = shadowColor.copy(alpha = 0.4f)
            )
            .clip(RoundedCornerShape(Dimens16))
            .background(gradient)
            .clickable {
                AudioPlayerManager.playSoundMenuClick()
                onClick()
            }
    ) {
        // Decorative circle top-right
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(AppDimens.Dimens40)
                .offset(x = Dimens8, y = -(Dimens8))
                .background(Color.White.copy(alpha = 0.08f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens14),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens6)
        ) {
            Box(
                modifier = Modifier
                    .size(AppDimens.Dimens50)
                    .background(Color.White.copy(alpha = 0.25f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    style = MaterialTheme.typography.titleMedium.scaled()
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ── Account card ──────────────────────────────────────────────────────────────

@Composable
private fun AccountCard(
    isRestoring: Boolean,
    restoreMessage: String?,
    isLoggedIn: Boolean,
    onRestore: () -> Unit,
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(Dimens4, RoundedCornerShape(Dimens16))
            .clip(RoundedCornerShape(Dimens16))
            .background(Color.White.copy(alpha = 0.92f))
    ) {
        Column {
            AccountRow(
                icon = Icons.Filled.Refresh,
                title = if (isRestoring) "Restoring…" else "Restore Purchases",
                subtitle = restoreMessage ?: "Already subscribed? Tap to restore",
                iconColor = if (restoreMessage == "No active subscription found.") Color(0xFFE53935) else Color(0xFF43A047),
                onClick = onRestore
            )
            if (isLoggedIn) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = Dimens16),
                    thickness = AppDimens.Dimens1,
                    color = Color.Gray.copy(alpha = 0.2f)
                )
                AccountRow(
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    title = "Log Out",
                    subtitle = "Sign out of your account",
                    iconColor = Color(0xFFE53935),
                    onClick = onLogout
                )
            }
        }
    }
}

@Composable
private fun AccountRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                AudioPlayerManager.playSoundMenuClick()
                onClick()
            }
            .padding(Dimens12),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens12)
    ) {
        Box(
            modifier = Modifier
                .size(AppDimens.ToolbarIconSize)
                .clip(RoundedCornerShape(Dimens10))
                .background(iconColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(Dimens20)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall.scaled(), color = Color.Black)
            Text(subtitle, style = MaterialTheme.typography.bodySmall.scaled(), color = Color.Gray)
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray.copy(alpha = 0.4f),
            modifier = Modifier.size(Dimens20)
        )
    }
}

// ── Rate App Sheet ────────────────────────────────────────────────────────────

@Composable
private fun RateAppSheetContent(
    onLoveIt: () -> Unit,
    onNotReally: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "stars")
    val emojiScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
        label = "emojiScale"
    )
    val emojis = listOf("🌟", "😍", "⭐", "😍", "🌟")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens24),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens16)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens8)) {
            emojis.forEachIndexed { i, emoji ->
                val phase = ((emojiScale - 1f) + i * 0.05f).coerceIn(0f, 0.3f)
                Text(
                    text = emoji,
                    style = MaterialTheme.typography.headlineMedium.scaled(),
                    modifier = Modifier.graphicsLayer {
                        scaleX = 1f + phase
                        scaleY = 1f + phase
                    }
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens8)
        ) {
            Text(
                text = "How are we doing?",
                style = MaterialTheme.typography.headlineMedium.scaled(),
                fontWeight = FontWeight.Black,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = "We'd love to hear if your little learner is enjoying the lessons!",
                style = MaterialTheme.typography.bodyMedium.scaled(),
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KidsActionButton(
                text = "😍 Yes, Love it!",
                type = ButtonType.POSITIVE,
                isSmall = true,
                onClick = onLoveIt
            )
            KidsActionButton(
                text = "😕 Not really",
                type = ButtonType.RED,
                isSmall = true,
                onClick = onNotReally
            )
        }
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

private fun openFeedbackEmail(context: android.content.Context) {
    val intent = android.content.Intent(android.content.Intent.ACTION_SENDTO).apply {
        data = android.net.Uri.parse("mailto:vedaavilearning@gmail.com")
        putExtra(android.content.Intent.EXTRA_SUBJECT, "App Feedback - Kids English Learning")
        setPackage("com.google.android.gm")
    }
    try {
        context.startActivity(intent)
    } catch (_: Exception) {
        val fallback = android.content.Intent(android.content.Intent.ACTION_SENDTO).apply {
            data = android.net.Uri.parse("mailto:vedaavilearning@gmail.com")
            putExtra(android.content.Intent.EXTRA_SUBJECT, "App Feedback - Kids English Learning")
        }
        try { context.startActivity(fallback) } catch (_: Exception) { }
    }
}

data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)