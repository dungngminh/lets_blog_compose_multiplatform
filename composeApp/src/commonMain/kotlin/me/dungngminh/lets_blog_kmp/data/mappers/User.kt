package me.dungngminh.lets_blog_kmp.data.mappers

import me.dungngminh.lets_blog_kmp.data.models.request.user.EditUserRequest
import me.dungngminh.lets_blog_kmp.data.models.response.user.GetUserResponse
import me.dungngminh.lets_blog_kmp.domain.entities.User

fun GetUserResponse.toUser() =
    User(
        id = id,
        email = email,
        name = fullName,
        avatarUrl = avatarUrl,
        follower = follower,
        following = following,
        blogCount = blogCount,
    )

fun User.toEditUserRequest() =
    EditUserRequest(
        avatarUrl = avatarUrl,
        fullName = name,
    )
