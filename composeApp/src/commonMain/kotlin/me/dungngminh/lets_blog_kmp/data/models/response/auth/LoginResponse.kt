package me.dungngminh.lets_blog_kmp.data.models.response.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val id: String,
    val token: String,
)
