package me.dungngminh.lets_blog_kmp.data.models.request.blog

data class FavoriteBlogRequest(
    val blogId: String,
    val isFavorite: Boolean,
)
