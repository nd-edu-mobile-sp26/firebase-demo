package edu.nd.pmcburne.firebase.demo.repositories

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAuthRepository(
    var mockUser: FirebaseUser? = null
) : AuthRepository {
    override val currentUserFlow: Flow<FirebaseUser?> =
        flow { emit(mockUser) }
    override val currentUser: FirebaseUser? get() = mockUser

    override fun isUserSignedIn(): Boolean = mockUser != null
    override suspend fun signInWithEmail(email: String, password: String) { } //NO OP
    override suspend fun signUpWithEmail(email: String, password: String) { } // NO OP
    override fun signOut() { mockUser = null }
}