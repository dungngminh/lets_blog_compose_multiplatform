package me.dungngminh.lets_blog_kmp.presentation.main.favorite

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hoc081098.flowext.flowFromSuspend
import com.hoc081098.flowext.startWith
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.repositories.FavoriteRepository

sealed class FavoriteUiState {
    data object Loading : FavoriteUiState()

    data object NoFavoriteBlogs : FavoriteUiState()

    data class Success(
        val blogs: ImmutableList<Blog> = persistentListOf(),
    ) : FavoriteUiState()

    data class Error(
        val message: String,
    ) : FavoriteUiState()
}

class FavoriteViewModel(
    private val favoriteRepository: FavoriteRepository,
) : ScreenModel {
    private val refreshFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    private val retryFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    private val favoriteBlogFlow =
        flowFromSuspend { favoriteRepository.getFavoriteBlogs() }
            .mapToFavoriteState()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState =
        merge(
            refreshFlow
                .flatMapLatest { favoriteBlogFlow },
            // init and retry
            retryFlow
                .startWith(Unit)
                .flatMapLatest {
                    favoriteBlogFlow
                        .startWith(FavoriteUiState.Loading)
                },
        ).stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FavoriteUiState.Loading,
        )

    private fun Flow<Result<List<Blog>>>.mapToFavoriteState(): Flow<FavoriteUiState> =
        this.map { result ->
            result.fold(
                onSuccess = { blogs ->
                    if (blogs.isEmpty()) {
                        FavoriteUiState.NoFavoriteBlogs
                    } else {
                        FavoriteUiState.Success(blogs.toImmutableList())
                    }
                },
                onFailure = { FavoriteUiState.Error(it.message ?: "Unknown error") },
            )
        }

    fun refresh() {
        if (uiState.value is FavoriteUiState.Success || uiState.value is FavoriteUiState.NoFavoriteBlogs) {
            screenModelScope.launch {
                refreshFlow.emit(Unit)
            }
        }
    }

    fun retry() {
        screenModelScope.launch {
            if (uiState.value is FavoriteUiState.Error) {
                retryFlow.emit(Unit)
            }
        }
    }
}
