package com.example.myapplication.main.base.force_update

sealed interface ForceUpdateUiState {
    data object Idle : ForceUpdateUiState
    data object UpToDate : ForceUpdateUiState
    data class UpdateRequired(
        val installedVersion: Int,
        val requiredVersion: Int
    ) : ForceUpdateUiState
}
