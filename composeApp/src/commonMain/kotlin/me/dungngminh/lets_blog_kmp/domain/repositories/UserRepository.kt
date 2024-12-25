package me.dungngminh.lets_blog_kmp.domain.repositories

import kotlinx.coroutines.flow.Flow
import me.dungngminh.lets_blog_kmp.domain.entities.User

interface UserRepository {
    fun getUserProfile(id: String): Flow<Result<User>>

    suspend fun editUserProfile(user: User): Result<Unit>
}
