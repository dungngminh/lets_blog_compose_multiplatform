package me.dungngminh.lets_blog_kmp.data.models.response

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val success: Boolean,
    val message: String,
    val result: T? = null,
) {
    fun unwrap(): T = result!!
}
