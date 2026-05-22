package com.example.myapplication.data.purchase

import android.app.Activity
import com.example.myapplication.data.access.AccessManager
import com.example.myapplication.data.access.UserAccessState
import com.google.firebase.auth.FirebaseAuth
import com.revenuecat.purchases.Purchases
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
 *  - Update AccessManager after any successful purchase
 */
@Singleton
class PurchaseManager @Inject constructor(
    private val revenueCatManager: RevenueCatManager,
    private val accessManager: AccessManager
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
                if (userCancelled) cont.resume(PurchaseResult.Cancelled)
                else cont.resume(PurchaseResult.Error(error.message))
            },
            onSuccess = { _, _ ->
                // Always fetch fresh customer info from server after purchase.
                Purchases.sharedInstance.getCustomerInfoWith(
                    onError = { cont.resume(PurchaseResult.Success) },
                    onSuccess = { freshInfo ->
                        // Prefer uid from local state, fall back to Firebase Auth
                        val uid = accessManager.userState.value.userId
                            ?: FirebaseAuth.getInstance().currentUser?.uid
                        if (freshInfo.entitlements[RevenueCatManager.ENTITLEMENT_ID]?.isActive == true && uid != null) {
                            accessManager.updateUserState(UserAccessState.PremiumUser(uid))
                        }
                        cont.resume(PurchaseResult.Success)
                    }
                )
            }
        )
    }

    // ── Restore ───────────────────────────────────────────────────────

    /**
     * Restores previous purchases. Updates AccessManager if premium is found.
     * Returns true if premium entitlement was restored.
     */
    suspend fun restorePurchases(): Boolean {
        val restored = revenueCatManager.restorePurchases()
        if (restored) {
            // Prefer uid from local state, fall back to Firebase Auth
            val uid = accessManager.userState.value.userId
                ?: FirebaseAuth.getInstance().currentUser?.uid
            if (uid != null) {
                accessManager.updateUserState(UserAccessState.PremiumUser(uid))
            }
        }
        return restored
    }
}
