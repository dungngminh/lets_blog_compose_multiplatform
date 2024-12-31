package me.dungngminh.lets_blog_kmp.presentation.create_blog.preview

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.dungngminh.lets_blog_kmp.domain.entities.BlogCategory
import me.dungngminh.lets_blog_kmp.domain.repositories.BlogRepository

enum class CreateBlogPreviewValidationError {
    EMPTY,
    NONE,
}

data class CreateBlogPreviewUiState(
    val title: String = "",
    val titleError: CreateBlogPreviewValidationError? = null,
    val imagePath: String = "",
    val imagePathError: CreateBlogPreviewValidationError? = null,
    val category: BlogCategory = BlogCategory.entries.first(),
    val categoryError: CreateBlogPreviewValidationError? = null,
    val isLoading: Boolean = true,
    val isFormValid: Boolean = false,
    val errorMessage: String? = null,
    val isPublishSuccess: Boolean = false,
)

class PreviewBlogViewModel(
    private val content: String,
    private val blogRepository: BlogRepository,
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

    fun changeImagePath(imagePath: String) {
        val imagePathError =
            when {
                imagePath.isEmpty() -> CreateBlogPreviewValidationError.EMPTY
                else -> CreateBlogPreviewValidationError.NONE
            }

        val isFormValid = isFormValid(imagePathError = imagePathError)
        _uiState.value =
            currentState.copy(
                imagePath = imagePath,
                imagePathError = imagePathError,
                isFormValid = isFormValid,
            )
    }

    fun changeCategory(category: BlogCategory) {
        val isFormValid = isFormValid(categoryError = CreateBlogPreviewValidationError.NONE)
        _uiState.value =
            currentState.copy(
                category = category,
                categoryError = CreateBlogPreviewValidationError.NONE,
                isFormValid = isFormValid,
            )
    }

    fun publishBlog() {
        screenModelScope.launch {
            blogRepository
                .createBlog(
                    title = currentState.title,
                    content = content,
                    imageUrl = currentState.imagePath,
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
        categoryError: CreateBlogPreviewValidationError? = null,
    ): Boolean {
        val titleValidationError = titleError ?: currentState.titleError
        val imagePathValidationError = imagePathError ?: currentState.imagePathError
        val categoryValidationError = categoryError ?: currentState.categoryError
        return listOf(titleValidationError, imagePathValidationError, categoryValidationError)
            .all { it == CreateBlogPreviewValidationError.NONE }
    }
}
