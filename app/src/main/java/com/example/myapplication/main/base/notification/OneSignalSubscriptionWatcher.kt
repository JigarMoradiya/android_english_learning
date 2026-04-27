package com.example.myapplication.main.base.notification

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun OneSignalSubscriptionWatcher(
    viewModel: OneSignalSubscriptionViewModel = hiltViewModel(),
    onState: (OneSignalSubscriptionUiState) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is OneSignalSubscriptionUiState.Subscribed -> {
                Log.d(
                    "OneSignalSubscription",
                    "SUBSCRIBED id=${state.subscriptionId} token=${state.pushToken}"
                )
            }
            OneSignalSubscriptionUiState.NotSubscribed -> {
                Log.d("OneSignalSubscription", "NOT SUBSCRIBED")
            }
            OneSignalSubscriptionUiState.Loading -> Unit
        }
        onState(uiState)
    }
}
