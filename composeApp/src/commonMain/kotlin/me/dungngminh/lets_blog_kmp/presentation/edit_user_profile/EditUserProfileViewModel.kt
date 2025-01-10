package me.dungngminh.lets_blog_kmp.presentation.edit_user_profile

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.dungngminh.lets_blog_kmp.commons.MIN_USERNAME_LENGTH
import me.dungngminh.lets_blog_kmp.domain.entities.User
import me.dungngminh.lets_blog_kmp.domain.repositories.UploadDocumentRepository
import me.dungngminh.lets_blog_kmp.domain.repositories.UserRepository

enum class EditUserProfileValidationError {
    EMPTY_USERNAME,
    USERNAME_TOO_SHORT,
    NONE,
}

data class EditUserProfileState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedImagePath: PlatformFile? = null,
    val currentUserAvatarUrl: String? = null,
    val userName: String = "",
    val userNameError: EditUserProfileValidationError? = null,
    val isEditUserProfileFormValid: Boolean = false,
    val isEditUserProfileSuccess: Boolean = false,
    val editUserProfileError: String? = null,
) {
    companion object {
        fun fromUser(user: User): EditUserProfileState =
            EditUserProfileState(
                currentUserAvatarUrl = user.avatarUrl,
                userName = user.name,
                userNameError = EditUserProfileValidationError.NONE,
                isEditUserProfileFormValid = true,
            )
    }
}

class EditUserProfileViewModel(
    private val user: User,
    private val uploadDocumentRepository: UploadDocumentRepository,
    private val userRepository: UserRepository,
) : ScreenModel {
    private val _uiState = MutableStateFlow(EditUserProfileState.fromUser(user))
    val uiState = _uiState.asStateFlow()

    fun changeUsername(userName: String) {
        val usernameInput = userName.trim()
        val usernameError =
            when {
                usernameInput.isEmpty() -> EditUserProfileValidationError.EMPTY_USERNAME
                usernameInput.length < MIN_USERNAME_LENGTH -> EditUserProfileValidationError.USERNAME_TOO_SHORT
                else -> EditUserProfileValidationError.NONE
            }

        _uiState.update { state ->
            state.copy(
                userName = userName,
                userNameError = usernameError,
                isEditUserProfileFormValid = usernameError == EditUserProfileValidationError.NONE,
            )
        }
    }

    fun changeImageFile(imageFile: PlatformFile?) {
        _uiState.update { state ->
            state.copy(
                selectedImagePath = imageFile,
                currentUserAvatarUrl = null,
            )
        }
    }

    private fun performUploadImage(
        imageFile: PlatformFile,
        onSuccess: (String) -> Unit,
    ) {
        screenModelScope.launch {
            uploadDocumentRepository
                .uploadUserAvatar(imageFile)
                .onSuccess(onSuccess)
                .onFailure {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = it.message,
                        )
                    }
                }
        }
    }

    fun saveProfile() {
        fun startSaveProfile(avatarUrl: String) {
            screenModelScope.launch {
                userRepository
                    .editUserProfile(
                        user.copy(
                            name = uiState.value.userName,
                            avatarUrl = avatarUrl,
                        ),
                    ).onSuccess {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                isEditUserProfileSuccess = true,
                            )
                        }
                    }.onFailure {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                errorMessage = it.message,
                            )
                        }
                    }
            }
        }
        _uiState.update { state ->
            state.copy(
                isLoading = true,
                errorMessage = null,
            )
        }
        if (uiState.value.selectedImagePath == null) {
            startSaveProfile(uiState.value.currentUserAvatarUrl.orEmpty())
        } else {
            performUploadImage(
                imageFile = uiState.value.selectedImagePath!!,
            ) { avatarUrl ->
                startSaveProfile(avatarUrl)
            }
        }
    }

    fun onErrorShown() {
        _uiState.update {
            it.copy(
                errorMessage = null,
            )
        }
    }
}
