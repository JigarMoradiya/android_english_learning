package com.example.myapplication.main.base.notification

import android.util.Log
import androidx.lifecycle.ViewModel
import com.onesignal.OneSignal
import com.onesignal.user.subscriptions.IPushSubscriptionObserver
import com.onesignal.user.subscriptions.PushSubscriptionChangedState
import com.onesignal.user.subscriptions.PushSubscriptionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OneSignalSubscriptionViewModel @Inject constructor() : ViewModel() {

    private val _uiState =
        MutableStateFlow<OneSignalSubscriptionUiState>(OneSignalSubscriptionUiState.Loading)
    val uiState: StateFlow<OneSignalSubscriptionUiState> = _uiState.asStateFlow()

    private val observer = object : IPushSubscriptionObserver {
        override fun onPushSubscriptionChange(state: PushSubscriptionChangedState) {
            _uiState.value = state.current.toUiState()
            Log.d(TAG, "subscription changed -> ${_uiState.value}")
        }
    }

    init {
        val sub = OneSignal.User.pushSubscription
        _uiState.value = mapToUiState(
            id = sub.id,
            token = sub.token,
            optedIn = sub.optedIn
        )
        OneSignal.User.pushSubscription.addObserver(observer)
        Log.d(TAG, "initial state -> ${_uiState.value}")
    }

    fun refresh() {
        val sub = OneSignal.User.pushSubscription
        _uiState.value = mapToUiState(
            id = sub.id,
            token = sub.token,
            optedIn = sub.optedIn
        )
    }

    override fun onCleared() {
        OneSignal.User.pushSubscription.removeObserver(observer)
        super.onCleared()
    }

    private fun PushSubscriptionState.toUiState(): OneSignalSubscriptionUiState =
        mapToUiState(id = id, token = token, optedIn = optedIn)

    private fun mapToUiState(
        id: String,
        token: String,
        optedIn: Boolean
    ): OneSignalSubscriptionUiState {
        return if (optedIn && id.isNotEmpty() && token.isNotEmpty()) {
            OneSignalSubscriptionUiState.Subscribed(subscriptionId = id, pushToken = token)
        } else {
            OneSignalSubscriptionUiState.NotSubscribed
        }
    }

    companion object {
        private const val TAG = "OneSignalSubscription"
    }
}
