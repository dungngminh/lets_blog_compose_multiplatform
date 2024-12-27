package me.dungngminh.lets_blog_kmp.presentation.sign_in

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hoc081098.flowext.flowFromSuspend
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import me.dungngminh.lets_blog_kmp.commons.MIN_PASSWORD_LENGTH
import me.dungngminh.lets_blog_kmp.commons.extensions.isValidEmail
import me.dungngminh.lets_blog_kmp.domain.repositories.AuthRepository

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
    val emailError: SignInValidationError? = null,
    val passwordError: SignInValidationError? = null,
    val isLoading: Boolean = false,
    val passwordVisible: Boolean = true,
    val isSignInFormValid: Boolean = false,
    val error: String? = null,
    val isLoginSuccess: Boolean = false,
)

class SignInViewModel(
    private val authRepository: AuthRepository,
) : ScreenModel {
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
        flowFromSuspend {
            authRepository.login(
                email = currentState.email,
                password = currentState.password,
            )
        }.onStart {
            _state.update { it.copy(isLoading = true) }
        }.onEach { result ->
            result.fold(
                onSuccess = {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isLoginSuccess = true,
                        )
                    }
                },
                onFailure = {
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            error = it.message
                        )
                    }
                },
            )
        }.launchIn(screenModelScope)
    }

    fun onErrorMessageShown() {
        _state.update { it.copy(error = null) }
    }

    private fun isFormValid(
        emailError: SignInValidationError? = null,
        passwordError: SignInValidationError? = null,
    ): Boolean {
        val emailValidationError = emailError ?: currentState.emailError
        val passwordValidationError = passwordError ?: currentState.passwordError
        return listOf(emailValidationError, passwordValidationError)
            .all { it == SignInValidationError.NONE }
    }
}
