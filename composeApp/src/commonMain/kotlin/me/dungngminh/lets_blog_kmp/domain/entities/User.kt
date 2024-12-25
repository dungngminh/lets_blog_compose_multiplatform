package me.dungngminh.lets_blog_kmp.domain.entities

data class User(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String? = null,
    val follower: Int = 0,
    val following: Int = 0,
)
