package me.dungngminh.lets_blog_kmp.presentation.detail_blog

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.ScreenTransition
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.ic_caret_left
import letsblogkmp.composeapp.generated.resources.ic_favorite
import letsblogkmp.composeapp.generated.resources.ic_favorite_filled
import letsblogkmp.composeapp.generated.resources.ic_gemini
import letsblogkmp.composeapp.generated.resources.ic_pencil
import letsblogkmp.composeapp.generated.resources.ic_trash
import letsblogkmp.composeapp.generated.resources.summary_screen_fab_label
import me.dungngminh.lets_blog_kmp.LocalWindowSizeClass
import me.dungngminh.lets_blog_kmp.commons.extensions.fromJsonStr
import me.dungngminh.lets_blog_kmp.commons.extensions.timeAgo
import me.dungngminh.lets_blog_kmp.commons.extensions.toJsonStr
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.entities.BlogCategory
import me.dungngminh.lets_blog_kmp.domain.entities.User
import me.dungngminh.lets_blog_kmp.presentation.detail_blog.summary.SummaryBlogContentBottomSheet
import me.dungngminh.lets_blog_kmp.presentation.edit_blog.EditBlogScreen
import me.dungngminh.lets_blog_kmp.presentation.login.LoginScreen
import me.dungngminh.lets_blog_kmp.presentation.main.MainScreen
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionState
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalVoyagerApi::class)
data class DetailBlogScreen(
    val blogId: String,
    val blogData: String,
) : Screen,
    ScreenTransition {
    override val key: ScreenKey
        get() = "DetailBlogScreen($blogId)"

    @Composable
    override fun Content() {
        val blog = remember(blogId) { blogData.fromJsonStr<Blog>() }

        val navigator = LocalNavigator.currentOrThrow

        val userSessionViewModel = navigator.koinNavigatorScreenModel<UserSessionViewModel>()

        val userSessionState by userSessionViewModel.userSessionState.collectAsStateWithLifecycle()

        val viewModel = koinScreenModel<DetailBlogViewModel> { parametersOf(blog) }

        val detailBlogState by viewModel.uiState.collectAsStateWithLifecycle()

        val summaryContentState by viewModel.summaryContentState.collectAsStateWithLifecycle()

        val blogContentRichTextState = rememberRichTextState()

        val windowSizeClass = LocalWindowSizeClass.currentOrThrow

        val snackbarHostState = remember { SnackbarHostState() }

        val coroutineScope = rememberCoroutineScope()

        var isSummaryBlogBottomSheetShown by remember { mutableStateOf(false) }

        LaunchedEffect(blogId) {
            blogContentRichTextState.setHtml(blog?.content.orEmpty())
        }

        LaunchedEffect(Unit) {
            viewModel
                .uiState
                .onEach { state ->
                    when {
                        state.isDeleteSuccess -> {
                            navigator.popUntil { it is MainScreen }
                        }
                    }
                }.launchIn(this)
        }

        DetailBlogScreenContent(
            blog = detailBlogState.blog,
            userSessionState = userSessionState,
            summaryBlogContentState = summaryContentState,
            snackbarHostState = snackbarHostState,
            blogContentRichTextState = blogContentRichTextState,
            isLargeScreen = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact,
            isSummaryBottomSheetShown = isSummaryBlogBottomSheetShown,
            onBackClick = navigator::pop,
            onFavoriteClick = {
                viewModel.favoriteBlog()
            },
            onUnFavoriteClick = {
                viewModel.unFavoriteBlog()
            },
            onUnAuthenticatedFavoriteClick = {
                navigator.push(LoginScreen)
            },
            onEditClick = {
                navigator.push(
                    EditBlogScreen(
                        blogId = detailBlogState.blog.id,
                        blogData = detailBlogState.blog.toJsonStr(),
                    ),
                )
            },
            onDeleteClick = viewModel::deleteBlog,
            onSummaryBlogClick = {
                isSummaryBlogBottomSheetShown = true
            },
            onSummaryBottomSheetDismiss = {
                isSummaryBlogBottomSheetShown = false
            },
            onSummaryBlogRetry = viewModel::retrySummaryBlog,
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
    modifier: Modifier = Modifier,
    blog: Blog,
    snackbarHostState: SnackbarHostState,
    userSessionState: UserSessionState,
    blogContentRichTextState: RichTextState,
    summaryBlogContentState: SummaryBlogContentState,
    isLargeScreen: Boolean = false,
    isSummaryBottomSheetShown: Boolean = false,
    onBackClick: () -> Unit,
    onFavoriteClick: (Blog) -> Unit,
    onUnFavoriteClick: (Blog) -> Unit,
    onUnAuthenticatedFavoriteClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onSummaryBottomSheetDismiss: () -> Unit = {},
    onSummaryBlogClick: () -> Unit = {},
    onSummaryBlogRetry: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            DetailBlogAppBar(
                blog = blog,
                userSessionState = userSessionState,
                onBackClick = onBackClick,
                onFavoriteClick = onFavoriteClick,
                onUnFavoriteClick = onUnFavoriteClick,
                onUnAuthenticatedFavoriteClick = onUnAuthenticatedFavoriteClick,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
            )
        },
        floatingActionButton = {
            SummaryBlogFabButton(onFabClick = onSummaryBlogClick)
        },
    ) { innerPadding ->

        if (isSummaryBottomSheetShown) {
            ModalBottomSheet(
                modifier = Modifier,
                onDismissRequest = onSummaryBottomSheetDismiss,
            ) {
                SummaryBlogContentBottomSheet(
                    summaryBlogContentState = summaryBlogContentState,
                    onRetryClick = onSummaryBlogRetry,
                )
            }
        }

        LazyColumn(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            contentPadding =
                PaddingValues(
                    bottom = 42.dp,
                    start = 16.dp,
                    end = 16.dp,
                ),
        ) {
            item(contentType = "blog_image") {
                CoilImage(
                    imageModel = { blog.imageUrl },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .clip(RoundedCornerShape(16.dp)),
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item(contentType = "blog_title") {
                Text(
                    text = blog.title,
                    style =
                        MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                        ),
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            item(contentType = "creator_info") {
                DetailBlogCreatorInfo(blog = blog)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item(contentType = "blog_content") {
                if (isLargeScreen) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.weight(0.3f))
                        RichText(
                            blogContentRichTextState,
                            modifier = Modifier.weight(0.6f),
                        )
                        Spacer(modifier = Modifier.weight(0.3f))
                    }
                } else {
                    RichText(blogContentRichTextState)
                }
            }
        }
    }
}

@Composable
fun DetailBlogCreatorInfo(
    modifier: Modifier = Modifier,
    blog: Blog,
) {
    Row(modifier = modifier) {
        CoilImage(
            imageModel = { blog.creator.avatarUrl },
            modifier =
                Modifier
                    .size(36.dp)
                    .clip(CircleShape),
            imageOptions = ImageOptions(contentScale = ContentScale.Crop),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                blog.creator.name,
                style =
                    MaterialTheme
                        .typography.titleMedium
                        .copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                blog.createdAt.timeAgo(),
                style =
                    MaterialTheme
                        .typography.bodyMedium,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBlogAppBar(
    modifier: Modifier = Modifier,
    blog: Blog,
    userSessionState: UserSessionState,
    onBackClick: () -> Unit = {},
    onFavoriteClick: (Blog) -> Unit,
    onUnFavoriteClick: (Blog) -> Unit,
    onUnAuthenticatedFavoriteClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
) {
    val isBlogCreatorSameAsUser by remember(userSessionState) {
        derivedStateOf {
            val user = (userSessionState as? UserSessionState.Authenticated)?.user
            user != null && user.id == blog.creator.id
        }
    }
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
            ) {
                Icon(
                    painterResource(Res.drawable.ic_caret_left),
                    contentDescription = "create_post_back_button",
                )
            }
        },
        title = {},
        actions = {
            if (isBlogCreatorSameAsUser) {
                IconButton(onClick = onEditClick) {
                    Icon(
                        painterResource(Res.drawable.ic_pencil),
                        contentDescription = "edit_button",
                        modifier = Modifier.size(24.dp),
                    )
                }

                IconButton(onClick = onDeleteClick) {
                    Icon(
                        painterResource(Res.drawable.ic_trash),
                        contentDescription = "delete_button",
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
            DetailScreenFavoriteButton(
                blog = blog,
                userSessionState = userSessionState,
                onFavoriteClick = onFavoriteClick,
                onUnFavoriteClick = onUnFavoriteClick,
                onUnAuthenticatedFavoriteClick = onUnAuthenticatedFavoriteClick,
            )
        },
    )
}

@Composable
fun SummaryBlogFabButton(
    modifier: Modifier = Modifier,
    onFabClick: () -> Unit,
) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        onClick = onFabClick,
    ) {
        Icon(
            painterResource(Res.drawable.ic_gemini),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
        )
        Spacer(Modifier.width(8.dp))
        Text(stringResource(Res.string.summary_screen_fab_label))
    }
}

@Composable
fun DetailScreenFavoriteButton(
    modifier: Modifier = Modifier,
    blog: Blog,
    userSessionState: UserSessionState,
    onFavoriteClick: (Blog) -> Unit,
    onUnFavoriteClick: (Blog) -> Unit,
    onUnAuthenticatedFavoriteClick: () -> Unit = {},
) {
    @Composable
    fun FavoriteButton(
        modifier: Modifier = Modifier,
        onFavoriteClick: (Blog) -> Unit = {},
        onUnFavoriteClick: (Blog) -> Unit = {},
    ) {
        IconButton(
            modifier = modifier,
            onClick = {
                if (blog.isFavoriteByUser == true) {
                    onUnFavoriteClick(blog)
                } else {
                    onFavoriteClick(blog)
                }
            },
        ) {
            Icon(
                if (blog.isFavoriteByUser == true) {
                    painterResource(Res.drawable.ic_favorite_filled)
                } else {
                    painterResource(Res.drawable.ic_favorite)
                },
                contentDescription = "favorite_button",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
    when (userSessionState) {
        is UserSessionState.Authenticated -> {
            if (userSessionState.user != null && userSessionState.user.id != blog.creator.id) {
                FavoriteButton(
                    modifier = modifier,
                    onFavoriteClick = onFavoriteClick,
                    onUnFavoriteClick = onUnFavoriteClick,
                )
            }
        }

        else ->
            FavoriteButton(
                modifier = modifier,
                onFavoriteClick = {
                    onUnAuthenticatedFavoriteClick()
                },
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun Preview_DetailBlogScreenContent() {
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
        userSessionState =
            UserSessionState.Authenticated(
                user =
                    User(
                        id = "sapien",
                        name = "Gilda Puckett",
                        email = "meghan.mooney@example.com",
                        avatarUrl = null,
                        follower = 6291,
                        following = 9751,
                    ),
            ),
        onFavoriteClick = {},
        onUnAuthenticatedFavoriteClick = {},
        onUnFavoriteClick = {},
        blogContentRichTextState = rememberRichTextState(),
        summaryBlogContentState = SummaryBlogContentState.Initial,
        onSummaryBlogClick = {},
        snackbarHostState = remember { SnackbarHostState() },
    )
}
