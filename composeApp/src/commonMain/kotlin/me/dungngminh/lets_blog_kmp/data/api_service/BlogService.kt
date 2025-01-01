package me.dungngminh.lets_blog_kmp.data.api_service

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import me.dungngminh.lets_blog_kmp.data.models.request.blog.CreateBlogRequest
import me.dungngminh.lets_blog_kmp.data.models.request.blog.EditBlogRequest
import me.dungngminh.lets_blog_kmp.data.models.request.blog.FavoriteBlogRequest
import me.dungngminh.lets_blog_kmp.data.models.response.BaseResponse
import me.dungngminh.lets_blog_kmp.data.models.response.blog.GetBlogResponse

interface BlogService {
    @GET("api/blogs")
    suspend fun getBlogs(
        @Query("limit") limit: Int,
        @Query("page") offset: Int,
        @Query("search") searchQuery: String?,
    ): BaseResponse<List<GetBlogResponse>>

    @GET("api/top-blogs")
    suspend fun getTopBlogs(
        @Query("limit") limit: Int,
    ): BaseResponse<List<GetBlogResponse>>

    @GET("api/blogs/{id}")
    suspend fun getBlogById(
        @Path("id") id: String,
    ): BaseResponse<GetBlogResponse>

    @POST("api/blogs")
    suspend fun createBlog(
        @Body() createBlogRequest: CreateBlogRequest,
    )

    @PATCH("api/blogs/{id}")
    suspend fun updateBlog(
        @Path("id") id: String,
        @Body() editBlogRequest: EditBlogRequest,
    )

    @DELETE("api/blogs/{id}")
    suspend fun deleteBlog(
        @Path("id") id: String,
    )

    @POST("api/favorites")
    suspend fun favoriteBlog(
        @Body body: FavoriteBlogRequest,
    )
}
