package me.dungngminh.lets_blog_kmp.presentation.main

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.map
import me.dungngminh.lets_blog_kmp.domain.repositories.AuthRepository

class UserSessionViewModel(
    private val authRepository: AuthRepository,
) : ScreenModel {
    val authState =
        authRepository.userStoreDataFlow
            .map {
                if (it == null) {
                    AuthState.Unauthenticated
                } else {
                    AuthState.Authenticated(it.userId)
                }
            }

    fun logout() {
    }
}

sealed class AuthState {
    data object Initial : AuthState()

    data object Unauthenticated : AuthState()

    data class Authenticated(
        val userId: String,
    ) : AuthState()
}
