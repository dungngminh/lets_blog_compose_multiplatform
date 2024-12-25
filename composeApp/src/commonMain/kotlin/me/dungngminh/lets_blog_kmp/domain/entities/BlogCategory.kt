package me.dungngminh.lets_blog_kmp.domain.entities

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmStatic

@Serializable
enum class BlogCategory {
    BUSINESS,
    TECHNOLOGY,
    FASHION,
    TRAVEL,
    FOOD,
    EDUCATION,
    ;

    companion object {
        @JvmStatic
        fun fromString(value: String): BlogCategory = valueOf(value.uppercase())
    }
}
