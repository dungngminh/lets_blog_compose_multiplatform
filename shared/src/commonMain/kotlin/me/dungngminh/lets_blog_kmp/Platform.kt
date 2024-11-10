package me.dungngminh.lets_blog_kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
