package me.dungngminh.lets_blog_kmp.data.models.request.blog

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteBlogRequest(
    @SerialName("blog_id") val blogId: String,
    @SerialName("is_favorite") val isFavorite: Boolean,
)
