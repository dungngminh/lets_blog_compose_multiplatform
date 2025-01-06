package me.dungngminh.lets_blog_kmp.presentation.main.favorite.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.entities.User
import me.dungngminh.lets_blog_kmp.presentation.components.BlogCard
import me.dungngminh.lets_blog_kmp.presentation.components.Center
import me.dungngminh.lets_blog_kmp.presentation.main.favorite.FavoriteUiState

@Composable
fun AuthenticatedFavoriteContent(
    modifier: Modifier = Modifier,
    favoriteUiState: FavoriteUiState,
    user: User?,
    onBlogClick: (Blog) -> Unit,
) {
    when (favoriteUiState) {
        FavoriteUiState.Loading -> {
            Center {
                CircularProgressIndicator()
            }
        }

        is FavoriteUiState.Error -> {
        }

        FavoriteUiState.NoFavoriteBlogs -> {
        }

        is FavoriteUiState.Success ->
            FavoriteBlogList(
                modifier = modifier,
                favoriteBlogs = favoriteUiState.blogs.toImmutableList(),
                onBlogClick = onBlogClick,
                user = user,
            )
    }
}

@Composable
private fun FavoriteBlogList(
    modifier: Modifier = Modifier,
    favoriteBlogs: ImmutableList<Blog>,
    user: User?,
    onBlogClick: (Blog) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    ) {
        itemsIndexed(
            favoriteBlogs,
            { _, blog -> blog.id },
        ) { index, blog ->
            val blogWithUser = blog.copy(creator = user)
            BlogCard(
                modifier = Modifier.fillMaxWidth(),
                blog = blogWithUser,
                onClick = {
                    onBlogClick(blogWithUser)
                },
            )
            if (index < favoriteBlogs.size - 1) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
