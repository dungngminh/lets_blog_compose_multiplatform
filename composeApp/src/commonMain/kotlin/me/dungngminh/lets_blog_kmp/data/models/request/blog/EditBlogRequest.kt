package me.dungngminh.lets_blog_kmp.data.models.request.blog

import kotlinx.serialization.Serializable

@Serializable
data class EditBlogRequest(
    val title: String,
    val content: String,
    val category: String,
    val imageUrl: String,
)
