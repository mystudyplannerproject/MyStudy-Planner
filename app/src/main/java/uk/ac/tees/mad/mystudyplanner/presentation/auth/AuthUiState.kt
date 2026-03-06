package uk.ac.tees.mad.mystudyplanner.presentation.auth

import android.util.Patterns

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val mode: AuthMode = AuthMode.LOGIN,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val isEmailValid get() = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordValid get() = password.length >= 6

    val canSubmit get() = isEmailValid && isPasswordValid && !isLoading
}


enum class AuthMode { LOGIN, REGISTER }
