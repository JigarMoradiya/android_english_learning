package com.example.myapplication.main.base.force_update

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.R
import com.example.myapplication.main.common.CustomPopupView

@Composable
fun ForceUpdateHandler(
    viewModel: ForceUpdateViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.checkForUpdate() }

    val isUpdateRequired = uiState is ForceUpdateUiState.UpdateRequired

    AnimatedVisibility(
        visible = isUpdateRequired,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        CustomPopupView(
            description = stringResource(R.string.force_update_description),
            positiveButtonText = stringResource(R.string.force_update_positive),
            icon = R.drawable.ic_new_version,
            widthMultiplier = 0.5f,
            isShowConfettiRainEffect = false,
            onPositiveTapped = {
                val pkg = context.packageName
                val marketIntent = Intent(
                    Intent.ACTION_VIEW,
                    "market://details?id=$pkg".toUri()
                ).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
                runCatching { context.startActivity(marketIntent) }
                    .onFailure {
                        if (it is ActivityNotFoundException) {
                            val webIntent = Intent(
                                Intent.ACTION_VIEW,
                                "https://play.google.com/store/apps/details?id=$pkg".toUri()
                            ).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
                            context.startActivity(webIntent)
                        }
                    }
                viewModel.dismiss()
            }
        )
    }
}
