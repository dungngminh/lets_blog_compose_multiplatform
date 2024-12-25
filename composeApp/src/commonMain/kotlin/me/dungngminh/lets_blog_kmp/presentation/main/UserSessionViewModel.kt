package me.dungngminh.lets_blog_kmp.presentation.main

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hoc081098.flowext.flatMapFirst
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.dungngminh.lets_blog_kmp.domain.entities.User
import me.dungngminh.lets_blog_kmp.domain.repositories.AuthRepository
import me.dungngminh.lets_blog_kmp.domain.repositories.UserRepository

class UserSessionViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ScreenModel {
    val userSessionState =
        authRepository.userStoreDataFlow
            .flatMapFirst { storeData ->
                if (storeData == null) {
                    return@flatMapFirst flowOf(UserSessionState.Unauthenticated)
                }
                userRepository
                    .getUserProfile(storeData.userId)
                    .map { userResult ->
                        userResult.fold(
                            onSuccess = { user ->
                                UserSessionState.Authenticated(user)
                            },
                            onFailure = {
                                UserSessionState.Error(it.message ?: "Unknown error")
                            },
                        )
                    }
            }.stateIn(
                scope = screenModelScope,
                started = SharingStarted.Lazily,
                initialValue = UserSessionState.Initial,
            )

    fun logout() {
        screenModelScope.launch {
            authRepository.logout()
        }
    }
}

sealed class UserSessionState {
    data object Initial : UserSessionState()

    data object Unauthenticated : UserSessionState()

    data class Authenticated(
        val user: User,
    ) : UserSessionState()

    data class Error(
        val message: String,
    ) : UserSessionState()
}
