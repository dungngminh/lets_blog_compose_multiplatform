package me.dungngminh.lets_blog_kmp.presentation.login

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hoc081098.flowext.flowFromSuspend
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import me.dungngminh.lets_blog_kmp.commons.MIN_PASSWORD_LENGTH
import me.dungngminh.lets_blog_kmp.commons.extensions.isValidEmail
import me.dungngminh.lets_blog_kmp.commons.types.AppError
import me.dungngminh.lets_blog_kmp.data.mappers.fold
import me.dungngminh.lets_blog_kmp.domain.repositories.AuthRepository

enum class LoginValidationError {
    EMPTY_EMAIL,
    EMPTY_PASSWORD,
    INVALID_EMAIL,
    PASSWORD_TOO_SHORT,
    NONE,
}

data class LoginState(
    val email: String = "",
    val password: String = "",
    val emailError: LoginValidationError? = null,
    val passwordError: LoginValidationError? = null,
    val isLoading: Boolean = false,
    val passwordVisible: Boolean = true,
    val isLoginFormValid: Boolean = false,
    val error: AppError? = null,
    val isLoginSuccess: Boolean = false,
)

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ScreenModel {
    private val _state = MutableStateFlow(LoginState())
    val state = _state

    private val currentState: LoginState
        get() = _state.value

    fun changeEmail(email: String) {
        val emailInput = email.trim()
        val emailError =
            when {
                emailInput.isEmpty() -> LoginValidationError.EMPTY_EMAIL
                !emailInput.isValidEmail() -> LoginValidationError.INVALID_EMAIL
                else -> LoginValidationError.NONE
            }
        val isLoginFormValid = isFormValid(emailError = emailError)
        _state.update {
            it.copy(
                email = email,
                emailError = emailError,
                isLoginFormValid = isLoginFormValid,
            )
        }
    }

    fun changePassword(password: String) {
        val passwordError =
            when {
                password.isBlank() -> LoginValidationError.EMPTY_PASSWORD
                password.length < MIN_PASSWORD_LENGTH -> LoginValidationError.PASSWORD_TOO_SHORT
                else -> LoginValidationError.NONE
            }

        val isLoginFormValid = isFormValid(passwordError = passwordError)

        _state.update {
            it.copy(
                password = password,
                passwordError = passwordError,
                isLoginFormValid = isLoginFormValid,
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
                onFailure = { error ->
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            error = error,
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
        emailError: LoginValidationError? = null,
        passwordError: LoginValidationError? = null,
    ): Boolean {
        val emailValidationError = emailError ?: currentState.emailError
        val passwordValidationError = passwordError ?: currentState.passwordError
        return listOf(emailValidationError, passwordValidationError)
            .all { it == LoginValidationError.NONE }
    }
}
