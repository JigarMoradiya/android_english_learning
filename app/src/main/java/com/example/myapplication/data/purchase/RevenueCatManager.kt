package com.example.myapplication.data.purchase

import android.content.Context
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.LogLevel
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import com.revenuecat.purchases.getCustomerInfoWith
import com.revenuecat.purchases.logInWith
import com.revenuecat.purchases.logOutWith
import com.revenuecat.purchases.restorePurchasesWith
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

/**
 * Wraps the RevenueCat SDK.
 *
 * Responsibilities:
 *  - Initialize SDK on app start
 *  - Identify user (link Firebase UID to RevenueCat)
 *  - Check premium entitlement status
 *  - Restore purchases
 *
 * ── SETUP REQUIRED ──────────────────────────────────────────────────
 * Replace REVENUECAT_API_KEY with your Android public API key from:
 * RevenueCat Dashboard → Project Settings → API Keys → Android
 * It looks like: goog_xxxxxxxxxxxxxxxxxxxxxxxxxx
 *
 * Replace ENTITLEMENT_ID with the entitlement identifier you created
 * in RevenueCat Dashboard → Entitlements (e.g. "premium")
 * ────────────────────────────────────────────────────────────────────
 */
@Singleton
class RevenueCatManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        // TODO: Replace with your Android public API key from RevenueCat Dashboard
        private const val REVENUECAT_API_KEY = "goog_DSMibbramxekSjRRSlUGKCoeplf"

        // Must match the Entitlement identifier in RevenueCat Dashboard
        const val ENTITLEMENT_ID = "premium"

        // Product IDs — must match what you created in Google Play Console
        const val PRODUCT_MONTHLY = "english_premium_monthly"
        const val PRODUCT_YEARLY  = "english_premium_yearly"
    }

    // ── Initialization ────────────────────────────────────────────────

    /**
     * Call once from MyApplication.onCreate().
     * Sets up the RevenueCat SDK before any other usage.
     */
    fun initialize() {
        Purchases.logLevel = LogLevel.DEBUG  // set to LogLevel.ERROR in production
        Purchases.configure(
            PurchasesConfiguration.Builder(context, REVENUECAT_API_KEY).build()
        )
    }

    // ── User identity ─────────────────────────────────────────────────

    /**
     * Links the Firebase UID to RevenueCat so purchases sync across
     * devices and platforms. Call after every successful sign-in.
     * Returns fresh CustomerInfo from the logIn response — use it directly
     * to avoid a second network round-trip.
     */
    suspend fun identify(userId: String): CustomerInfo? =
        suspendCancellableCoroutine { cont ->
            Purchases.sharedInstance.logInWith(
                appUserID = userId,
                onError   = { cont.resume(null) },
                onSuccess = { customerInfo, _ -> cont.resume(customerInfo) }
            )
        }

    /**
     * Resets the RevenueCat identity on sign-out.
     * Creates an anonymous user so the app still works without login.
     */
    suspend fun reset(): Boolean =
        suspendCancellableCoroutine { cont ->
            Purchases.sharedInstance.logOutWith(
                onError   = { cont.resume(false) },
                onSuccess = { cont.resume(true) }
            )
        }

    // ── Entitlement check ─────────────────────────────────────────────

    /**
     * Returns true if the user has an active premium entitlement.
     * Always fetches fresh from RevenueCat server.
     */
    suspend fun isPremium(): Boolean =
        suspendCancellableCoroutine { cont ->
            Purchases.sharedInstance.getCustomerInfoWith(
                onError   = { cont.resume(false) },
                onSuccess = { info -> cont.resume(info.isPremiumActive()) }
            )
        }

    /**
     * Returns the full CustomerInfo object for advanced use cases.
     * Returns null if the fetch fails.
     */
    suspend fun getCustomerInfo(): CustomerInfo? =
        suspendCancellableCoroutine { cont ->
            Purchases.sharedInstance.getCustomerInfoWith(
                onError   = { cont.resume(null) },
                onSuccess = { cont.resume(it) }
            )
        }

    // ── Restore purchases ─────────────────────────────────────────────

    /**
     * Restores purchases for the current user (e.g. after reinstall or
     * switching to a new device with the same account).
     * Returns true if any entitlement was restored.
     */
    suspend fun restorePurchases(): Boolean =
        suspendCancellableCoroutine { cont ->
            Purchases.sharedInstance.restorePurchasesWith(
                onError   = { cont.resume(false) },
                onSuccess = { info -> cont.resume(info.isPremiumActive()) }
            )
        }

    // ── Helper ────────────────────────────────────────────────────────

    private fun CustomerInfo.isPremiumActive(): Boolean =
        entitlements[ENTITLEMENT_ID]?.isActive == true
}
