package me.dungngminh.lets_blog_kmp.data.repositories

import com.hoc081098.flowext.FlowExtPreview
import com.hoc081098.flowext.mapToResult
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import me.dungngminh.lets_blog_kmp.data.api_service.AuthService
import me.dungngminh.lets_blog_kmp.data.local.UserStore
import me.dungngminh.lets_blog_kmp.data.models.request.LoginRequest
import me.dungngminh.lets_blog_kmp.domain.repositories.AuthRepository

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val userStore: UserStore,
    private val ioDispatcher: CoroutineDispatcher,
) : AuthRepository {
    override val authStateFlow: Flow<String?>
        get() =
            userStore.tokenFlow

    @OptIn(FlowExtPreview::class)
    override fun login(
        email: String,
        password: String,
    ): Flow<Result<Unit>> =
        authService
            .login(
                LoginRequest(
                    email = email,
                    password = password,
                ),
            ).map {
                Napier.i("login: $it")
                userStore.saveToken(it.result.token)
            }.mapToResult()
            .flowOn(ioDispatcher)

    override fun register(
        name: String,
        email: String,
        password: String,
    ): Flow<Result<Unit>> {
        TODO("Not yet implemented")
    }

    override fun checkAuth(): Flow<Unit> {
        TODO("Not yet implemented")
    }
}
