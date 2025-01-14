package me.dungngminh.lets_blog_kmp.domain.repositories

import kotlinx.coroutines.flow.Flow
import me.dungngminh.lets_blog_kmp.data.local.UserStoreData
import me.dungngminh.lets_blog_kmp.data.mappers.AppResult

interface AuthRepository {
    val userStoreDataFlow: Flow<UserStoreData?>

    suspend fun login(
        email: String,
        password: String,
    ): AppResult<Unit>

    suspend fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
    ): AppResult<Unit>

    suspend fun logout()

    suspend fun checkAuth(): UserStoreData?
}
