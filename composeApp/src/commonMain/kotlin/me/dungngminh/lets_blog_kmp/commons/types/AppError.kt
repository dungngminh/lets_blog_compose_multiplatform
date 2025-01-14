package me.dungngminh.lets_blog_kmp.commons.types

import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.general_error_blog_not_found
import letsblogkmp.composeapp.generated.resources.general_error_email_already_exists
import letsblogkmp.composeapp.generated.resources.general_error_invalid_email_or_password
import letsblogkmp.composeapp.generated.resources.general_error_please_check_your_network
import letsblogkmp.composeapp.generated.resources.general_error_you_can_not_action_your_own_blog
import letsblogkmp.composeapp.generated.resources.general_error_you_have_already_favorited_this_blog
import letsblogkmp.composeapp.generated.resources.general_something_went_wrong_please_try_again
import me.dungngminh.lets_blog_kmp.data.mappers.ErrorCodes
import org.jetbrains.compose.resources.StringResource

sealed class AppError(
    cause: Throwable?,
) : Throwable(cause) {
    sealed class Remote(
        cause: Throwable?,
    ) : AppError(cause) {
        data class NetworkError(
            override val cause: Throwable? = null,
        ) : Remote(cause)

        // server-error
        data class ServerError(
            val errorMessage: String,
            override val cause: Throwable? = null,
        ) : Remote(cause)
    }

    sealed class Auth(
        cause: Throwable?,
    ) : AppError(cause) {
        // invalid-email-or-password
        data class InvalidCredentials(
            override val cause: Throwable? = null,
        ) : Auth(cause)

        // email-registerd
        data class EmailAlreadyInUse(
            override val cause: Throwable? = null,
        ) : Auth(cause)
    }

    sealed class Blog(
        cause: Throwable?,
    ) : AppError(cause) {
        // blog-not-found
        data class BlogNotFound(
            override val cause: Throwable? = null,
        ) : Blog(cause)

        // you-are-not-creator
        data class YouAreNotTheCreator(
            override val cause: Throwable? = null,
        ) : Blog(cause)
    }

    // Favorite
    sealed class Favorite(
        cause: Throwable?,
    ) : AppError(cause) {
        // cannot-action-own-blog
        data class CannotActionYourOwnBlog(
            override val cause: Throwable? = null,
        ) : Favorite(cause)

        // already-favorited
        data class BlogAlreadyFavorite(
            override val cause: Throwable? = null,
        ) : Favorite(cause)
    }

    // Upload
    sealed class Upload(
        cause: Throwable?,
    ) : AppError(cause) {
        // upload-failed
        data class UploadFailed(
            override val cause: Throwable? = null,
        ) : Upload(cause)

        // no-image-upload
        data class NoImageUpload(
            override val cause: Throwable? = null,
        ) : Upload(cause)
    }

    sealed class User(
        cause: Throwable?,
    ) : AppError(cause) {
        // user-not-found
        data class UserNotFound(
            override val cause: Throwable? = null,
        ) : User(cause)
    }

    // edge case
    data class Unknown(
        val errorCode: String? = null,
        override val cause: Throwable? = null,
    ) : AppError(cause)

    companion object {
        fun fromErrorCode(errorCode: String?): AppError =
            when (errorCode) {
                ErrorCodes.SERVER_ERROR -> Remote.ServerError(errorCode)
                ErrorCodes.INVALID_EMAIL_OR_PASSWORD -> Auth.InvalidCredentials()
                ErrorCodes.EMAIL_REGISTERED -> Auth.EmailAlreadyInUse()
                ErrorCodes.BLOG_NOT_FOUND -> Blog.BlogNotFound()
                ErrorCodes.CANNOT_ACTION_OWN_BLOG -> Favorite.CannotActionYourOwnBlog()
                ErrorCodes.ALREADY_FAVORITED -> Favorite.BlogAlreadyFavorite()
                ErrorCodes.YOU_ARE_NOT_CREATOR -> Blog.YouAreNotTheCreator()
                ErrorCodes.USER_NOT_FOUND -> User.UserNotFound()
                ErrorCodes.NO_IMAGE_UPLOAD -> Upload.NoImageUpload()
                ErrorCodes.UPLOAD_FAILED -> Upload.UploadFailed()
                else -> Unknown(errorCode)
            }
    }
}

val AppError.localizedMessageRes: StringResource
    get() =
        when (this) {
            is AppError.Auth.EmailAlreadyInUse -> Res.string.general_error_email_already_exists
            is AppError.Auth.InvalidCredentials -> Res.string.general_error_invalid_email_or_password
            is AppError.Blog.BlogNotFound -> Res.string.general_error_blog_not_found
            is AppError.Favorite.BlogAlreadyFavorite -> Res.string.general_error_you_have_already_favorited_this_blog
            is AppError.Favorite.CannotActionYourOwnBlog -> Res.string.general_error_you_can_not_action_your_own_blog
            is AppError.Remote.NetworkError -> Res.string.general_error_please_check_your_network
            else -> Res.string.general_something_went_wrong_please_try_again
        }
