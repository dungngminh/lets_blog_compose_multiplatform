package me.dungngminh.lets_blog_kmp.presentation.preview_blog

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.aakira.napier.Napier
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.entities.BlogCategory
import me.dungngminh.lets_blog_kmp.domain.repositories.BlogRepository
import me.dungngminh.lets_blog_kmp.domain.repositories.UploadDocumentRepository

enum class CreateBlogPreviewValidationError {
    EMPTY,
    NONE,
}

data class PreviewBlogUiState(
    val title: String = "",
    val titleError: CreateBlogPreviewValidationError? = null,
    val imageFile: PlatformFile? = null,
    val networkImageUrl: String? = null,
    val imageError: CreateBlogPreviewValidationError? = null,
    val category: BlogCategory = BlogCategory.entries.first(),
    val isLoading: Boolean = false,
    val isFormValid: Boolean = false,
    val errorMessage: String? = null,
    val isPublishSuccess: Boolean = false,
) {
    companion object {
        fun fromBlog(blog: Blog): PreviewBlogUiState =
            PreviewBlogUiState(
                title = blog.title,
                titleError = CreateBlogPreviewValidationError.NONE,
                networkImageUrl = blog.imageUrl,
                imageError = CreateBlogPreviewValidationError.NONE,
                category = blog.category,
                isFormValid = true,
            )
    }
}

class PreviewBlogViewModel(
    private val content: String,
    blog: Blog? = null,
    private val blogRepository: BlogRepository,
    private val uploadDocumentRepository: UploadDocumentRepository,
) : ScreenModel {
    private val _uiState =
        MutableStateFlow(
            blog?.let(PreviewBlogUiState::fromBlog) ?: PreviewBlogUiState(),
        )

    val uiState = _uiState.asStateFlow()

    private val currentState: PreviewBlogUiState
        get() = uiState.value

    init {
        uiState
            .onEach {
                Napier.d("PreviewBlogViewModel: $it")
            }.launchIn(screenModelScope)
    }

    fun changeTitle(title: String) {
        val titleError =
            when {
                title.isEmpty() -> CreateBlogPreviewValidationError.EMPTY
                else -> CreateBlogPreviewValidationError.NONE
            }

        val isFormValid = isFormValid(titleError = titleError)
        _uiState.update { state ->
            state.copy(
                title = title,
                titleError = titleError,
                isFormValid = isFormValid,
            )
        }
    }

    fun changeImageFile(imageFile: PlatformFile?) {
        val imagePathError =
            when (imageFile) {
                null -> CreateBlogPreviewValidationError.EMPTY
                else -> CreateBlogPreviewValidationError.NONE
            }

        val isFormValid = isFormValid(imageError = imagePathError)
        _uiState.update { state ->
            state.copy(
                imageFile = imageFile,
                imageError = imagePathError,
                isFormValid = isFormValid,
            )
        }
    }

    fun deleteImage() {
        _uiState.update {
            it.copy(
                imageFile = null,
                networkImageUrl = null,
                imageError = CreateBlogPreviewValidationError.EMPTY,
                isFormValid = false,
            )
        }
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
        performUploadImage(currentState.imageFile!!, onSuccess = ::createBlog)
    }

    fun editBlog(blog: Blog) {
        // upload image to server
        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
            )
        }
        val imageFile = currentState.imageFile
        if (imageFile != null) {
            performUploadImage(imageFile) { imageUrl ->
                updateBlog(
                    blog =
                        blog.copy(
                            title = currentState.title,
                            content = content,
                            imageUrl = imageUrl,
                            category = currentState.category,
                        ),
                )
            }
        } else {
            updateBlog(
                blog =
                    blog.copy(
                        title = currentState.title,
                        content = content,
                        imageUrl = currentState.networkImageUrl!!,
                        category = currentState.category,
                    ),
            )
        }
    }

    private fun performUploadImage(
        imageFile: PlatformFile,
        onSuccess: (String) -> Unit,
    ) {
        screenModelScope.launch {
            uploadDocumentRepository
                .uploadBlogImage(imageFile)
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

    private fun updateBlog(blog: Blog) {
        screenModelScope.launch {
            blogRepository
                .updateBlog(blog)
                .onSuccess {
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
        imageError: CreateBlogPreviewValidationError? = null,
    ): Boolean {
        val titleValidationError = titleError ?: currentState.titleError
        val imagePathValidationError = imageError ?: currentState.imageError
        return listOf(titleValidationError, imagePathValidationError)
            .all { it == CreateBlogPreviewValidationError.NONE }
    }
}
