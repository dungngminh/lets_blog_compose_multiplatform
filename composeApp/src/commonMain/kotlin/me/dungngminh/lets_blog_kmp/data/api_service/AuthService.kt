package me.dungngminh.lets_blog_kmp.data.api_service

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import kotlinx.coroutines.flow.Flow
import me.dungngminh.lets_blog_kmp.data.models.request.LoginRequest
import me.dungngminh.lets_blog_kmp.data.models.request.RegisterRequest
import me.dungngminh.lets_blog_kmp.data.models.response.BaseResponse
import me.dungngminh.lets_blog_kmp.data.models.response.LoginResponse

interface AuthService {
    @POST("api/auth/login")
    fun login(
        @Body loginRequest: LoginRequest,
    ): Flow<BaseResponse<LoginResponse>>

    @POST("api/auth/register")
    fun register(
        @Body registerRequest: RegisterRequest,
    ): Flow<Unit>
}
