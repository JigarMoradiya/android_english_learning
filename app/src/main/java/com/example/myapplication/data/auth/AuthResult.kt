package com.example.myapplication.data.auth

/**
 * All possible outcomes of a sign-in attempt.
 *
 * Success            → user is signed in, [userId] is the Firebase UID
 * NeedsAccountLinking→ same email exists with a different provider;
 *                       show dialog asking user to sign in with [existingProvider]
 * Cancelled          → user dismissed the sign-in dialog
 * Error              → something went wrong, [message] has details
 */
sealed class AuthResult {
    data class Success(val userId: String) : AuthResult()

    data class NeedsAccountLinking(
        val existingProvider: String,   // e.g. "google.com" or "apple.com"
        val email: String
    ) : AuthResult()

    object Cancelled : AuthResult()
    data class Error(val message: String) : AuthResult()
}
