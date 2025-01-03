package me.dungngminh.lets_blog_kmp.data.api_service

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.Path
import me.dungngminh.lets_blog_kmp.data.models.request.user.EditUserRequest
import me.dungngminh.lets_blog_kmp.data.models.response.BaseResponse
import me.dungngminh.lets_blog_kmp.data.models.response.blog.GetBlogResponse
import me.dungngminh.lets_blog_kmp.data.models.response.user.GetUserResponse

interface UserService {
    @GET("api/users/{id}/profiles")
    suspend fun getUserProfile(
        @Path("id") id: String,
    ): BaseResponse<GetUserResponse>

    @PATCH("api/users/{id}/profiles")
    suspend fun editUserProfile(
        @Path("id") id: String,
        @Body() editUserRequest: EditUserRequest,
    )

    @GET("api/users/{id}/blogs")
    suspend fun getUserBlogs(
        @Path("id") id: String,
    ): BaseResponse<List<GetBlogResponse>>
}
