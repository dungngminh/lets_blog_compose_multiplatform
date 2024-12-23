package me.dungngminh.lets_blog_kmp.domain.repositories

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authStateFlow: Flow<String?>

    suspend fun login(
        email: String,
        password: String,
    ): Result<Unit>

    suspend fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
    ): Result<Unit>

    suspend fun checkAuth(): String?
}
