package me.dungngminh.lets_blog_kmp.presentation.sign_up

import cafe.adriel.voyager.core.model.ScreenModel

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
    val error: String = "",
    val usernameError: SignUpValidationError = SignUpValidationError.NONE,
    val emailError: SignUpValidationError = SignUpValidationError.NONE,
    val passwordError: SignUpValidationError = SignUpValidationError.NONE,
    val confirmPasswordError: SignUpValidationError = SignUpValidationError.NONE,
    val isSignUpFormValid: Boolean = false,
)

class SignUpViewModel : ScreenModel
