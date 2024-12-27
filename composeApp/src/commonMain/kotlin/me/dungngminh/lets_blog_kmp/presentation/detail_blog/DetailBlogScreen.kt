package me.dungngminh.lets_blog_kmp.presentation.detail_blog

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.ScreenTransition
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.entities.BlogCategory
import me.dungngminh.lets_blog_kmp.domain.entities.User

@OptIn(ExperimentalVoyagerApi::class)
data class DetailBlogScreen(
    val blog: Blog,
) : Screen,
    ScreenTransition {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel { DetailBlogViewModel(blog = blog) }

        DetailBlogScreenContent(
            blog = blog,
            onBackClick = {
                navigator.pop()
            },
        )
    }

    override fun enter(lastEvent: StackEvent): EnterTransition =
        slideIn { size ->
            val x = if (lastEvent == StackEvent.Pop) -size.width else size.width
            IntOffset(x = x, y = 0)
        }

    override fun exit(lastEvent: StackEvent): ExitTransition =
        slideOut { size ->
            val x = if (lastEvent == StackEvent.Pop) size.width else -size.width
            IntOffset(x = x, y = 0)
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBlogScreenContent(
    blog: Blog,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                    ) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "create_post_back_button",
                        )
                    }
                },
                title = {
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
        ) {
            CoilImage(
                imageModel = { blog.imageUrl },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                imageOptions = ImageOptions(contentScale = ContentScale.Crop),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = blog.title, style = MaterialTheme.typography.headlineLarge)
        }
    }
}

@Preview
@Composable
fun PreviewDetailBlogScreenContent() {
    DetailBlogScreenContent(
        blog =
            Blog(
                id = "quem",
                title = "mentitum",
                content = "sollicitudin",
                imageUrl = "https://search.yahoo.com/search?p=elit",
                category = BlogCategory.TRAVEL,
                createdAt = 3576,
                updatedAt = 2941,
                creator =
                    User(
                        id = "verterem",
                        name = "Concetta Whitley",
                        email = "marissa.mooney@example.com",
                        avatarUrl = null,
                        follower = 8480,
                        following = 3629,
                    ),
            ),
        onBackClick = {},
    )
}
