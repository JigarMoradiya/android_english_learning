package com.example.myapplication.utils.extensions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.utils.AppConstants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object OtherEx {
    var isActionEnabled by mutableStateOf(true)
        private set
    fun ViewModel.safeAction(action: () -> Unit) {
        if (!isActionEnabled) return

        isActionEnabled = false
        action()

        viewModelScope.launch {
            delay(AppConstants.clickDisableTime)
            isActionEnabled = true
        }
    }
}