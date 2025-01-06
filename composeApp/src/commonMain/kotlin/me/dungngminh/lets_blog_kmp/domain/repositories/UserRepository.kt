package me.dungngminh.lets_blog_kmp.domain.repositories

import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.entities.User

interface UserRepository {
    suspend fun getUserProfile(id: String): Result<User>

    suspend fun editUserProfile(user: User): Result<Unit>

    suspend fun getUserBlogs(id: String): Result<List<Blog>>

    suspend fun getUserFavoriteBlogs(id: String): Result<List<Blog>>
}
