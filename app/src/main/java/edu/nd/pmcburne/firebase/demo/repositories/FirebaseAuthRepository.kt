package edu.nd.pmcburne.firebase.demo.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * This class is used to authenticate a user with Firebase Authentication. This handles both login
 * and creating new accounts.
 */

class FirebaseAuthRepository(
    private val auth: FirebaseAuth
) {

    val currentUserFlow: Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        auth.addAuthStateListener(listener)
        trySend(auth.currentUser)
        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    fun isUserSignedIn(): Boolean = auth.currentUser != null

    suspend fun signInWithEmail(email: String, password: String) {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            Log.v("Authentication", "Sign in successful: ${auth.currentUser}")
        } catch (e: Exception) {
            Log.v("Authentication", "Sign in unsuccessful: ${e.message}")
            throw e
        }
    }

    suspend fun signUpWithEmail(email: String, password: String) {
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Log.v("Authentication", "Sign up successful ${auth.currentUser}")
        } catch (e: Exception) {
            Log.v("Authentication", "Error signing up: ${e.message}")
            throw e
        }
    }

    fun signOut() {
        auth.signOut()
    }
}
