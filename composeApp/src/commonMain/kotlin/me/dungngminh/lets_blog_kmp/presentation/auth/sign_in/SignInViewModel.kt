package me.dungngminh.lets_blog_kmp.presentation.auth.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.dungngminh.lets_blog_kmp.commons.MIN_PASSWORD_LENGTH
import me.dungngminh.lets_blog_kmp.commons.extensions.isValidEmail

enum class SignInValidationError {
    EMPTY_EMAIL,
    EMPTY_PASSWORD,
    INVALID_EMAIL,
    PASSWORD_TOO_SHORT,
    NONE,
}

data class SignInState(
    val email: String = "",
    val password: String = "",
    val emailError: SignInValidationError = SignInValidationError.NONE,
    val passwordError: SignInValidationError = SignInValidationError.NONE,
    val isLoading: Boolean = false,
    val passwordVisible: Boolean = true,
    val isSignInFormValid: Boolean = false,
    val error: String? = null,
)

class SignInViewModel : ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state

    val currentState: SignInState
        get() = _state.value

    fun changeEmail(email: String) {
        val emailInput = email.trim()
        val emailError =
            when {
                emailInput.isEmpty() -> SignInValidationError.EMPTY_EMAIL
                !emailInput.isValidEmail() -> SignInValidationError.INVALID_EMAIL
                else -> SignInValidationError.NONE
            }
        val isSignInFormValid = isFormValid(emailError = emailError)
        _state.update {
            it.copy(
                email = email,
                emailError = emailError,
                isSignInFormValid = isSignInFormValid,
            )
        }
    }

    fun changePassword(password: String) {
        val passwordError =
            when {
                password.isBlank() -> SignInValidationError.EMPTY_PASSWORD
                password.length < MIN_PASSWORD_LENGTH -> SignInValidationError.PASSWORD_TOO_SHORT
                else -> SignInValidationError.NONE
            }

        val isSignInFormValid = isFormValid(passwordError = passwordError)

        _state.update {
            it.copy(
                password = password,
                passwordError = passwordError,
                isSignInFormValid = isSignInFormValid,
            )
        }
    }

    fun togglePasswordVisibility() {
        _state.update {
            it.copy(passwordVisible = !it.passwordVisible)
        }
    }

    fun signIn() {
        _state.update {
            it.copy(isLoading = true)
        }
        // TODO call api
        viewModelScope.launch {
            delay(2000)
            _state.update {
                it.copy(isLoading = false)
            }
        }
    }

    private fun isFormValid(
        emailError: SignInValidationError? = null,
        passwordError: SignInValidationError? = null,
    ): Boolean {
        val isEmailValueValid =
            (emailError ?: currentState.emailError) == SignInValidationError.NONE
        val isPasswordValueValid =
            (passwordError ?: currentState.passwordError) == SignInValidationError.NONE
        return isEmailValueValid && isPasswordValueValid
    }
}
