package edu.nd.pmcburne.firebase.demo.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import edu.nd.pmcburne.firebase.demo.repositories.AuthRepository
import edu.nd.pmcburne.firebase.demo.repositories.FirebaseAuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LogInViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    val currentUser: StateFlow<FirebaseUser?> = authRepository.currentUserFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = authRepository.currentUser
        )

    val isLoggedIn: StateFlow<Boolean> = authRepository.currentUserFlow
        .map { it != null }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = authRepository.isUserSignedIn()
        )

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun signIn() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                authRepository.signInWithEmail(email, password)
                // Note: isLoggedIn and currentUser will update automatically via Flow
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "An error occurred"
            } finally {
                isLoading = false
            }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                authRepository.signUpWithEmail(email, password)
                // Note: isLoggedIn and currentUser will update automatically via Flow
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "An error occurred"
            } finally {
                isLoading = false
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
        email = ""
        password = ""
    }
}
