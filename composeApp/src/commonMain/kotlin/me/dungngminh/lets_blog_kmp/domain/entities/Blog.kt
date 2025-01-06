package me.dungngminh.lets_blog_kmp.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Blog(
    val id: String,
    val title: String,
    val content: String,
    val imageUrl: String,
    val category: BlogCategory,
    val createdAt: Long,
    val updatedAt: Long,
    val creator: User? = null,
    val isFavoriteByUser: Boolean? = null,
)
