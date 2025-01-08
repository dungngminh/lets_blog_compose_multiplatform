package me.dungngminh.lets_blog_kmp.presentation.register

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hoc081098.flowext.flowFromSuspend
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import me.dungngminh.lets_blog_kmp.commons.MIN_PASSWORD_LENGTH
import me.dungngminh.lets_blog_kmp.commons.MIN_USERNAME_LENGTH
import me.dungngminh.lets_blog_kmp.commons.extensions.isValidEmail
import me.dungngminh.lets_blog_kmp.domain.repositories.AuthRepository

enum class RegisterValidationError {
    EMPTY_USERNAME,
    USERNAME_TOO_SHORT,
    EMPTY_EMAIL,
    EMPTY_PASSWORD,
    INVALID_EMAIL,
    PASSWORD_TOO_SHORT,
    PASSWORDS_NOT_MATCH,
    NONE,
}

data class RegisterState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val passwordVisible: Boolean = true,
    val confirmPasswordVisible: Boolean = true,
    val error: String? = null,
    val usernameError: RegisterValidationError? = null,
    val emailError: RegisterValidationError? = null,
    val passwordError: RegisterValidationError? = null,
    val confirmPasswordError: RegisterValidationError? = null,
    val isRegisterFormValid: Boolean = false,
    val isRegisterSuccess: Boolean = false,
)

class RegisterViewModel(
    private val authRepository: AuthRepository,
) : ScreenModel {
    private val _state = MutableStateFlow(RegisterState())
    val state = _state

    val currentState: RegisterState
        get() = _state.value

    fun changeUsername(username: String) {
        val usernameInput = username.trim()
        val usernameError =
            when {
                usernameInput.isEmpty() -> RegisterValidationError.EMPTY_USERNAME
                usernameInput.length < MIN_USERNAME_LENGTH -> RegisterValidationError.USERNAME_TOO_SHORT
                else -> RegisterValidationError.NONE
            }

        val isSignUpFormValid = isFormValid(usernameError = usernameError)
        _state.update {
            it.copy(
                username = username,
                usernameError = usernameError,
                isRegisterFormValid = isSignUpFormValid,
            )
        }
    }

    fun changeEmail(email: String) {
        val emailError =
            when {
                email.isEmpty() -> RegisterValidationError.EMPTY_EMAIL
                !email.isValidEmail() -> RegisterValidationError.INVALID_EMAIL
                else -> RegisterValidationError.NONE
            }

        val isSignUpFormValid = isFormValid(emailError = emailError)
        _state.update {
            it.copy(
                email = email,
                emailError = emailError,
                isRegisterFormValid = isSignUpFormValid,
            )
        }
    }

    fun changePassword(password: String) {
        val passwordError =
            when {
                password.isEmpty() -> RegisterValidationError.EMPTY_PASSWORD
                password.length < MIN_PASSWORD_LENGTH -> RegisterValidationError.PASSWORD_TOO_SHORT
                else -> RegisterValidationError.NONE
            }

        val isSignUpFormValid = isFormValid(passwordError = passwordError)
        _state.update {
            it.copy(
                password = password,
                passwordError = passwordError,
                isRegisterFormValid = isSignUpFormValid,
            )
        }
    }

    fun changeConfirmPassword(confirmPassword: String) {
        val confirmPasswordError =
            when {
                confirmPassword.isEmpty() -> RegisterValidationError.EMPTY_PASSWORD
                confirmPassword != currentState.password -> RegisterValidationError.PASSWORDS_NOT_MATCH
                else -> RegisterValidationError.NONE
            }

        val isSignUpFormValid = isFormValid(confirmPasswordError = confirmPasswordError)
        _state.update {
            it.copy(
                confirmPassword = confirmPassword,
                confirmPasswordError = confirmPasswordError,
                isRegisterFormValid = isSignUpFormValid,
            )
        }
    }

    fun togglePasswordVisibility() {
        _state.update {
            it.copy(passwordVisible = !it.passwordVisible)
        }
    }

    fun toggleConfirmPasswordVisibility() {
        _state.update {
            it.copy(confirmPasswordVisible = !it.confirmPasswordVisible)
        }
    }

    fun register() {
        flowFromSuspend {
            authRepository.register(
                name = currentState.username,
                email = currentState.email,
                password = currentState.password,
                confirmPassword = currentState.confirmPassword,
            )
        }.onStart {
            _state.update { it.copy(isLoading = true) }
        }.onEach { result ->
            result.fold(
                onSuccess = {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRegisterSuccess = true,
                        )
                    }
                },
                onFailure = {
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            error = it.message,
                        )
                    }
                },
            )
        }.launchIn(screenModelScope)
    }

    fun onErrorShown() {
        _state.update {
            it.copy(error = null)
        }
    }

    private fun isFormValid(
        usernameError: RegisterValidationError? = null,
        emailError: RegisterValidationError? = null,
        passwordError: RegisterValidationError? = null,
        confirmPasswordError: RegisterValidationError? = null,
    ): Boolean {
        val usernameValidationError = usernameError ?: currentState.usernameError
        val emailValidationError = emailError ?: currentState.emailError
        val passwordValidationError = passwordError ?: currentState.passwordError
        val confirmPasswordValidationError =
            confirmPasswordError
                ?: currentState.confirmPasswordError

        return listOf(
            usernameValidationError,
            emailValidationError,
            passwordValidationError,
            confirmPasswordValidationError,
        ).all { it == RegisterValidationError.NONE }
    }
}
