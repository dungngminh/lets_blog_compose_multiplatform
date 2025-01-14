package me.dungngminh.lets_blog_kmp.data.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.dungngminh.lets_blog_kmp.commons.types.AppError

@Serializable
data class BaseResponse<T : Any>(
    val success: Boolean,
    val message: String,
    @SerialName("error_code") val errorCode: String? = null,
    val result: T? = null,
) {
    // Get the result if the response is successful, otherwise throw an exception
    fun unwrap(): T = if (success) result!! else throw AppError.fromErrorCode(errorCode)

    val data: T? = if (success) result else throw AppError.fromErrorCode(errorCode)
}
