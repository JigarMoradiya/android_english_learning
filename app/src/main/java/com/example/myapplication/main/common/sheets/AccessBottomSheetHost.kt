package com.example.myapplication.main.common.sheets

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.ui.theme.AppDimens.PaywallSheetHeightFraction
import com.example.myapplication.utils.extensions.findActivity

/**
 * CompositionLocal that gives any screen access to [AccessSheetViewModel].
 *
 * Usage:
 * ```kotlin
 * val accessVM = LocalAccessSheetViewModel.current
 * val scope = rememberCoroutineScope()
 * KidsActivityCard(...) {
 *     scope.launch {
 *         val allowed = accessVM.checkAccess(ModuleID.FILL_THE_BLANK)
 *         if (allowed) navController.navigate(RouteNavigation.FillTheBlank.route)
 *     }
 * }
 * ```
 */
val LocalAccessSheetViewModel = compositionLocalOf<AccessSheetViewModel> {
    error("No AccessSheetViewModel provided. Wrap your NavGraph with AccessBottomSheetHost.")
}

/**
 * Wraps the entire NavGraph.
 * - Provides [LocalAccessSheetViewModel] to all children.
 * - Renders Daily Limit / Login / Paywall sheets.
 * - Overlays the Parental Gate before Login or Paywall.
 */
@Composable
fun AccessBottomSheetHost(
    viewModel: AccessSheetViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val sheetState        by viewModel.sheetState.collectAsState()
    val isLoading         by viewModel.isLoading.collectAsState()
    val packages          by viewModel.packages.collectAsState()
    val isTrialEligible   by viewModel.isTrialEligible.collectAsState()
    val showParentalGate  by viewModel.showParentalGate.collectAsState()
    val scope            = rememberCoroutineScope()

    // One-shot toast messages
    LaunchedEffect(Unit) {
        viewModel.message.collect { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        CompositionLocalProvider(LocalAccessSheetViewModel provides viewModel) {
            content()
        }

        // ── Daily Limit Sheet ─────────────────────────────────────────
        KidsBottomSheet(
            visible = sheetState is AccessSheetState.DailyLimit,
            onDismiss = { viewModel.dismiss() }
        ) {
            val state = sheetState as? AccessSheetState.DailyLimit
            if (state != null) {
                DailyLimitSheet(
                    canUnlockWithLogin = state.canUnlockWithLogin,
                    onLoginClick = {
                        // Show login sheet for the same module via parental gate
                        viewModel.requestState(AccessSheetState.Login(state.moduleId))
                    },
                    onPremiumClick = {
                        // Show paywall for the same module via parental gate
                        viewModel.requestState(AccessSheetState.Paywall(state.moduleId))
                    },
                    onDismiss = { viewModel.dismiss() }
                )
            }
        }

        // ── Login Sheet ───────────────────────────────────────────────
        KidsBottomSheet(
            visible = sheetState is AccessSheetState.Login,
            onDismiss = { viewModel.dismiss() }
        ) {
            LoginSheet(
                isLoading = isLoading,
                onGoogleSignIn = { activity?.let { viewModel.signInWithGoogle(it) } },
//                onAppleSignIn  = { activity?.let { viewModel.signInWithApple(it) } },
                onAppleSignIn = {
                    context.findActivity()?.let {
                        viewModel.signInWithApple(it)
                    }
                },
                onDismiss = { viewModel.dismiss() }
            )
        }

        // ── Paywall Sheet — slightly taller to fit plan rows + restore link ──
        KidsBottomSheet(
            visible = sheetState is AccessSheetState.Paywall,
            onDismiss = { viewModel.dismiss() },
            heightFraction = PaywallSheetHeightFraction
        ) {
            PaywallSheet(
                packages        = packages,
                isLoading       = isLoading,
                isTrialEligible = isTrialEligible,
                onPurchase      = { pkg -> activity?.let { viewModel.purchase(it, pkg) } },
                onRestore       = { viewModel.restorePurchases() },
                onDismiss       = { viewModel.dismiss() }
            )
        }

        // ── Premium Celebration Sheet ─────────────────────────────────
        KidsBottomSheet(
            visible = sheetState is AccessSheetState.PremiumCelebration,
            onDismiss = { viewModel.dismiss() },
            wrapContent = true
        ) {
            PremiumCelebrationSheet(
                onRateNow = {
                    val act = activity ?: return@PremiumCelebrationSheet
                    scope.launch { viewModel.rateNowAndDismiss(act) }
                },
                onLater = { viewModel.dismiss() }
            )
        }

        // ── Parental Gate — overlays everything ───────────────────────
        AnimatedVisibility(
            visible = showParentalGate,
            enter = fadeIn(animationSpec = tween(200)),
            exit  = fadeOut(animationSpec = tween(200))
        ) {
            ParentalGateDialog(
                onPassed    = { viewModel.parentalGatePassed() },
                onCancelled = { viewModel.parentalGateCancelled() }
            )
        }

    } // end Box
}
