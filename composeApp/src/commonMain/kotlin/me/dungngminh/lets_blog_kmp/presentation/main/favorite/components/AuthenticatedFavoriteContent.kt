package me.dungngminh.lets_blog_kmp.presentation.main.favorite.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.presentation.components.BlogCard
import me.dungngminh.lets_blog_kmp.presentation.components.Center
import me.dungngminh.lets_blog_kmp.presentation.components.ErrorView
import me.dungngminh.lets_blog_kmp.presentation.components.ErrorViewType
import me.dungngminh.lets_blog_kmp.presentation.main.favorite.FavoriteUiState

@Composable
fun AuthenticatedFavoriteContent(
    modifier: Modifier = Modifier,
    favoriteUiState: FavoriteUiState,
    onBlogClick: (Blog) -> Unit,
    onRetryClick: () -> Unit,
    onRefresh: () -> Unit,
) {
    when (favoriteUiState) {
        FavoriteUiState.Loading -> {
            Center(modifier = modifier) {
                CircularProgressIndicator()
            }
        }

        is FavoriteUiState.Error -> {
            ErrorView(
                modifier = modifier,
                type = ErrorViewType.GENERAL_ERROR,
                onRetryActionClick = onRetryClick,
            )
        }

        FavoriteUiState.NoFavoriteBlogs -> {
            ErrorView(
                modifier = modifier,
                type = ErrorViewType.EMPTY_FAVORITE_BLOG,
            )
        }

        is FavoriteUiState.Success -> {
            FavoriteBlogList(
                modifier = modifier,
                favoriteBlogs = favoriteUiState.blogs.toImmutableList(),
                onBlogClick = onBlogClick,
                onRefresh = onRefresh,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoriteBlogList(
    modifier: Modifier = Modifier,
    favoriteBlogs: ImmutableList<Blog>,
    onBlogClick: (Blog) -> Unit,
    onRefresh: () -> Unit,
) {
    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = onRefresh,
    ) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        ) {
            itemsIndexed(
                favoriteBlogs,
                { _, blog -> blog.id },
            ) { index, blog ->
                BlogCard(
                    modifier = Modifier.fillMaxWidth(),
                    blog = blog,
                    onClick = {
                        onBlogClick(blog)
                    },
                )
                if (index < favoriteBlogs.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
