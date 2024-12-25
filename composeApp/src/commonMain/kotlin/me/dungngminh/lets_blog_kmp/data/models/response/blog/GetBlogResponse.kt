package me.dungngminh.lets_blog_kmp.data.models.response.blog

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.dungngminh.lets_blog_kmp.data.models.response.user.GetUserResponse

@Serializable
data class GetBlogResponse(
    val id: String,
    val title: String,
    val content: String,
    @SerialName("image_url") val imageUrl: String,
    val category: String,
    @SerialName("is_favorited_by_user") val isFavoritedByUser: Boolean? = null,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    val creator: GetUserResponse,
)
