package me.dungngminh.lets_blog_kmp.presentation.detail_blog

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.repositories.BlogRepository
import me.dungngminh.lets_blog_kmp.domain.repositories.FavoriteRepository

data class DetailBlogState(
    val blog: Blog,
    val isDeleteSuccess: Boolean = false,
    val deleteError: String? = null,
)

sealed class SummaryBlogContentState {
    data object Initial : SummaryBlogContentState()

    data object Loading : SummaryBlogContentState()

    data class Error(
        val message: String,
    ) : SummaryBlogContentState()

    data class Success(
        val summaryContent: String,
    ) : SummaryBlogContentState()
}

class DetailBlogViewModel(
    blog: Blog,
    private val favoriteRepository: FavoriteRepository,
    private val blogRepository: BlogRepository,
) : ScreenModel {
    private val _uiState = MutableStateFlow(DetailBlogState(blog = blog))
    val uiState =
        _uiState.asStateFlow()

    private val textBlogContent =
        RichTextState()
            .setHtml(blog.content)
            .toText()

//    private val summaryContentFlow =

    val currentBlogState: DetailBlogState
        get() = _uiState.value

    fun favoriteBlog() {
        val currentBlog = currentBlogState.blog
        val updatedBlog = currentBlog.copy(isFavoriteByUser = true)
        _uiState.update {
            it.copy(blog = updatedBlog)
        }
        screenModelScope.launch {
            favoriteRepository.favoriteBlog(currentBlog.id)
        }
    }

    fun unFavoriteBlog() {
        val currentBlog = currentBlogState.blog
        val updatedBlog = currentBlog.copy(isFavoriteByUser = false)
        _uiState.update {
            it.copy(blog = updatedBlog)
        }
        screenModelScope.launch {
            favoriteRepository.unFavoriteBlog(currentBlog.id)
        }
    }

    fun deleteBlog() {
        val currentBlog = currentBlogState.blog
        screenModelScope.launch {
            blogRepository
                .deleteBlog(currentBlog.id)
                .onSuccess {
                    _uiState.update {
                        it.copy(isDeleteSuccess = true)
                    }
                }.onFailure {
                    _uiState.update { state ->
                        state.copy(deleteError = it.message)
                    }
                }
        }
    }
}
