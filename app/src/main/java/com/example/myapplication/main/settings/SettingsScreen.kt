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
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import android.app.Activity
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.myapplication.data.access.UserAccessState
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.sheets.AccessSheetState
import com.example.myapplication.main.common.sheets.LocalAccessSheetViewModel
import com.example.myapplication.main.common.sheets.ParentalGateDialog
import com.example.myapplication.ui.theme.AppDimens
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
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
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(navigateToParentProgress) {
        if (navigateToParentProgress) {
            viewModel.consumeParentProgressNavigation()
            navController.navigate(com.example.myapplication.main.base.nav.RouteNavigation.ParentProgress.route)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.indigoPurple, shape = KidsFloatingShape.curveLines)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            // ── Back button ──────────────────────────────────────────────
            BackButtonWithText(
                title = "Settings",
                onBackClick = { navController.popBackStack() }
            )

            // ── Content ──────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Dimens16),
                verticalArrangement = Arrangement.spacedBy(Dimens12)
            ) {
                Spacer(Modifier.height(Dimens8))

                // ── User status card ─────────────────────────────────────
                UserStatusCard(
                    state = userState,
                    subscriptionInfo = subscriptionInfo,
                    onSignIn = {
                        sheetViewModel.requestState(AccessSheetState.Login(moduleId = "signin_from_settings"))
                    },
                    onUpgrade = {
                        sheetViewModel.requestState(AccessSheetState.Paywall(moduleId = "upgrade_from_settings"))
                    }
                )

                // ── Background music volume ──────────────────────────────
                SettingsCard {
                    MusicVolumeRow(
                        volume = musicVolume,
                        onVolumeChange = { viewModel.updateMusicVolume(it) }
                    )
                }

                // ── Parent Report ────────────────────────────────────────
                SettingsCard {
                    SettingsRow(
                        icon = Icons.Filled.Person,
                        title = "Parent Report",
                        subtitle = "View your child's weekly progress",
                        type = ButtonType.BLUE
                    ) {
                        viewModel.requestParentalGate(SettingsViewModel.ParentalAction.ParentProgress)
                    }
                }

                // ── Access Plan ──────────────────────────────────────────
                SettingsCard {
                    SettingsRow(
                        icon = Icons.Filled.Map,
                        title = "Access Plan",
                        subtitle = "See what's included in each tier",
                        type = ButtonType.BLUE
                    ) {
                        navController.navigate(RouteNavigation.AccessPlan.route)
                    }
                }

                // ── Rate the App ─────────────────────────────────────────
                SettingsCard {
                    SettingsRow(
                        icon = Icons.Filled.Star,
                        title = "Rate the App",
                        subtitle = "Enjoying the app? Leave us a review ⭐",
                        type = ButtonType.ORANGE
                    ) {
                        val marketUri = "market://details?id=com.vedaavi.english.learning".toUri()
                        val fallbackUri = "https://play.google.com/store/apps/details?id=com.vedaavi.english.learning".toUri()
                        try {
                            context.startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, marketUri).apply {
                                addFlags(android.content.Intent.FLAG_ACTIVITY_NO_HISTORY or android.content.Intent.FLAG_ACTIVITY_NEW_DOCUMENT or android.content.Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                            })
                        } catch (e: android.content.ActivityNotFoundException) {
                            context.startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, fallbackUri))
                        }
                    }
                }

                // ── Restore + Logout ─────────────────────────────────────
                SettingsCard {
                    Column {
                        SettingsRow(
                            icon = Icons.Filled.Refresh,
                            title = if (isRestoring) "Restoring…" else "Restore Purchases",
                            subtitle = restoreMessage ?: "Already subscribed? Tap to restore",
                            type = if (restoreMessage == "No active subscription found.") ButtonType.RED else ButtonType.GREEN
                        ) {
                            if (!isRestoring) {
                                if (!userState.isLoggedIn) {
                                    sheetViewModel.requestLoginForRestore()
                                } else {
                                    viewModel.requestParentalGate(SettingsViewModel.ParentalAction.Restore)
                                }
                            }
                        }
                        if (userState.isLoggedIn) {
                            HorizontalDivider(Modifier, thickness = AppDimens.Dimens1, color = Color.Gray.copy(alpha = 0.2f))
                            SettingsRow(
                                icon = Icons.AutoMirrored.Filled.ExitToApp,
                                title = "Log Out",
                                subtitle = "Sign out of your account",
                                type = ButtonType.RED
                            ) {
                                viewModel.requestParentalGate(SettingsViewModel.ParentalAction.Logout)
                            }
                        }
                    }
                }

                // ── [DEV] Clear trial offer flags ────────────────────────
