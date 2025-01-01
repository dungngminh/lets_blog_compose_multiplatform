package me.dungngminh.lets_blog_kmp.presentation.detail_blog

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.repositories.BlogRepository

data class DetailBlogState(
    val blog: Blog,
)

class DetailBlogViewModel(
    blog: Blog,
    private val blogRepository: BlogRepository,
) : ScreenModel {
    private val _uiState = MutableStateFlow(DetailBlogState(blog = blog))
    val uiState =
        _uiState.asStateFlow()

    val currentBlogState: DetailBlogState
        get() = _uiState.value

    fun favoriteBlog() {
        val currentBlog = currentBlogState.blog
        val updatedBlog = currentBlog.copy(isFavoriteByUser = true)
        _uiState.update {
            it.copy(blog = updatedBlog)
        }
        screenModelScope.launch {
            blogRepository.favoriteBlog(currentBlog.id)
        }
    }

    fun unFavoriteBlog() {
        val currentBlog = currentBlogState.blog
        val updatedBlog = currentBlog.copy(isFavoriteByUser = false)
        _uiState.update {
            it.copy(blog = updatedBlog)
        }
        screenModelScope.launch {
            blogRepository.unFavoriteBlog(currentBlog.id)
        }
    }
}
