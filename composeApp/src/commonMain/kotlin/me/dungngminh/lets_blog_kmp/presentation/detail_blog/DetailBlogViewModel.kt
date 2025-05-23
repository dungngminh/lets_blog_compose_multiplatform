package me.dungngminh.lets_blog_kmp.presentation.detail_blog

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hoc081098.flowext.flowFromSuspend
import com.hoc081098.flowext.startWith
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.repositories.BlogRepository
import me.dungngminh.lets_blog_kmp.domain.repositories.FavoriteRepository
import me.dungngminh.lets_blog_kmp.domain.repositories.SummaryContentRepository

data class DetailBlogState(
    val blog: Blog,
    val isLoading: Boolean = false,
    val isDeleteSuccess: Boolean = false,
    val isFavoriteSuccess: Boolean = false,
    val isUnFavoriteSuccess: Boolean = false,
    val deleteError: String? = null,
)

sealed class SummaryBlogContentState {
    data object Initial : SummaryBlogContentState()

    data object Loading : SummaryBlogContentState()

    data class Error(
        val message: String,
    ) : SummaryBlogContentState()

    data class Success(
        val summarizedContent: String,
    ) : SummaryBlogContentState()
}

class DetailBlogViewModel(
    blog: Blog,
    private val favoriteRepository: FavoriteRepository,
    private val blogRepository: BlogRepository,
    private val summaryContentRepository: SummaryContentRepository,
) : ScreenModel {
    private val _uiState = MutableStateFlow(DetailBlogState(blog = blog))
    val uiState =
        _uiState.asStateFlow()

    private val textBlogContent =
        RichTextState()
            .setHtml(blog.content)
            .toText()

    private val retrySummaryBlogFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    private val summaryContentFlow =
        flowFromSuspend {
            summaryContentRepository.summaryContent(textBlogContent)
        }.map { summaryResult ->
            summaryResult.fold(
                onSuccess = {
                    SummaryBlogContentState.Success(it.orEmpty())
                },
                onFailure = {
                    SummaryBlogContentState.Error(it.message ?: "Unknown Error")
                },
            )
        }.startWith(SummaryBlogContentState.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    val summaryContentState =
        retrySummaryBlogFlow
            .startWith(Unit)
            .flatMapLatest { summaryContentFlow }
            .stateIn(
                scope = screenModelScope,
                started = SharingStarted.Lazily,
                initialValue = SummaryBlogContentState.Initial,
            )

    private val currentBlogState: DetailBlogState
        get() = _uiState.value

    fun favoriteBlog() {
        val currentBlog = currentBlogState.blog
        val updatedBlog = currentBlog.copy(isFavoriteByUser = true)
        _uiState.update {
            it.copy(
                blog = updatedBlog,
                isFavoriteSuccess = true,
            )
        }
        screenModelScope.launch {
            favoriteRepository.favoriteBlog(currentBlog.id)
        }
    }

    fun unFavoriteBlog() {
        val currentBlog = currentBlogState.blog
        val updatedBlog = currentBlog.copy(isFavoriteByUser = false)
        _uiState.update {
            it.copy(
                blog = updatedBlog,
                isUnFavoriteSuccess = true,
            )
        }
        screenModelScope.launch {
            favoriteRepository.unFavoriteBlog(currentBlog.id)
        }
    }

    fun deleteBlog() {
        _uiState.update {
            it.copy(isLoading = true)
        }
        val currentBlog = currentBlogState.blog
        screenModelScope.launch {
            blogRepository
                .deleteBlog(currentBlog.id)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isDeleteSuccess = true,
                            isLoading = false,
                        )
                    }
                }.onFailure {
                    _uiState.update { state ->
                        state.copy(
                            deleteError = it.message,
                            isLoading = false,
                        )
                    }
                }
        }
    }

    fun onFavoriteSuccessShown() {
        _uiState.update {
            it.copy(isFavoriteSuccess = false)
        }
    }

    fun onUnFavoriteSuccessShown() {
        _uiState.update {
            it.copy(isUnFavoriteSuccess = false)
        }
    }

    fun onDeleteErrorShown() {
        _uiState.update {
            it.copy(deleteError = null)
        }
    }

    fun retrySummaryBlog() {
        screenModelScope.launch {
            if (summaryContentState.value is SummaryBlogContentState.Error) {
                retrySummaryBlogFlow.emit(Unit)
            }
        }
    }
}
