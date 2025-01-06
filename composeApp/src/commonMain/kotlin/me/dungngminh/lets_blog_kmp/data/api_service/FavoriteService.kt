package me.dungngminh.lets_blog_kmp.data.api_service

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import me.dungngminh.lets_blog_kmp.data.models.request.blog.FavoriteBlogRequest
import me.dungngminh.lets_blog_kmp.data.models.response.BaseResponse
import me.dungngminh.lets_blog_kmp.data.models.response.favorites.GetFavoriteResponse

interface FavoriteService {
    @GET("api/favorites")
    suspend fun getFavoriteBlogs(): BaseResponse<List<GetFavoriteResponse>>

    @POST("api/favorites")
    suspend fun favoriteBlog(
        @Body body: FavoriteBlogRequest,
    )
}
