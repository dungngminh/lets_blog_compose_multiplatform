package me.dungngminh.lets_blog_kmp.data.models.request.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditUserRequest(
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("full_name") val fullName: String? = null,
)
