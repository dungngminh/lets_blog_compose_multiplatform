package me.dungngminh.lets_blog_kmp.presentation.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.ic_favorite
import letsblogkmp.composeapp.generated.resources.ic_favorite_filled
import me.dungngminh.lets_blog_kmp.commons.extensions.timeAgo
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.entities.BlogCategory
import me.dungngminh.lets_blog_kmp.domain.entities.User
import org.jetbrains.compose.resources.painterResource

@Composable
fun PopularBlogCard(
    modifier: Modifier = Modifier,
    blog: Blog,
    onClick: (Blog) -> Unit,
    onFavoriteClick: (Blog) -> Unit,
    onUnFavoriteBlogClick: (Blog) -> Unit,
) {
    Box(
        modifier =
            modifier
                .size(260.dp)
                .clip(RoundedCornerShape(24.dp))
                .clickable { onClick(blog) },
    ) {
        CoilImage(
            imageModel = { blog.imageUrl },
            modifier =
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp)),
            imageOptions = ImageOptions(contentScale = ContentScale.Crop),
        )
        PopularBlogCardContent(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(12.dp),
            blog = blog,
            onFavoriteClick = onFavoriteClick,
            onUnFavoriteBlogClick = onUnFavoriteBlogClick,
        )
    }
}

@Composable
private fun PopularBlogCardContent(
    modifier: Modifier = Modifier,
    blog: Blog,
    onFavoriteClick: (Blog) -> Unit,
    onUnFavoriteBlogClick: (Blog) -> Unit,
) {
    Column(modifier = modifier) {
        // Favorite Button
        if (blog.isFavoriteByUser != null) {
            IconButton(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    if (blog.isFavoriteByUser) {
                        onUnFavoriteBlogClick(blog)
                    } else {
                        onFavoriteClick(blog)
                    }
                },
            ) {
                Icon(
                    if (blog.isFavoriteByUser) {
                        painterResource(Res.drawable.ic_favorite_filled)
                    } else {
                        painterResource(Res.drawable.ic_favorite)
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier =
                Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row {
                    CoilImage(
                        imageModel = {},
                        modifier = Modifier.size(36.dp),
                        imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        Text(
                            blog.creator.name,
                            style =
                                MaterialTheme
                                    .typography.titleSmall
                                    .copy(fontWeight = FontWeight.SemiBold),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            blog.createdAt.timeAgo(),
                            style =
                                MaterialTheme
                                    .typography.bodySmall,
                        )
                    }
                }
                Text(
                    blog.title,
                    style =
                        MaterialTheme
                            .typography.titleMedium
                            .copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewPopularBlogCard() {
    MaterialTheme {
        PopularBlogCard(
            modifier = Modifier,
            blog =
                Blog(
                    id = "pertinacia",
                    title = "scripta",
                    content = "quidam",
                    imageUrl = "https://duckduckgo.com/?q=cum",
                    category = BlogCategory.TRAVEL,
                    createdAt = 5029,
                    updatedAt = 2657,
                    creator =
                        User(
                            id = "adolescens",
                            name = "Erica Puckett",
                            email = "millicent.larsen@example.com",
                            avatarUrl = null,
                            follower = 7878,
                            following = 8387,
                        ),
                ),
            onClick = {},
            onFavoriteClick = {},
            onUnFavoriteBlogClick = {},
        )
    }
}
