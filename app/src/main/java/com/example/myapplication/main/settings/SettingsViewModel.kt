package com.example.myapplication.main.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.access.AccessManager
import com.example.myapplication.data.access.UserAccessState
import com.example.myapplication.data.auth.AuthManager
import com.example.myapplication.data.purchase.PurchaseManager
import com.example.myapplication.data.purchase.RevenueCatManager
import com.example.myapplication.utilities.BGMusicManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

// ── Subscription info shown in the user status card ───────────────────────────
data class SubscriptionInfo(
    val planName: String,    // "Monthly Plan" / "Yearly Plan"
    val renewalDate: String, // "Renews Jun 21, 2026" / "Active"
    val price: String = ""   // "$3.99" / "₹299" etc.
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val purchaseManager: PurchaseManager,
    private val accessManager: AccessManager,
    private val revenueCatManager: RevenueCatManager,
    private val bgMusicManager: BGMusicManager,
) : ViewModel() {

    val userState: StateFlow<UserAccessState> = accessManager.userState

    // ── Subscription info (only populated for premium users) ─────────────────
    private val _subscriptionInfo = MutableStateFlow<SubscriptionInfo?>(null)
    val subscriptionInfo: StateFlow<SubscriptionInfo?> = _subscriptionInfo.asStateFlow()

    init {
        viewModelScope.launch {
            accessManager.userState.collect { state ->
                if (state.isPremium) loadSubscriptionInfo() else _subscriptionInfo.value = null
            }
        }
    }

    private suspend fun loadSubscriptionInfo() {
        val info = revenueCatManager.getCustomerInfo() ?: return
        val entitlement = info.entitlements[RevenueCatManager.ENTITLEMENT_ID] ?: return

        val productId = entitlement.productIdentifier
        val planName = if (productId.contains("monthly", ignoreCase = true)) "Monthly Plan" else "Yearly Plan"

        val renewalDate = entitlement.expirationDate?.let {
            "Renews ${SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(it)}"
        } ?: "Active"

        // Price must come from offerings, not entitlement
        val price = purchaseManager.getOfferings()
            ?.current?.availablePackages
            ?.firstOrNull { it.product.purchasingData.productId == productId }
            ?.product?.price?.let { "$${"%.2f".format(it)}" }
            ?: ""

        _subscriptionInfo.value = SubscriptionInfo(planName, renewalDate, price)
    }

    var musicVolume by mutableFloatStateOf(bgMusicManager.getVolume())
        private set

    fun updateMusicVolume(volume: Float) {
        bgMusicManager.setVolume(volume)
        musicVolume = volume
    }

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
