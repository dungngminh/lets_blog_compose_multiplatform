package me.dungngminh.lets_blog_kmp.data.models.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserResponse(
    val id: String,
    @SerialName("full_name") val fullName: String,
    val email: String,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    val follower: Int = 0,
    val following: Int = 0,
)