//                SettingsCard {
//                    SettingsRow(
//                        icon = Icons.Filled.Refresh,
//                        title = "Reset Trial Offer Flag",
//                        subtitle = "Dev only — next age-card tap shows trial paywall again",
//                        type = ButtonType.ORANGE
//                    ) { viewModel.clearTrialOfferFlags() }
//                }

                // ── [DEV] Clear today's activity count ──────────────────
//                SettingsCard {
//                    SettingsRow(
//                        icon = Icons.Filled.DeleteForever,
//                        title = "Clear Today's Activity Count",
//                        subtitle = "Dev only — resets daily limit for testing",
//                        type = ButtonType.RED
//                    ) { viewModel.clearTodayActivityCount() }
//                }

                // ── [DEV] Simulate 6-day streak ──────────────────────────
//                SettingsCard {
//                    SettingsRow(
//                        icon = Icons.Filled.Refresh,
//                        title = "Simulate 6-Day Streak",
//                        subtitle = "Dev only — do 1 activity after this to trigger 7-day banner",
//                        type = ButtonType.BLUE
//                    ) { viewModel.simulateSixDayStreak() }
//                }

                // ── [DEV] Reset activity milestone (50) ─────────────────
//                SettingsCard {
//                    SettingsRow(
//                        icon = Icons.Filled.Refresh,
//                        title = "Reset Activity Milestone (50)",
//                        subtitle = "Dev only — next activity triggers 50-activities banner",
//                        type = ButtonType.ORANGE
//                    ) { viewModel.clearActivityMilestone(50) }
//                }

                // ── [DEV] Test premium celebration ───────────────────────
//                SettingsCard {
//                    SettingsRow(
//                        icon = Icons.Filled.Star,
//                        title = "Test Premium Celebration",
//                        subtitle = "Dev only — shows the post-purchase sheet",
//                        type = ButtonType.ORANGE
//                    ) {
//                        sheetViewModel.requestState(AccessSheetState.PremiumCelebration)
//                    }
//                }

                // ── [DEV] Clear all app preferences ──────────────────────
                SettingsCard {
                    SettingsRow(
                        icon = Icons.Filled.Delete,
                        title = "Clear All App Data",
                        subtitle = "Dev only — wipes all data and restarts the app",
                        type = ButtonType.RED
                    ) { viewModel.clearAllPreferences(context) }
                }

                Spacer(Modifier.height(Dimens16))
            }
        }

        // ── Parental gate overlay (shared common component) ─────────────
        if (showParentalGate) {
            ParentalGateDialog(
                onPassed    = { viewModel.executeAction() },
                onCancelled = { viewModel.dismissParentalGate() }
            )
        }
    }
}

// ── User status card ──────────────────────────────────────────────────────────

