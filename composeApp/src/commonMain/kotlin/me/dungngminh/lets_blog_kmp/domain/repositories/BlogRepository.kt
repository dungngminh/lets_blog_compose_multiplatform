package me.dungngminh.lets_blog_kmp.domain.repositories

import kotlinx.coroutines.flow.Flow
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.entities.BlogCategory

interface BlogRepository {
    fun getBlogs(
        searchQuery: String?,
        limit: Int,
        offset: Int,
        blogCategory: BlogCategory?,
    ): Flow<Result<List<Blog>>>

    fun getBlogById(id: String): Flow<Result<Blog>>

    suspend fun createBlog(
        title: String,
        imageUrl: String,
        blogCategory: BlogCategory,
        content: String,
    ): Result<Unit>

    suspend fun deleteBlog(id: String): Result<Unit>

    suspend fun updateBlog(updatedBlog: Blog): Result<Unit>
}
