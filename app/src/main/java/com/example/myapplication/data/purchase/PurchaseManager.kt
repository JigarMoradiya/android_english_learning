package com.example.myapplication.data.purchase

import android.app.Activity
import com.example.myapplication.data.access.ReviewManager
import com.revenuecat.purchases.CacheFetchPolicy
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesErrorCode
import com.revenuecat.purchases.getCustomerInfoWith
import com.revenuecat.purchases.getOfferingsWith
import com.revenuecat.purchases.purchaseWith
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

/**
 * Clean UI-facing purchase layer.
 *
 * ViewModels call this — they never touch RevenueCatManager directly.
 *
 * Responsibilities:
 *  - Load available subscription packages from RevenueCat offerings
 *  - Trigger a purchase flow for monthly or yearly plan
 *  - Restore previous purchases
 *  - Update AccessManager (via RevenueCatManager) after any successful purchase
 */
@Singleton
class PurchaseManager @Inject constructor(
    private val revenueCatManager: RevenueCatManager,
    private val reviewManager: ReviewManager
) {

    // ── Purchase result ───────────────────────────────────────────────

    sealed class PurchaseResult {
        object Success    : PurchaseResult()
        object Cancelled  : PurchaseResult()
        data class Error(val message: String) : PurchaseResult()
    }

    // ── Load offerings ────────────────────────────────────────────────

    /**
     * Fetches available packages from RevenueCat.
     * Returns a map of packageIdentifier → StoreProduct.
     * Returns empty map on failure.
     */
    suspend fun getOfferings(): com.revenuecat.purchases.Offerings? =
        suspendCancellableCoroutine { cont ->
            Purchases.sharedInstance.getOfferingsWith(
                onError   = { cont.resume(null) },
                onSuccess = { cont.resume(it) }
            )
        }

    // ── Purchase ──────────────────────────────────────────────────────

    /**
     * Starts a purchase flow for [packageToPurchase].
     * Call [getOfferings] first to get the package.
     *
     * @param activity          The current Activity (required by Google Play Billing)
     * @param packageToPurchase The RevenueCat Package from the current offering
     */
    suspend fun purchase(
        activity: Activity,
        packageToPurchase: com.revenuecat.purchases.Package
    ): PurchaseResult = suspendCancellableCoroutine { cont ->
        Purchases.sharedInstance.purchaseWith(
            purchaseParams = com.revenuecat.purchases.PurchaseParams
                .Builder(activity, packageToPurchase)
                .build(),
            onError = { error, userCancelled ->
                when {
                    userCancelled -> cont.resume(PurchaseResult.Cancelled)
                    // Pending (e.g. cash / slow payment) — Premium unlocks by itself once paid.
                    error.code == PurchasesErrorCode.PaymentPendingError ->
                        cont.resume(PurchaseResult.Error("Waiting for payment. Premium will unlock once it's complete."))
                    else -> cont.resume(PurchaseResult.Error(error.message))
                }
            },
            onSuccess = { _, purchaseInfo ->
                // Always fetch fresh customer info from server after purchase;
                // fall back to the purchase's own CustomerInfo if that fails.
                fun finish(info: CustomerInfo) {
                    revenueCatManager.applyCustomerInfo(info)
                    if (info.entitlements[RevenueCatManager.ENTITLEMENT_ID]?.isActive == true) {
                        reviewManager.onPremiumPurchased()
                        cont.resume(PurchaseResult.Success)
                    } else {
                        // Paid but the entitlement isn't active yet (store still processing) —
                        // don't celebrate a Premium the app hasn't unlocked.
                        cont.resume(PurchaseResult.Error(NOT_ACTIVE_YET_MESSAGE))
                    }
                }
                Purchases.sharedInstance.getCustomerInfoWith(
                    fetchPolicy = CacheFetchPolicy.FETCH_CURRENT,
                    onError = { finish(purchaseInfo) },
                    onSuccess = { finish(it) }
                )
            }
        )
    }

    private companion object {
        const val NOT_ACTIVE_YET_MESSAGE =
            "Your purchase is being processed. If Premium doesn't unlock in a minute, tap Restore Purchases."
    }

    // ── Restore ───────────────────────────────────────────────────────

    /**
     * Restores previous purchases from the device's store account.
     * AccessManager is updated by RevenueCatManager.
     * Returns true if premium entitlement was restored.
     */
    suspend fun restorePurchases(): Boolean = revenueCatManager.restorePurchases()
}