@Composable
private fun UserStatusCard(
    state: UserAccessState,
    subscriptionInfo: SubscriptionInfo?,
    onSignIn: () -> Unit,
    onUpgrade: () -> Unit
) {
    val (icon, name, type, desc) = when {
        state.isPremium  -> Quad(Icons.Filled.Star,          "Premium",      ButtonType.ORANGE,   "Full access to all activities")
        state.isLoggedIn -> Quad(Icons.Filled.Person,        "Free Account", ButtonType.BLUE,     "3 activities/day")
        else             -> Quad(Icons.Filled.AccountCircle, "Guest",        ButtonType.NEGATIVE, "Sign in to track your progress")
    }
    val colors = getButtonColors(type)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(Dimens4, RoundedCornerShape(Dimens16))
            .clip(RoundedCornerShape(Dimens16))
            .background(Color.White.copy(alpha = 0.92f))
            .padding(Dimens16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tier icon circle
        Box(
            modifier = Modifier
                .size(AppDimens.Dimens50)
                .shadow(Dimens4, CircleShape)
                .clip(CircleShape)
                .background(brush = colors.gradient),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(Dimens24)
            )
        }

        Spacer(Modifier.width(Dimens12))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium.scaled(),
                color = colors.base
            )
            // Premium: show plan name + price + renewal date
            if (state.isPremium && subscriptionInfo != null) {
                Row(horizontalArrangement = Arrangement.spacedBy(Dimens6)) {
                    Text(
                        text = subscriptionInfo.planName,
                        style = MaterialTheme.typography.bodyMedium.scaled(),
                        fontWeight = FontWeight.Medium,
                        color = Color.Black.copy(alpha = 0.75f)
                    )
                    if (subscriptionInfo.price.isNotEmpty()) {
                        Text(
                            text = "· ${subscriptionInfo.price}",
                            style = MaterialTheme.typography.bodyMedium.scaled(),
                            fontWeight = FontWeight.Medium,
                            color = Color.Black.copy(alpha = 0.75f)
                        )
                    }
                }
                Text(
                    text = subscriptionInfo.renewalDate,
                    style = MaterialTheme.typography.bodySmall.scaled(),
                    color = Color.Gray
                )
            } else {
                Text(text = desc, style = MaterialTheme.typography.bodyMedium.scaled(), color = Color.Gray)
            }
        }

        // Guest → Sign In button; Free → Upgrade button; Premium → nothing
        Spacer(Modifier.width(Dimens8))
        when {
            state.isPremium  -> { /* no button */ }
            state.isLoggedIn -> KidsActionButton(
                text = "Upgrade",
                icon = Icons.Filled.Star,
                type = ButtonType.ORANGE,
                isSmall = true,
                onClick = onUpgrade
            )
            else -> KidsActionButton(
                text = "Sign In",
                icon = Icons.Filled.Person,
                type = ButtonType.BLUE,
                isSmall = true,
                onClick = onSignIn
            )
        }
    }
}

// ── Settings card wrapper ─────────────────────────────────────────────────────

@Composable
private fun SettingsCard(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(Dimens4, RoundedCornerShape(Dimens16))
            .clip(RoundedCornerShape(Dimens16))
            .background(Color.White.copy(alpha = 0.92f))
    ) {
        content()
    }
}

// ── Settings row ──────────────────────────────────────────────────────────────

@Composable
private fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    type: ButtonType,
    onClick: () -> Unit
) {
    val colors = getButtonColors(type)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                AudioPlayerManager.playSoundMenuClick()
                onClick()
            }
            .padding(Dimens12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Gradient icon box
        Box(
            modifier = Modifier
                .size(AppDimens.ToolbarIconSize)
                .shadow(Dimens4, RoundedCornerShape(Dimens10))
                .clip(RoundedCornerShape(Dimens10))
                .background(brush = colors.gradient),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(Dimens20)
            )
        }

        Spacer(Modifier.width(Dimens12))

        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall.scaled(), color = Color.Black)
            Text(subtitle, style = MaterialTheme.typography.bodySmall.scaled(), color = Color.Gray)
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray.copy(alpha = 0.5f),
            modifier = Modifier.size(Dimens20)
        )
    }
}

// ── Music volume row ──────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MusicVolumeRow(volume: Float, onVolumeChange: (Float) -> Unit) {
    // iOS system purple — matches SwiftUI Color.purple in light mode
    val purple = Color(0xFFBF5AF2)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(AppDimens.ToolbarIconSize)
                    .shadow(Dimens4, RoundedCornerShape(Dimens10))
                    .clip(RoundedCornerShape(Dimens10))
                    .background(purple),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.MusicNote,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(Dimens20)
                )
            }

            Spacer(Modifier.width(Dimens12))

            Column(modifier = Modifier.weight(1f)) {
                Text("Background Music", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall.scaled(), color = Color.Black)
                Text("Adjust music volume", style = MaterialTheme.typography.bodySmall.scaled(), color = Color.Gray)
            }

            Icon(
                imageVector = if (volume == 0f) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = null,
                tint = purple.copy(alpha = 0.7f),
                modifier = Modifier.size(Dimens20)
            )
        }

        Slider(
            value = volume,
            onValueChange = onVolumeChange,
            valueRange = 0f..1f,
            thumb = {
                Box(
                    modifier = Modifier
                        .size(Dimens20)
                        .graphicsLayer(
                            shadowElevation = 12f,
                            shape = CircleShape,
                            clip = true
                        )
                        .background(purple, CircleShape)
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    modifier = Modifier.height(Dimens4),
                    colors = SliderDefaults.colors(
                        activeTrackColor = purple,
                        inactiveTrackColor = purple.copy(alpha = 0.2f),
                        thumbColor = purple,
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens16)
                .padding(bottom = Dimens12)
        )
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
