package me.dungngminh.lets_blog_kmp.domain.repositories

import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.entities.BlogCategory

interface BlogRepository {
    suspend fun getBlogs(
        searchQuery: String? = null,
        limit: Int,
        offset: Int,
        blogCategory: BlogCategory? = null,
    ): Result<List<Blog>>

    suspend fun getBlogById(id: String): Result<Blog>

    suspend fun createBlog(
        title: String,
        imageUrl: String,
        blogCategory: BlogCategory,
        content: String,
    ): Result<Unit>

    suspend fun deleteBlog(id: String): Result<Unit>

    suspend fun updateBlog(updatedBlog: Blog): Result<Unit>
}
