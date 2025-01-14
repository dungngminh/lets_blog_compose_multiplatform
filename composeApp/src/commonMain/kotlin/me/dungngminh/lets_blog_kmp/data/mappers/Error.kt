@file:Suppress("UNCHECKED_CAST")

package me.dungngminh.lets_blog_kmp.data.mappers

import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException
import me.dungngminh.lets_blog_kmp.commons.types.AppError
import me.dungngminh.lets_blog_kmp.data.models.response.BaseResponse
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmName

object ErrorCodes {
    // Base
    const val SERVER_ERROR = "server-error"
    const val BODY_EMPTY = "body-empty"
    const val BODY_INVALID = "body-invalid"

    // Login
    const val INVALID_EMAIL_OR_PASSWORD = "invalid-email-or-password"

    // Register
    const val EMAIL_REGISTERED = "email-registerd"

    // Blog
    const val BLOG_NOT_FOUND = "blog-not-found"

    // Upload
    const val NO_IMAGE_UPLOAD = "no-image-upload"
    const val UPLOAD_FAILED = "upload-failed"

    // Favorite
    const val CANNOT_ACTION_OWN_BLOG = "cannot-action-own-blog"
    const val ALREADY_FAVORITED = "already-favorited"

    // Blog by user id
    const val YOU_ARE_NOT_CREATOR = "you-are-not-creator"

    // User
    const val USER_NOT_FOUND = "user-not-found"
}

suspend fun Throwable.toAppError(): AppError =
    when (this) {
        is AppError -> this
        is ClientRequestException -> {
            runCatching {
                val response = this.response.body<BaseResponse<Any>>()
                val errorCode = response.message
                AppError.fromErrorCode(errorCode)
            }.getOrElse { AppError.Unknown(cause = this) }
        }

        is ServerResponseException -> {
            runCatching {
                val response = this.response.body<BaseResponse<Any>>()
                val errorCode = response.message
                AppError.fromErrorCode(errorCode)
            }.getOrElse { AppError.Remote.ServerError(this.message, this) }
        }

        is IOException -> AppError.Remote.NetworkError(cause = this)

        is CancellationException -> throw this

        else -> AppError.Unknown(cause = this)
    }

data class AppResult<T>(
    val data: Any?,
    val error: AppError? = null,
) {
    inline fun getOrNull(): T? =
        when {
            isSuccess -> data as T
            else -> null
        }

    inline fun getOrElse(block: (AppError) -> T): T =
        when {
            isSuccess -> data as T
            else -> block(error!!)
        }

    val isSuccess: Boolean
        get() = error == null

    val isFailure: Boolean
        get() = error != null

    companion object {
        @JvmName("success")
        inline fun <T> success(value: T): AppResult<T> = AppResult(value)

        @JvmName("failure")
        inline fun <T> failure(error: AppError): AppResult<T> = AppResult(null, error = error)
    }
}

suspend inline fun <T, R> T.runAppCatchingSuspend(block: T.() -> R): AppResult<R> =
    try {
        AppResult.success(block())
    } catch (e: Throwable) {
        AppResult.failure(e.toAppError())
    }

@OptIn(ExperimentalContracts::class)
inline fun <R, T> AppResult<T>.fold(
    onSuccess: (value: T) -> R,
    onFailure: (exception: AppError) -> R,
): R {
    contract {
        callsInPlace(onSuccess, InvocationKind.AT_MOST_ONCE)
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }
    Result
    return when (error) {
        null -> onSuccess(data as T)
        else -> onFailure(error)
    }
}
