package me.dungngminh.lets_blog_kmp.data.mappers

import me.dungngminh.lets_blog_kmp.commons.extensions.mapISODateTimeStringToLong
import me.dungngminh.lets_blog_kmp.data.models.request.blog.EditBlogRequest
import me.dungngminh.lets_blog_kmp.data.models.response.blog.GetBlogResponse
import me.dungngminh.lets_blog_kmp.data.models.response.favorites.GetFavoriteResponse
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.entities.BlogCategory

fun GetBlogResponse.toBlog() =
    Blog(
        id = id,
        title = title,
        content = content,
        creator = creator.toUser(),
        imageUrl = imageUrl,
        category = BlogCategory.fromString(category),
        createdAt = createdAt.mapISODateTimeStringToLong(),
        updatedAt = updatedAt.mapISODateTimeStringToLong(),
        isFavoriteByUser = isFavoritedByUser,
    )

fun Blog.toEditBlogRequest() =
    EditBlogRequest(
        title = title,
        content = content,
        category = category.toApiString(),
        imageUrl = imageUrl,
    )

fun GetFavoriteResponse.toBlog() =
    Blog(
        id = id,
        title = title,
        content = content,
        imageUrl = imageUrl,
        category = BlogCategory.fromString(category),
        createdAt = createdAt.mapISODateTimeStringToLong(),
        updatedAt = updatedAt.mapISODateTimeStringToLong(),
        isFavoriteByUser = true,
    )
