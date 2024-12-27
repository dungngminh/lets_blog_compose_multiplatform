package me.dungngminh.lets_blog_kmp.presentation.main.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.repositories.BlogRepository

data class HomeScreenUiState(
    val trendingBlogs: List<Blog> = emptyList(),
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

    private var currentPage = 1

    private fun fetchBlogs() {
        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
                blogs = persistentListOf(),
            )
        }
        screenModelScope.launch {
            blogRepository
                .getBlogs(
                    searchQuery = null,
                    limit = 10,
                    offset = currentPage,
                    blogCategory = null,
                ).onSuccess { fetchedBlogs ->
                    _uiState.update { state ->
                        state.copy(
                            blogs = fetchedBlogs,
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

    fun refreshBlogs() {
        currentPage = 1
        fetchBlogs()
    }
}
