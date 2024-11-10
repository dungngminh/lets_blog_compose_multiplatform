package me.dungngminh.lets_blog_kmp.data.repositories

import kotlinx.coroutines.flow.Flow
import me.dungngminh.lets_blog_kmp.domain.repositories.AuthRepository

class AuthRepositoryImpl : AuthRepository {
    override val authStateFlow: Flow<Unit>
        get() = TODO("Not yet implemented")

    override fun login(
        email: String,
        password: String,
    ): Flow<Result<Unit>> {
        TODO("Not yet implemented")
    }

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
