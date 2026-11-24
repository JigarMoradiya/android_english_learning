package com.example.myapplication.data.purchase

import android.content.Context
import com.example.myapplication.data.access.AccessManager
import com.example.myapplication.data.access.UserAccessState
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.LogLevel
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import com.revenuecat.purchases.interfaces.UpdatedCustomerInfoListener
import com.revenuecat.purchases.getCustomerInfoWith
import com.revenuecat.purchases.getOfferingsWith
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
 *  - Initialize SDK on app start (anonymous RevenueCat ID — there is no sign-in)
 *  - Keep [AccessManager] in sync with the premium entitlement
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
    @ApplicationContext private val context: Context,
    private val accessManager: AccessManager
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
     * Sets up the RevenueCat SDK, then keeps the app's Premium state in sync:
     * the listener fires with cached info on launch and again whenever
     * entitlements change (purchase, restore, renewal, expiry).
     */
    fun initialize() {
        val debuggable = (context.applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0
        Purchases.logLevel = if (debuggable) LogLevel.DEBUG else LogLevel.ERROR
        Purchases.configure(
            PurchasesConfiguration.Builder(context, REVENUECAT_API_KEY).build()
        )
        Purchases.sharedInstance.updatedCustomerInfoListener =
            UpdatedCustomerInfoListener { info -> applyCustomerInfo(info) }
        Purchases.sharedInstance.getCustomerInfoWith(
            onError   = { },
            onSuccess = { info -> applyCustomerInfo(info) }
        )
    }

    /** Pushes the entitlement in [info] into [AccessManager]. */
    fun applyCustomerInfo(info: CustomerInfo) {
        accessManager.updateUserState(
            if (info.isPremiumActive()) UserAccessState.Premium else UserAccessState.Free
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

    // ── Intro offer eligibility ───────────────────────────────────────

    /**
     * Returns true if the current offering has a free trial on any package.
     * On Android, Google Play enforces user-level eligibility at purchase time —
     * we just check whether the product has a trial offer configured.
     */
    suspend fun isEligibleForIntroOffer(): Boolean =
        suspendCancellableCoroutine { cont ->
            Purchases.sharedInstance.getOfferingsWith(
                onError = { cont.resume(false) },
                onSuccess = { offerings ->
                    val packages = offerings.current?.availablePackages ?: emptyList()
                    val hasTrial = packages.any { pkg ->
                        pkg.product.defaultOption?.freePhase != null
                    }
                    cont.resume(hasTrial)
                }
            )
        }

    // ── Restore purchases ─────────────────────────────────────────────

    /**
     * Restores purchases tied to the device's Play account (e.g. after a
     * reinstall or on a new device) and updates [AccessManager].
     * Returns true if any entitlement was restored.
     */
    suspend fun restorePurchases(): Boolean =
        suspendCancellableCoroutine { cont ->
            Purchases.sharedInstance.restorePurchasesWith(
                onError   = { cont.resume(false) },
                onSuccess = { info ->
                    applyCustomerInfo(info)
                    cont.resume(info.isPremiumActive())
                }
            )
        }

    // ── Helper ────────────────────────────────────────────────────────

    private fun CustomerInfo.isPremiumActive(): Boolean =
        entitlements[ENTITLEMENT_ID]?.isActive == true
}
