package me.dungngminh.lets_blog_kmp.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.dungngminh.lets_blog_kmp.data.api_service.FavoriteService
import me.dungngminh.lets_blog_kmp.data.mappers.toBlog
import me.dungngminh.lets_blog_kmp.data.models.request.blog.FavoriteBlogRequest
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.repositories.FavoriteRepository

class FavoriteRepositoryImpl(
    private val favoriteService: FavoriteService,
    private val ioDispatcher: CoroutineDispatcher,
) : FavoriteRepository {
    override suspend fun getFavoriteBlogs(): Result<List<Blog>> =
        withContext(ioDispatcher) {
            runCatching {
                val response = favoriteService.getFavoriteBlogs()
                response
                    .unwrap()
                    .map { it.toBlog() }
            }
        }

    override suspend fun favoriteBlog(blogId: String): Result<Unit> =
        withContext(ioDispatcher) {
            runCatching {
                favoriteService.favoriteBlog(
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
                favoriteService.favoriteBlog(
                    FavoriteBlogRequest(
                        blogId = blogId,
                        isFavorite = false,
                    ),
                )
            }
        }
}
