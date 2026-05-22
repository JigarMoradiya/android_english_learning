package com.example.myapplication.data.auth

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.myapplication.data.access.AccessManager
import com.example.myapplication.data.access.UserAccessState
import com.example.myapplication.data.purchase.RevenueCatManager
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handles all authentication flows:
 *  - Google Sign-In  (via Credential Manager API — modern, not deprecated)
 *  - Apple Sign-In   (via Firebase OAuthProvider)
 *  - Account linking (same email, different provider)
 *  - Sign-out
 *
 * After every state change, updates [AccessManager] so the whole
 * app instantly reflects the new login / premium status.
 *
 * ── SETUP REQUIRED ──────────────────────────────────────────────────
 * WEB_CLIENT_ID below is the Web client (type 3) from your
 * Firebase Console → Project Settings → General →
 * Your Android app → OAuth 2.0 client (type: Web client).
 * This is already the correct ID from google-services.json.
 * ────────────────────────────────────────────────────────────────────
 */

private const val TAG = "AuthManager"

@Singleton
class AuthManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val accessManager: AccessManager,
    private val revenueCatManager: RevenueCatManager
) {

    companion object {
        // Web client (type 3) from google-services.json — required for Google Sign-In
        private const val WEB_CLIENT_ID =
            "650587239957-9c4v3tmo8ibcgcfhja81fuj3cgalfv7s.apps.googleusercontent.com"
    }

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val credentialManager: CredentialManager = CredentialManager.create(context)

    // ── Google Sign-In (Credential Manager) ──────────────────────────

    /**
     * Launches the Credential Manager bottom sheet for Google Sign-In.
     * Everything — account picker, token fetch, Firebase sign-in — happens
     * inside this single suspend call. No intent or onActivityResult needed.
     *
     * @param activity  The current foreground Activity (required by Credential Manager)
     */
    suspend fun signInWithGoogle(activity: Activity): AuthResult {
        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)   // show all accounts, not just previously used
                .setServerClientId(WEB_CLIENT_ID)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val credentialResponse = credentialManager.getCredential(
                context = activity,
                request = request
            )

            val googleIdTokenCredential =
                GoogleIdTokenCredential.createFrom(credentialResponse.credential.data)
            val idToken = googleIdTokenCredential.idToken

            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(firebaseCredential).await()
            val uid = result.user?.uid ?: return AuthResult.Error("No UID returned")
            Log.d(TAG, "signInWithGoogle → SUCCESS uid=$uid")
            onAuthSuccess(uid)
            AuthResult.Success(uid)

        } catch (e: GetCredentialCancellationException) {
            Log.d(TAG, "signInWithGoogle → Cancelled by user")
            AuthResult.Cancelled
        } catch (e: Exception) {
            Log.e(TAG, "signInWithGoogle → Error: ${e.message}")
            handleAuthException(e, provider = "google")
        }
    }

    // ── Apple Sign-In ─────────────────────────────────────────────────

    /**
     * Starts the Apple Sign-In flow using Firebase OAuthProvider.
     * Must be called from an Activity context.
     *
     * @param activity  The current Activity (required by Firebase for web-based OAuth)
     */
    suspend fun signInWithApple(activity: Activity): AuthResult {
        Log.d(TAG, "signInWithApple → starting")
        return try {
            val provider = OAuthProvider.newBuilder("apple.com")
                .setScopes(listOf("email", "name"))
                .build()
            val pending = firebaseAuth.pendingAuthResult
            val task = if (pending != null) {
                Log.d(TAG, "Using pending auth result")
                pending
            } else {
                Log.d(TAG, "Starting new Apple auth flow")
                firebaseAuth.startActivityForSignInWithProvider(activity, provider)
            }

            val result = task.await()
            Log.d(TAG, "signInWithApple → launching OAuthProvider browser flow")
            val uid = result.user?.uid ?: return AuthResult.Error("No UID returned")
            Log.d(TAG, "signInWithApple → SUCCESS uid=$uid")
            onAuthSuccess(uid)
            AuthResult.Success(uid)
        } catch (e: Exception) {
            Log.e(TAG, "signInWithApple → EXCEPTION type=${e::class.java.simpleName} message=${e.message}", e)
            handleAuthException(e, provider = "apple")
        }
    }

    // ── Account Linking ───────────────────────────────────────────────

    /**
     * Links a new Google credential to the existing Firebase account.
     * Call this after [AuthResult.NeedsAccountLinking] is returned to resolve
     * a credential collision on the same email.
     *
     * @param activity  The current foreground Activity
     */
    suspend fun linkWithGoogle(activity: Activity): AuthResult {
        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(WEB_CLIENT_ID)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val credentialResponse = credentialManager.getCredential(
                context = activity,
                request = request
            )

            val googleIdTokenCredential =
                GoogleIdTokenCredential.createFrom(credentialResponse.credential.data)
            val idToken = googleIdTokenCredential.idToken

            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            val currentUser = firebaseAuth.currentUser
                ?: return AuthResult.Error("No current user to link")

            currentUser.linkWithCredential(firebaseCredential).await()
            val uid = currentUser.uid
            onAuthSuccess(uid)
            AuthResult.Success(uid)

        } catch (e: GetCredentialCancellationException) {
            AuthResult.Cancelled
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Google linking failed")
        }
    }

    /**
     * Links a new Apple credential to the existing Firebase account.
     * Call this after [AuthResult.NeedsAccountLinking] to resolve a
     * credential collision on the same email.
     */
    suspend fun linkWithApple(activity: Activity): AuthResult {
        return try {
            val provider = OAuthProvider.newBuilder("apple.com")
                .setScopes(listOf("email", "name"))
                .build()

            val currentUser = firebaseAuth.currentUser
                ?: return AuthResult.Error("No current user to link")

            currentUser.startActivityForLinkWithProvider(activity, provider).await()
            val uid = currentUser.uid
            onAuthSuccess(uid)
            AuthResult.Success(uid)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Apple linking failed")
        }
    }

    // ── Sign-out ──────────────────────────────────────────────────────

    fun signOut() {
        firebaseAuth.signOut()
        CoroutineScope(Dispatchers.IO).launch {
            // Clear saved Google credential so the picker shows on next sign-in
            try { credentialManager.clearCredentialState(ClearCredentialStateRequest()) }
            catch (e: Exception) { /* ignore */ }
            revenueCatManager.reset()
        }
        accessManager.updateUserState(UserAccessState.Guest)
    }

    // ── Session restore on app launch ─────────────────────────────────

    /**
     * Call once on app start (e.g. in MainActivity or a splash screen).
     * Restores the user's session if they were previously signed in.
     */
    fun restoreSession() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            Log.d(TAG, "restoreSession → found user uid=${user.uid}, checking premium...")
            CoroutineScope(Dispatchers.IO).launch {
                onAuthSuccess(user.uid)
            }
        } else {
            Log.d(TAG, "restoreSession → no user found, setting Guest")
            accessManager.updateUserState(UserAccessState.Guest)
        }
    }

    // ── Current user ──────────────────────────────────────────────────

    val currentUserId: String?
        get() = firebaseAuth.currentUser?.uid

    val isLoggedIn: Boolean
        get() = firebaseAuth.currentUser != null

    // ── Private helpers ───────────────────────────────────────────────

    /**
     * Called after every successful sign-in, link, or session restore.
     * Identifies the user in RevenueCat and updates AccessManager with the
     * correct premium/free state. Always awaited — never fire-and-forget.
     */
    private suspend fun onAuthSuccess(uid: String) {
        // identify() calls RC logIn and returns fresh CustomerInfo in one round-trip.
        val customerInfo = revenueCatManager.identify(uid)
        val isPremium = if (customerInfo != null) {
            customerInfo.entitlements[RevenueCatManager.ENTITLEMENT_ID]?.isActive == true
        } else {
            // logIn failed (network issue) — fall back to a direct server fetch.
            revenueCatManager.isPremium()
        }
        val state = if (isPremium) UserAccessState.PremiumUser(uid)
                    else           UserAccessState.FreeUser(uid)
        Log.d(TAG, "onAuthSuccess → uid=$uid isPremium=$isPremium state=$state")
        accessManager.updateUserState(state)
    }

    private fun handleAuthException(
        e: Exception,
        provider: String = ""
    ): AuthResult {
        val errorCode =
            (e as? com.google.firebase.auth.FirebaseAuthUserCollisionException)?.errorCode

        Log.e(TAG, "handleAuthException → provider=$provider errorCode=$errorCode exceptionClass=${e::class.java.name} message=${e.message}")

        return if (errorCode == "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL") {
            val email =
                (e as com.google.firebase.auth.FirebaseAuthUserCollisionException).email
                    ?: return AuthResult.Error("Account conflict — email unknown")

            // Infer the existing provider from whichever one we just tried
            val existingProvider = if (provider == "google") "apple.com" else "google.com"
            Log.d(TAG, "handleAuthException → NeedsAccountLinking existingProvider=$existingProvider email=$email")
            AuthResult.NeedsAccountLinking(existingProvider, email)
        } else {
            val msg = e.message ?: ""
            val result = when {
                msg.contains("CANCELLED", ignoreCase = true) -> AuthResult.Cancelled
                msg.contains("sign_in_cancelled", ignoreCase = true) -> AuthResult.Cancelled
                else -> AuthResult.Error(msg.ifBlank { "Sign-in failed" })
            }
            Log.e(TAG, "handleAuthException → returning result=$result msg='$msg'")
            result
        }
    }
}
