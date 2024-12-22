package me.dungngminh.lets_blog_kmp.data.models.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
)
