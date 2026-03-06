package uk.ac.tees.mad.mystudyplanner.presentation.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun onEmailChange(email: String) =
        _uiState.update { it.copy(email = email) }

    fun onPasswordChange(password: String) =
        _uiState.update { it.copy(password = password) }

    fun toggleMode() =
        _uiState.update {
            it.copy(
                mode = if (it.mode == AuthMode.LOGIN)
                    AuthMode.REGISTER else AuthMode.LOGIN,
                errorMessage = null
            )
        }

    fun submit(onSuccess: () -> Unit) {
        val state = _uiState.value
        _uiState.update { it.copy(isLoading = true) }

        val task = if (state.mode == AuthMode.LOGIN) {
            auth.signInWithEmailAndPassword(state.email, state.password)
        } else {
            auth.createUserWithEmailAndPassword(state.email, state.password)
        }

        task
            .addOnSuccessListener {
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            }
            .addOnFailureListener { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage ?: "Auth failed"
                    )
                }
            }
    }

    fun clearError() =
        _uiState.update { it.copy(errorMessage = null) }
}
