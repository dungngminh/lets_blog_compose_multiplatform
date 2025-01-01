package me.dungngminh.lets_blog_kmp.presentation.create_blog.preview

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.dungngminh.lets_blog_kmp.domain.entities.BlogCategory
import me.dungngminh.lets_blog_kmp.domain.repositories.BlogRepository
import me.dungngminh.lets_blog_kmp.domain.repositories.UploadDocumentRepository

enum class CreateBlogPreviewValidationError {
    EMPTY,
    NONE,
}

data class CreateBlogPreviewUiState(
    val title: String = "",
    val titleError: CreateBlogPreviewValidationError? = null,
    val imageFile: PlatformFile? = null,
    val imagePathError: CreateBlogPreviewValidationError? = null,
    val category: BlogCategory = BlogCategory.entries.first(),
    val isLoading: Boolean = false,
    val isFormValid: Boolean = false,
    val errorMessage: String? = null,
    val isPublishSuccess: Boolean = false,
)

class PreviewBlogViewModel(
    private val content: String,
    private val blogRepository: BlogRepository,
    private val uploadDocumentRepository: UploadDocumentRepository,
) : ScreenModel {
    private val _uiState = MutableStateFlow(CreateBlogPreviewUiState())
    val uiState = _uiState.asStateFlow()

    val currentState: CreateBlogPreviewUiState
        get() = uiState.value

    fun changeTitle(title: String) {
        val titleError =
            when {
                title.isEmpty() -> CreateBlogPreviewValidationError.EMPTY
                else -> CreateBlogPreviewValidationError.NONE
            }

        val isFormValid = isFormValid(titleError = titleError)
        _uiState.value =
            currentState.copy(
                title = title,
                titleError = titleError,
                isFormValid = isFormValid,
            )
    }

    fun changeImageFile(imageFile: PlatformFile?) {
        val imagePathError =
            when (imageFile) {
                null -> CreateBlogPreviewValidationError.EMPTY
                else -> CreateBlogPreviewValidationError.NONE
            }

        val isFormValid = isFormValid(imagePathError = imagePathError)
        _uiState.value =
            currentState.copy(
                imageFile = imageFile,
                imagePathError = imagePathError,
                isFormValid = isFormValid,
            )
    }

    fun changeCategory(category: BlogCategory) {
        _uiState.update {
            it.copy(category = category)
        }
    }

    fun publishBlog() {
        // upload image to server
        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
            )
        }
        screenModelScope.launch {
            uploadDocumentRepository
                .uploadBlogImage(currentState.imageFile!!)
                .onSuccess(::createBlog)
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

    private fun createBlog(imageUrl: String) {
        screenModelScope.launch {
            blogRepository
                .createBlog(
                    title = currentState.title,
                    content = content,
                    imageUrl = imageUrl,
                    blogCategory = currentState.category,
                ).onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isPublishSuccess = true,
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

    fun onErrorMessageShown() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }

    private fun isFormValid(
        titleError: CreateBlogPreviewValidationError? = null,
        imagePathError: CreateBlogPreviewValidationError? = null,
    ): Boolean {
        val titleValidationError = titleError ?: currentState.titleError
        val imagePathValidationError = imagePathError ?: currentState.imagePathError
        return listOf(titleValidationError, imagePathValidationError)
            .all { it == CreateBlogPreviewValidationError.NONE }
    }
}
