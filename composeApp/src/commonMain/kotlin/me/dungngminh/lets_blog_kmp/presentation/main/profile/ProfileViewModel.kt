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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.repositories.AuthRepository
import me.dungngminh.lets_blog_kmp.domain.repositories.UserRepository

class ProfileViewModel(
    authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ScreenModel {
    val refreshFlow = MutableSharedFlow<Unit>()

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
                                UserBlogState.Success(blogs)
                            },
                            onFailure = { error ->
                                UserBlogState.Error(error.message ?: "Unknown error")
                            },
                        )
                    }.startWith(UserBlogState.Loading)
                }
            }

    @OptIn(ExperimentalCoroutinesApi::class)
    val userBlogState =
        refreshFlow
            .startWith(Unit)
            .flatMapLatest { userBlogFlow }
            .stateIn(
                scope = screenModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = UserBlogState.Uninitialized,
            )

    fun refresh() {
        screenModelScope.launch {
            refreshFlow.emit(Unit)
        }
    }
}

sealed class UserBlogState {
    data object Uninitialized : UserBlogState()

    data object Loading : UserBlogState()

    data class Success(
        val blogs: List<Blog>,
    ) : UserBlogState()

    data class Error(
        val message: String,
    ) : UserBlogState()
}
