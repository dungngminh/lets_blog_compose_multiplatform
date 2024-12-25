package me.dungngminh.lets_blog_kmp.data.repositories

import com.hoc081098.flowext.flowFromSuspend
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import me.dungngminh.lets_blog_kmp.data.api_service.BlogService
import me.dungngminh.lets_blog_kmp.data.mappers.toApiString
import me.dungngminh.lets_blog_kmp.data.mappers.toBlog
import me.dungngminh.lets_blog_kmp.data.mappers.toEditBlogRequest
import me.dungngminh.lets_blog_kmp.data.models.request.blog.CreateBlogRequest
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.entities.BlogCategory
import me.dungngminh.lets_blog_kmp.domain.repositories.BlogRepository

class BlogRepositoryImpl(
    private val blogService: BlogService,
    private val ioDispatcher: CoroutineDispatcher,
) : BlogRepository {
    override fun getBlogs(
        searchQuery: String?,
        limit: Int,
        offset: Int,
        blogCategory: BlogCategory?,
    ): Flow<Result<List<Blog>>> =
        flowFromSuspend {
            runCatching {
                val response =
                    blogService.getBlogs(
                        limit = limit,
                        offset = offset,
                    )
                val blogsResponse = response.unwrap()
                blogsResponse.map { it.toBlog() }
            }
        }.flowOn(ioDispatcher)

    override fun getBlogById(id: String): Flow<Result<Blog>> =
        flowFromSuspend {
            runCatching {
                val response = blogService.getBlogById(id)
                val blogResponse = response.unwrap()
                blogResponse.toBlog()
            }
        }.flowOn(ioDispatcher)

    override suspend fun createBlog(
        title: String,
        imageUrl: String,
        blogCategory: BlogCategory,
        content: String,
    ): Result<Unit> =
        ioDispatcher.runCatching {
            blogService.createBlog(
                CreateBlogRequest(
                    title = title,
                    imageUrl = imageUrl,
                    category = blogCategory.toApiString(),
                    content = content,
                ),
            )
        }

    override suspend fun deleteBlog(id: String): Result<Unit> =
        ioDispatcher.runCatching {
            blogService.deleteBlog(id = id)
        }

    override suspend fun updateBlog(updatedBlog: Blog): Result<Unit> =
        ioDispatcher.runCatching {
            blogService.updateBlog(
                id = updatedBlog.id,
                editBlogRequest = updatedBlog.toEditBlogRequest(),
            )
        }
}
