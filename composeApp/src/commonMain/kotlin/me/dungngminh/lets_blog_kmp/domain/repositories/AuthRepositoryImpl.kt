package me.dungngminh.lets_blog_kmp.domain.repositories

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authStateFlow: Flow<String?>

    fun login(
        email: String,
        password: String,
    ): Flow<Result<Unit>>

    fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
    ): Flow<Result<Unit>>

    fun checkAuth(): Flow<Unit>
}
