package me.dungngminh.lets_blog_kmp.presentation.sign_up

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

enum class SignUpValidationError {
    EMPTY_USERNAME,
    USERNAME_TOO_SHORT,
    EMPTY_EMAIL,
    EMPTY_PASSWORD,
    INVALID_EMAIL,
    PASSWORD_TOO_SHORT,
    PASSWORDS_NOT_MATCH,
    NONE,
}

data class SignUpState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val passwordVisible: Boolean = true,
    val confirmPasswordVisible: Boolean = true,
    val error: String? = null,
    val usernameError: SignUpValidationError? = null,
    val emailError: SignUpValidationError? = null,
    val passwordError: SignUpValidationError? = null,
    val confirmPasswordError: SignUpValidationError? = null,
    val isSignUpFormValid: Boolean = false,
    val isSignUpSuccess: Boolean = false,
)

class SignUpViewModel(
    private val authRepository: AuthRepository,
) : ScreenModel {
    private val _state = MutableStateFlow(SignUpState())
    val state = _state

    val currentState: SignUpState
        get() = _state.value

    fun changeUsername(username: String) {
        val usernameInput = username.trim()
        val usernameError =
            when {
                usernameInput.isEmpty() -> SignUpValidationError.EMPTY_USERNAME
                usernameInput.length < MIN_USERNAME_LENGTH -> SignUpValidationError.USERNAME_TOO_SHORT
                else -> SignUpValidationError.NONE
            }

        val isSignUpFormValid = isFormValid(usernameError = usernameError)
        _state.update {
            it.copy(
                username = username,
                usernameError = usernameError,
                isSignUpFormValid = isSignUpFormValid,
            )
        }
    }

    fun changeEmail(email: String) {
        val emailError =
            when {
                email.isEmpty() -> SignUpValidationError.EMPTY_EMAIL
                !email.isValidEmail() -> SignUpValidationError.INVALID_EMAIL
                else -> SignUpValidationError.NONE
            }

        val isSignUpFormValid = isFormValid(emailError = emailError)
        _state.update {
            it.copy(
                email = email,
                emailError = emailError,
                isSignUpFormValid = isSignUpFormValid,
            )
        }
    }

    fun changePassword(password: String) {
        val passwordError =
            when {
                password.isEmpty() -> SignUpValidationError.EMPTY_PASSWORD
                password.length < MIN_PASSWORD_LENGTH -> SignUpValidationError.PASSWORD_TOO_SHORT
                else -> SignUpValidationError.NONE
            }

        val isSignUpFormValid = isFormValid(passwordError = passwordError)
        _state.update {
            it.copy(
                password = password,
                passwordError = passwordError,
                isSignUpFormValid = isSignUpFormValid,
            )
        }
    }

    fun changeConfirmPassword(confirmPassword: String) {
        val confirmPasswordError =
            when {
                confirmPassword.isEmpty() -> SignUpValidationError.EMPTY_PASSWORD
                confirmPassword != currentState.password -> SignUpValidationError.PASSWORDS_NOT_MATCH
                else -> SignUpValidationError.NONE
            }

        val isSignUpFormValid = isFormValid(confirmPasswordError = confirmPasswordError)
        _state.update {
            it.copy(
                confirmPassword = confirmPassword,
                confirmPasswordError = confirmPasswordError,
                isSignUpFormValid = isSignUpFormValid,
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

    fun signUp() {
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
                            isSignUpSuccess = true,
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
        usernameError: SignUpValidationError? = null,
        emailError: SignUpValidationError? = null,
        passwordError: SignUpValidationError? = null,
        confirmPasswordError: SignUpValidationError? = null,
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
        ).all { it == SignUpValidationError.NONE }
    }
}
