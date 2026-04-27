package com.example.myapplication.main.common.permission

sealed interface NotificationPermissionUiState {
    data object Idle : NotificationPermissionUiState
    data object NotRequired : NotificationPermissionUiState
    data object Requesting : NotificationPermissionUiState
    data object Granted : NotificationPermissionUiState
    data object Denied : NotificationPermissionUiState
}
