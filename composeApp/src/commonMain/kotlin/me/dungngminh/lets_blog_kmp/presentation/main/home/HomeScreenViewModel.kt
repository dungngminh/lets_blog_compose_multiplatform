package me.dungngminh.lets_blog_kmp.presentation.main.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.dungngminh.lets_blog_kmp.commons.extensions.replaceFirst
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.repositories.BlogRepository

data class HomeScreenUiState(
    val popularBlogs: List<Blog> = emptyList(),
    val blogs: List<Blog> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)

class HomeScreenViewModel(
    private val blogRepository: BlogRepository,
) : ScreenModel {
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState =
        _uiState
            .onStart {
                fetchBlogs()
            }.stateIn(
                scope = screenModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HomeScreenUiState(),
            )

    val currentState: HomeScreenUiState
        get() = uiState.value

    private var currentPage = 1

    fun fetchBlogs() {
        currentPage = 1
        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
                blogs = emptyList(),
                popularBlogs = emptyList(),
            )
        }
        screenModelScope.launch {
            val popularBlogsRequest = async { blogRepository.getTopBlogs(limit = 5) }
            val blogsRequest =
                async {
                    blogRepository.getBlogs(
                        limit = 10,
                        offset = currentPage,
                    )
                }
            val popularBlogs =
                popularBlogsRequest
                    .await()
                    .onFailure {
                        Napier.e("Error: $it")
                    }.getOrNull() ?: emptyList()
            val blogsRequestResult = blogsRequest.await()

            blogsRequestResult
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            popularBlogs = popularBlogs,
                            blogs = it,
                            isLoading = false,
                        )
                    }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message,
                        )
                    }
                }
        }
    }

    fun loadMoreBlogs() {
        currentPage++
        screenModelScope.launch {
            blogRepository
                .getBlogs(
                    limit = 10,
                    offset = currentPage,
                    blogCategory = null,
                ).onSuccess { fetchedBlogs ->
                    _uiState.update { state ->
                        state.copy(blogs = state.blogs + fetchedBlogs)
                    }
                }
        }
    }

    fun favoritePopularBlog(blog: Blog) {
        val updatedBlogs =
            currentState.popularBlogs.replaceFirst(
                predicate = { it.id == blog.id },
                transform = { it.copy(isFavoriteByUser = true) },
            )
        _uiState.update { state ->
            state.copy(popularBlogs = updatedBlogs)
        }
        screenModelScope.launch {
            blogRepository.favoriteBlog(blog.id)
        }
    }

    fun unFavoritePopularBlog(blog: Blog) {
        val updatedBlogs =
            currentState.popularBlogs.replaceFirst(
                predicate = { it.id == blog.id },
                transform = { it.copy(isFavoriteByUser = false) },
            )
        _uiState.update { state ->
            state.copy(popularBlogs = updatedBlogs)
        }
        screenModelScope.launch {
            blogRepository.unFavoriteBlog(blog.id)
        }
    }
}
