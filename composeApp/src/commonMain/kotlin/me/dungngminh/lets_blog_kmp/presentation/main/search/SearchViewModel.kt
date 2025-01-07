package me.dungngminh.lets_blog_kmp.presentation.main.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hoc081098.flowext.flowFromSuspend
import com.hoc081098.flowext.startWith
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.repositories.BlogRepository

sealed class SearchUiState {
    data object Idle : SearchUiState()

    data object Loading : SearchUiState()

    data object EmptyQuery : SearchUiState()

    data class Success(
        val searchedBlogs: List<Blog> = emptyList(),
    ) : SearchUiState()

    data object EmptyResult : SearchUiState()

    data class Error(
        val errorMessage: String,
    ) : SearchUiState()
}

class SearchViewModel(
    private val blogRepository: BlogRepository,
) : ScreenModel {
    var searchFieldState by mutableStateOf("")
        private set

    private val retryFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val searchFlow =
        snapshotFlow { searchFieldState }
            .debounce(SEARCH_DEBOUNCE_TIME)
            .flatMapLatest { query ->
                if (query.trim().length < SEARCH_MIN_QUERY_LENGTH) {
                    flowOf(SearchUiState.EmptyQuery)
                } else {
                    flowFromSuspend {
                        blogRepository
                            .getBlogs(
                                searchQuery = query,
                                limit = 10,
                                offset = 1,
                                blogCategory = null,
                            )
                    }.map { result ->
                        result.fold(
                            onSuccess = { blogs ->
                                SearchUiState.Success(searchedBlogs = blogs)
                            },
                            onFailure = { error ->
                                SearchUiState.Error(
                                    errorMessage = error.message ?: "Unknown error",
                                )
                            },
                        )
                    }.startWith(SearchUiState.Loading)
                }
            }

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchState =
        retryFlow
            .startWith(Unit)
            .flatMapLatest { searchFlow }
            .stateIn(
                scope = screenModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SearchUiState.Idle,
            )

    fun onSearchChange(searchQuery: String) {
        searchFieldState = searchQuery
    }

    fun retry() {
        if (searchState.value is SearchUiState.Error) {
            screenModelScope.launch {
                retryFlow.emit(Unit)
            }
        }
    }

    companion object {
        private const val SEARCH_MIN_QUERY_LENGTH = 3
        private const val SEARCH_DEBOUNCE_TIME = 350L
    }
}
