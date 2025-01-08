package me.dungngminh.lets_blog_kmp.presentation.main.profile

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hoc081098.flowext.flowFromSuspend
import com.hoc081098.flowext.startWith
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.repositories.AuthRepository
import me.dungngminh.lets_blog_kmp.domain.repositories.UserRepository

class ProfileViewModel(
    authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ScreenModel {
    private val refreshFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    // This flow will use for initial fetch and retry
    private val retryFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val userBlogFlow =
        authRepository.userStoreDataFlow
            .flatMapLatest { storeData ->
                if (storeData == null) {
                    flowOf(UserBlogState.Uninitialized)
                } else {
                    flowFromSuspend {
                        userRepository
                            .getUserBlogs(storeData.userId)
                    }.map { result ->
                        result.fold(
                            onSuccess = { blogs ->
                                if (blogs.isEmpty()) {
                                    UserBlogState.EmptyBlog
                                } else {
                                    UserBlogState.Success(blogs)
                                }
                            },
                            onFailure = { error ->
                                UserBlogState.Error(error.message ?: "Unknown error")
                            },
                        )
                    }
                }
            }

    @OptIn(ExperimentalCoroutinesApi::class)
    val userBlogState =
        merge(
            refreshFlow
                .flatMapLatest { userBlogFlow },
            retryFlow
                .startWith(Unit)
                .flatMapLatest {
                    userBlogFlow
                        .startWith(UserBlogState.Loading)
                },
        ).stateIn(
            scope = screenModelScope,
            started = SharingStarted.Lazily,
            initialValue = UserBlogState.Uninitialized,
        )

    fun refresh() {
        screenModelScope.launch {
            if (userBlogState.value is UserBlogState.Success) {
                refreshFlow.emit(Unit)
            }
        }
    }

    fun retry() {
        screenModelScope.launch {
            if (userBlogState.value is UserBlogState.Error) {
                retryFlow.emit(Unit)
            }
        }
    }
}

sealed class UserBlogState {
    data object Uninitialized : UserBlogState()

    data object Loading : UserBlogState()

    data class Success(
        val blogs: List<Blog>,
    ) : UserBlogState()

    data object EmptyBlog : UserBlogState()

    data class Error(
        val message: String,
    ) : UserBlogState()
}
