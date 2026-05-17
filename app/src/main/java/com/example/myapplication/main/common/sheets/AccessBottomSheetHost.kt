package com.example.myapplication.main.common.sheets

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

/**
 * CompositionLocal that gives any screen access to the [AccessSheetViewModel].
 * Use this to call [AccessSheetViewModel.checkAccess] before navigating.
 *
 * Usage in a screen:
 * ```kotlin
 * val accessVM = LocalAccessSheetViewModel.current
 * val scope = rememberCoroutineScope()
 *
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
 * - Provides [LocalAccessSheetViewModel] to all child composables.
 * - Observes [AccessSheetViewModel.sheetState] and renders the correct sheet.
 * - Shows Toast messages from the ViewModel.
 */
@Composable
fun AccessBottomSheetHost(
    viewModel: AccessSheetViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val sheetState by viewModel.sheetState.collectAsState()
    val isLoading  by viewModel.isLoading.collectAsState()
    val packages   by viewModel.packages.collectAsState()

    // Show one-shot toast messages
    LaunchedEffect(Unit) {
        viewModel.message.collect { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    // Box ensures the sheets overlay the content correctly (fillMaxSize in
    // KidsBottomSheet needs a bounded parent — without this Box the scrim
    // renders invisible / zero-size and no sheet ever appears).
    Box(modifier = Modifier.fillMaxSize()) {

        // Provide the ViewModel down the tree
        androidx.compose.runtime.CompositionLocalProvider(
            LocalAccessSheetViewModel provides viewModel
        ) {
            content()
        }

        // ── Daily Limit Sheet ─────────────────────────────────────────────
        KidsBottomSheet(
        visible = sheetState is AccessSheetState.DailyLimit,
        onDismiss = { viewModel.dismiss() }
    ) {
        val state = sheetState as? AccessSheetState.DailyLimit
        if (state != null) {
            DailyLimitSheet(
                canUnlockWithLogin = state.canUnlockWithLogin,
                onLoginClick = {
                    viewModel.dismiss()
                    // Show login sheet for the same module
                    // ViewModel will re-check access after sign-in
                    activity?.let { viewModel.signInWithGoogle(it) }
                },
                onPremiumClick = {
                    // Transition to paywall
                    viewModel.dismiss()
                    // Re-trigger as paywall for the same module
                    // (AccessManager will return SubscribeRequired after login)
                },
                onDismiss = { viewModel.dismiss() }
            )
        }
    }

    // ── Login Sheet ───────────────────────────────────────────────────
    KidsBottomSheet(
        visible = sheetState is AccessSheetState.Login,
        onDismiss = { viewModel.dismiss() }
    ) {
        LoginSheet(
            isLoading = isLoading,
            onGoogleSignIn = {
                activity?.let { viewModel.signInWithGoogle(it) }
            },
            onAppleSignIn = {
                activity?.let { viewModel.signInWithApple(it) }
            },
            onDismiss = { viewModel.dismiss() }
        )
    }

    // ── Paywall Sheet ─────────────────────────────────────────────────
    KidsBottomSheet(
        visible = sheetState is AccessSheetState.Paywall,
        onDismiss = { viewModel.dismiss() }
    ) {
        PaywallSheet(
            packages   = packages,
            isLoading  = isLoading,
            onPurchase = { pkg ->
                activity?.let { viewModel.purchase(it, pkg) }
            },
            onRestore = { viewModel.restorePurchases() },
            onDismiss = { viewModel.dismiss() }
        )
    }

    } // end Box
}
