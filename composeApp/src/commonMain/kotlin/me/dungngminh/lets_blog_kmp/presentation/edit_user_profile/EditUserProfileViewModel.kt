package me.dungngminh.lets_blog_kmp.presentation.edit_user_profile

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.dungngminh.lets_blog_kmp.domain.entities.User
import me.dungngminh.lets_blog_kmp.domain.repositories.UploadDocumentRepository
import me.dungngminh.lets_blog_kmp.domain.repositories.UserRepository

data class EditUserProfileState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedImagePath: PlatformFile? = null,
    val currentUserAvatarUrl: String? = null,
    val fullName: String = "",
    val isEditUserProfileSuccess: Boolean = false,
) {
    companion object {
        fun fromUser(user: User): EditUserProfileState =
            EditUserProfileState(
                currentUserAvatarUrl = user.avatarUrl,
                fullName = user.name,
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

    fun changeFullName(fullName: String) {
        _uiState.update { state ->
            state.copy(
                fullName = fullName,
            )
        }
    }

    fun changeImageFile(imageFile: PlatformFile) {
        _uiState.update { state ->
            state.copy(
                selectedImagePath = imageFile,
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
        performUploadImage(
            imageFile = uiState.value.selectedImagePath!!,
        ) { avatarUrl ->
            screenModelScope.launch {
                userRepository
                    .editUserProfile(
                        user.copy(
                            name = uiState.value.fullName,
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
    }
}
