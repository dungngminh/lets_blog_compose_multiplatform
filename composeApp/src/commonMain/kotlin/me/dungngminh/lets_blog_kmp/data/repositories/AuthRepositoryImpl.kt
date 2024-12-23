package me.dungngminh.lets_blog_kmp.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import me.dungngminh.lets_blog_kmp.data.api_service.AuthService
import me.dungngminh.lets_blog_kmp.data.local.UserStore
import me.dungngminh.lets_blog_kmp.data.models.request.LoginRequest
import me.dungngminh.lets_blog_kmp.data.models.request.RegisterRequest
import me.dungngminh.lets_blog_kmp.domain.repositories.AuthRepository

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val userStore: UserStore,
    private val ioDispatcher: CoroutineDispatcher,
) : AuthRepository {
    override val authStateFlow: Flow<String?>
        get() =
            userStore.tokenFlow

    override suspend fun login(
        email: String,
        password: String,
    ): Result<Unit> =
        runCatching {
            withContext(ioDispatcher) {
                val loginResponse =
                    authService.login(
                        LoginRequest(
                            email = email,
                            password = password,
                        ),
                    )
                userStore.saveToken(loginResponse.unwrap().token)
            }
        }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
    ) = runCatching {
        withContext(ioDispatcher) {
            val registerRequest =
                RegisterRequest(
                    email = email,
                    password = password,
                    confirmationPassword = confirmPassword,
                    fullName = name,
                )
            authService.register(registerRequest)
        }
    }

    override suspend fun checkAuth(): String? =
        withContext(ioDispatcher) {
            userStore.tokenFlow.firstOrNull()
        }
}
