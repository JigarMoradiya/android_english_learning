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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.data.access.UserAccessState
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
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

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val userState by viewModel.userState.collectAsState()
    val showParentalGate by viewModel.showParentalGate.collectAsState()
    val sheetViewModel = LocalAccessSheetViewModel.current

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundUI()

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
                UserStatusCard(userState) {
                    sheetViewModel.requestState(AccessSheetState.Paywall(moduleId = "upgrade_from_settings"))
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

                // ── Restore + Logout ─────────────────────────────────────
                SettingsCard {
                    Column {
                        SettingsRow(
                            icon = Icons.Filled.Refresh,
                            title = "Restore Purchases",
                            subtitle = "Already subscribed? Tap to restore",
                            type = ButtonType.GREEN
                        ) {
                            viewModel.requestParentalGate(SettingsViewModel.ParentalAction.Restore)
                        }
                        if (userState.isLoggedIn) {
                            Divider(color = Color.Gray.copy(alpha = 0.2f), thickness = AppDimens.Dimens1)
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
private fun UserStatusCard(state: UserAccessState, onUpgrade: () -> Unit) {
    val tierInfo = when {
        state.isPremium  -> Quad(Icons.Filled.Star,          "Premium",      ButtonType.ORANGE,   "Full access to all activities")
        state.isLoggedIn -> Quad(Icons.Filled.Person,        "Free Account", ButtonType.BLUE,     "5 plays/day on limited activities")
        else             -> Quad(Icons.Filled.AccountCircle, "Guest",        ButtonType.NEGATIVE, "3 plays/day on limited activities")
    }
    val (icon, name, type, desc) = tierInfo
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
                fontSize = 18.sp,
                color = colors.base
            )
            Text(
                text = desc,
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        if (!state.isPremium) {
            Spacer(Modifier.width(Dimens8))
            KidsActionButton(
                text = "Upgrade",
                icon = Icons.Filled.Star,
                type = ButtonType.ORANGE,
                isSmall = true,
                onClick = onUpgrade
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
            Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray.copy(alpha = 0.5f),
            modifier = Modifier.size(Dimens20)
        )
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
