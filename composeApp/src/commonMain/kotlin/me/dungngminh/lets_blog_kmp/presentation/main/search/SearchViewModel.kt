package me.dungngminh.lets_blog_kmp.presentation.main.search

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.repositories.BlogRepository

data class SearchUiState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val searchedBlogs: List<Blog> = emptyList(),
)

class SearchViewModel(
    private val blogRepository: BlogRepository,
) : ScreenModel {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    val currentState: SearchUiState
        get() = uiState.value

    fun search(searchQuery: String) {
        _uiState.update {
            currentState.copy(
                isLoading = true,
                searchQuery = searchQuery,
            )
        }
        screenModelScope.launch {
            blogRepository
                .getBlogs(
                    searchQuery = searchQuery,
                    limit = 10,
                    offset = 1,
                    blogCategory = null,
                ).onSuccess { blogs ->
                    _uiState.update { state ->
                        state.copy(
                            searchedBlogs = blogs,
                            isLoading = false,
                        )
                    }
                }.onFailure {
                    _uiState.update { state ->
                        state.copy(
                            errorMessage = it.message,
                            isLoading = false,
                        )
                    }
                }
        }
    }
}
