package com.example.myapplication.main.base.notification

sealed interface OneSignalSubscriptionUiState {
    data object Loading : OneSignalSubscriptionUiState
    data class Subscribed(
        val subscriptionId: String,
        val pushToken: String
    ) : OneSignalSubscriptionUiState
    data object NotSubscribed : OneSignalSubscriptionUiState
}
