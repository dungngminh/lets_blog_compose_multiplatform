package me.dungngminh.lets_blog_kmp.data.models.response.favorites

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetFavoriteResponse(
    val id: String,
    val title: String,
    val content: String,
    @SerialName("image_url") val imageUrl: String,
    val category: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
)
