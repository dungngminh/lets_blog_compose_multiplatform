package me.dungngminh.lets_blog_kmp.domain.repositories

import me.dungngminh.lets_blog_kmp.domain.entities.Blog

interface FavoriteRepository {
    suspend fun getFavoriteBlogs(): Result<List<Blog>>

    suspend fun favoriteBlog(blogId: String): Result<Unit>

    suspend fun unFavoriteBlog(blogId: String): Result<Unit>
}
