package me.dungngminh.lets_blog_kmp.presentation.main

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hoc081098.flowext.flatMapFirst
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
import me.dungngminh.lets_blog_kmp.domain.entities.User
import me.dungngminh.lets_blog_kmp.domain.repositories.AuthRepository
import me.dungngminh.lets_blog_kmp.domain.repositories.UserRepository

@OptIn(ExperimentalCoroutinesApi::class)
class UserSessionViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ScreenModel {
    private val refreshFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    // This flow will use for initial fetch and retry
    private val retryFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    private val userSessionFlow =
        authRepository.userStoreDataFlow
            .flatMapFirst { storeData ->
                if (storeData == null) {
                    return@flatMapFirst flowOf(UserSessionState.Unauthenticated)
                }
                flowFromSuspend {
                    userRepository.getUserProfile(storeData.userId)
                }.map { userResult ->
                    userResult.fold(
                        onSuccess = { user ->
                            UserSessionState.Authenticated(user)
                        },
                        onFailure = {
                            UserSessionState.AuthenticatedFetchDataError(
                                it.message ?: "Unknown error",
                            )
                        },
                    )
                }.startWith(UserSessionState.Loading)
            }

    val userSessionState =
        merge(
            refreshFlow
                .flatMapLatest { userSessionFlow },
            retryFlow
                .startWith(Unit)
                .flatMapLatest {
                    userSessionFlow
                },
        ).stateIn(
            scope = screenModelScope,
            started = SharingStarted.Eagerly,
            initialValue = UserSessionState.Initial,
        )

    fun refresh() {
        screenModelScope.launch {
            if (userSessionState.value is UserSessionState.Authenticated) {
                refreshFlow.emit(Unit)
            }
        }
    }

    fun retry() {
        screenModelScope.launch {
            if (userSessionState.value is UserSessionState.AuthenticatedFetchDataError) {
                retryFlow.emit(Unit)
            }
        }
    }

    fun logout() {
        screenModelScope.launch {
            authRepository.logout()
        }
    }
}

sealed class UserSessionState {
    data object Initial : UserSessionState()

    data object Unauthenticated : UserSessionState()

    data object Loading : UserSessionState()

    data class Authenticated(
        val user: User,
    ) : UserSessionState()

    data class AuthenticatedFetchDataError(
        val message: String,
    ) : UserSessionState()

    val isAuthenticated
        get() =
            when (this) {
                is Authenticated, is AuthenticatedFetchDataError -> true
                else -> false
            }
}
