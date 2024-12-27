package me.dungngminh.lets_blog_kmp.presentation.main.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hoc081098.flowext.flatMapFirst
import com.hoc081098.flowext.flowFromSuspend
import com.hoc081098.flowext.startWith
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.repositories.BlogRepository

sealed class HomeScreenUiState {
    data object Initial : HomeScreenUiState()

    data object Loading : HomeScreenUiState()

    data class Loaded(
        val blogs: ImmutableList<Blog> = persistentListOf(),
    ) : HomeScreenUiState()

    data class Error(
        val message: String? = null,
    ) : HomeScreenUiState()
}

class HomeScreenViewModel(
    private val blogRepository: BlogRepository,
) : ScreenModel {
    private val loadMorePage = MutableStateFlow(1)

    val blogState =
        loadMorePage
            .filter { it > 0 }
            .flatMapFirst { page ->
                flowFromSuspend {
                    blogRepository.getBlogs(
                        limit = DEFAULT_PAGE_SITE,
                        offset = page,
                    )
                }
            }.map { result ->
                result.fold(onSuccess = {
                    HomeScreenUiState.Loaded(it.toImmutableList())
                }, onFailure = {
                    HomeScreenUiState.Error(it.message)
                })
            }.startWith(HomeScreenUiState.Loading)
            .stateIn(
                scope = screenModelScope,
                started = SharingStarted.Lazily,
                initialValue = HomeScreenUiState.Initial,
            )

    fun loadMore() {
        loadMorePage.update { it + 1 }
    }

    fun retry() {
        loadMorePage.update { 0 }
        loadMorePage.update { 1 }
    }

    companion object {
        private const val DEFAULT_PAGE_SITE = 10
    }
}
