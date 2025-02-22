package me.dungngminh.lets_blog_kmp.data.repositories

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import me.dungngminh.lets_blog_kmp.data.api_service.AuthService
import me.dungngminh.lets_blog_kmp.data.local.UserStore
import me.dungngminh.lets_blog_kmp.data.local.UserStoreData
import me.dungngminh.lets_blog_kmp.data.models.request.auth.LoginRequest
import me.dungngminh.lets_blog_kmp.data.models.request.auth.RegisterRequest
import me.dungngminh.lets_blog_kmp.domain.repositories.AuthRepository

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val httpClient: HttpClient,
    private val userStore: UserStore,
    private val ioDispatcher: CoroutineDispatcher,
) : AuthRepository {
    override val userStoreDataFlow: Flow<UserStoreData?>
        get() =
            userStore.userStoreDataFlow

    override suspend fun login(
        email: String,
        password: String,
    ): Result<Unit> =
        runCatching {
            withContext(ioDispatcher) {
                val response =
                    authService.login(
                        LoginRequest(
                            email = email,
                            password = password,
                        ),
                    )
                clearToken()
                val data = response.unwrap()
                userStore.updateUserStoreData(
                    UserStoreData(
                        token = data.token,
                        userId = data.id,
                    ),
                )
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

    override suspend fun checkAuth(): UserStoreData? =
        withContext(ioDispatcher) {
            userStore.userStoreDataFlow.firstOrNull()
        }

    override suspend fun logout() {
        clearToken()
        userStore.clearUserData()
    }

    private fun clearToken() {
        httpClient.authProvider<BearerAuthProvider>()?.clearToken()
    }
}
