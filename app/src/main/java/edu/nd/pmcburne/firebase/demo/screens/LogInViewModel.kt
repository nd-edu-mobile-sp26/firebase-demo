package edu.nd.pmcburne.firebase.demo.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.nd.pmcburne.firebase.demo.repositories.FirebaseAuthRepository
import kotlinx.coroutines.launch

class LogInViewModel(
    private val authRepository: FirebaseAuthRepository
) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isLoggedIn by mutableStateOf(authRepository.isUserSignedIn())
    var currentUser by mutableStateOf(authRepository.currentUser)


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
                isLoggedIn = authRepository.isUserSignedIn()
                if (!isLoggedIn) {
                    errorMessage = "Sign in failed. Please check your credentials."
                }
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
                isLoggedIn = authRepository.isUserSignedIn()
                if (!isLoggedIn) {
                    errorMessage = "Sign up failed."
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "An error occurred"
            } finally {
                isLoading = false
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
        isLoggedIn = false
        email = ""
        password = ""
    }
}