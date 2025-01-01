package me.dungngminh.lets_blog_kmp.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.dungngminh.lets_blog_kmp.data.api_service.BlogService
import me.dungngminh.lets_blog_kmp.data.mappers.toApiString
import me.dungngminh.lets_blog_kmp.data.mappers.toBlog
import me.dungngminh.lets_blog_kmp.data.mappers.toEditBlogRequest
import me.dungngminh.lets_blog_kmp.data.models.request.blog.CreateBlogRequest
import me.dungngminh.lets_blog_kmp.data.models.request.blog.FavoriteBlogRequest
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.entities.BlogCategory
import me.dungngminh.lets_blog_kmp.domain.repositories.BlogRepository

class BlogRepositoryImpl(
    private val blogService: BlogService,
    private val ioDispatcher: CoroutineDispatcher,
) : BlogRepository {
    override suspend fun getBlogs(
        searchQuery: String?,
        limit: Int,
        offset: Int,
        blogCategory: BlogCategory?,
    ): Result<List<Blog>> =
        withContext(ioDispatcher) {
            runCatching {
                val response =
                    blogService.getBlogs(
                        limit = limit,
                        offset = offset,
                        searchQuery = searchQuery,
                    )
                response
                    .unwrap()
                    .map { it.toBlog() }
            }
        }

    override suspend fun getTopBlogs(limit: Int): Result<List<Blog>> =
        withContext(ioDispatcher) {
            runCatching {
                val response = blogService.getTopBlogs(limit = limit)
                response
                    .unwrap()
                    .map { it.toBlog() }
            }
        }

    override suspend fun getBlogById(id: String): Result<Blog> =
        withContext(ioDispatcher) {
            runCatching {
                val response = blogService.getBlogById(id = id)
                response
                    .unwrap()
                    .toBlog()
            }
        }

    override suspend fun createBlog(
        title: String,
        imageUrl: String,
        blogCategory: BlogCategory,
        content: String,
    ): Result<Unit> =
        withContext(ioDispatcher) {
            runCatching {
                blogService.createBlog(
                    CreateBlogRequest(
                        title = title,
                        imageUrl = imageUrl,
                        category = blogCategory.toApiString(),
                        content = content,
                    ),
                )
            }
        }

    override suspend fun deleteBlog(id: String): Result<Unit> =
        withContext(ioDispatcher) {
            runCatching {
                blogService.deleteBlog(id = id)
            }
        }

    override suspend fun updateBlog(updatedBlog: Blog): Result<Unit> =
        withContext(ioDispatcher) {
            runCatching {
                blogService.updateBlog(
                    id = updatedBlog.id,
                    editBlogRequest = updatedBlog.toEditBlogRequest(),
                )
            }
        }

    override suspend fun favoriteBlog(blogId: String): Result<Unit> =
        withContext(ioDispatcher) {
            runCatching {
                blogService.favoriteBlog(
                    FavoriteBlogRequest(
                        blogId = blogId,
                        isFavorite = true,
                    ),
                )
            }
        }

    override suspend fun unFavoriteBlog(blogId: String): Result<Unit> =
        withContext(ioDispatcher) {
            runCatching {
                blogService.favoriteBlog(
                    FavoriteBlogRequest(
                        blogId = blogId,
                        isFavorite = false,
                    ),
                )
            }
        }
}
