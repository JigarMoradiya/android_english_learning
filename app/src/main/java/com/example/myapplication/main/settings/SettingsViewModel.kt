package com.example.myapplication.main.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.access.AccessManager
import com.example.myapplication.data.access.UserAccessState
import com.example.myapplication.data.auth.AuthManager
import com.example.myapplication.data.purchase.PurchaseManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val purchaseManager: PurchaseManager,
    private val accessManager: AccessManager,
) : ViewModel() {

    val userState: StateFlow<UserAccessState> = accessManager.userState

    // ── Parental gate ─────────────────────────────────────────────────────────

    enum class ParentalAction { Logout, Restore }

    private val _showParentalGate = MutableStateFlow(false)
    val showParentalGate: StateFlow<Boolean> = _showParentalGate.asStateFlow()

    private var pendingAction: ParentalAction = ParentalAction.Logout

    fun requestParentalGate(action: ParentalAction) {
        pendingAction = action
        _showParentalGate.value = true
    }

    fun dismissParentalGate() {
        _showParentalGate.value = false
    }

    fun executeAction() {
        _showParentalGate.value = false
        when (pendingAction) {
            ParentalAction.Logout  -> authManager.signOut()
            ParentalAction.Restore -> viewModelScope.launch { purchaseManager.restorePurchases() }
        }
    }
}
