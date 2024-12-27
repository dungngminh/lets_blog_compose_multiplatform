package me.dungngminh.lets_blog_kmp.presentation.main.home

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.general_my_friend
import letsblogkmp.composeapp.generated.resources.home_screen_good_afternoon_label
import letsblogkmp.composeapp.generated.resources.home_screen_good_evening_label
import letsblogkmp.composeapp.generated.resources.home_screen_good_morning_label
import letsblogkmp.composeapp.generated.resources.home_screen_greeting_label
import letsblogkmp.composeapp.generated.resources.home_screen_tap_to_search_label
import letsblogkmp.composeapp.generated.resources.home_screen_welcome_to_lets_blog_label
import me.dungngminh.lets_blog_kmp.commons.extensions.timeAgo
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.entities.BlogCategory
import me.dungngminh.lets_blog_kmp.domain.entities.User
import me.dungngminh.lets_blog_kmp.presentation.components.CreateBlogFabButton
import me.dungngminh.lets_blog_kmp.presentation.create_blog.CreateBlogScreen
import me.dungngminh.lets_blog_kmp.presentation.detail_blog.DetailBlogScreen
import me.dungngminh.lets_blog_kmp.presentation.main.MainScreenDestination
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionState
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionViewModel
import org.jetbrains.compose.resources.stringResource

object HomeTab : Tab {
    @Composable
    override fun Content() {
        val tabNavigator = LocalTabNavigator.current
        val parentNavigator = LocalNavigator.currentOrThrow.parent ?: return

        val userSessionViewModel = parentNavigator.koinNavigatorScreenModel<UserSessionViewModel>()

        val homeViewModel = koinScreenModel<HomeScreenViewModel>()

        val userSessionState by userSessionViewModel.userSessionState.collectAsStateWithLifecycle()

        val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()

        HomeScreenContent(
            userSessionState = userSessionState,
            homeUiState = homeUiState,
            onSearchBarClick = {
                tabNavigator.current = MainScreenDestination.Search.tab
            },
            onBlogRefresh = {
                homeViewModel.refreshBlogs()
            },
            onBlogClick = {
                parentNavigator.push(DetailBlogScreen(it))
            },
            onCreateBlogClick = {
                parentNavigator.push(CreateBlogScreen)
            },
        )
    }

    override val options: TabOptions
        @Composable get() =
            TabOptions(
                index =
                    MainScreenDestination.entries
                        .indexOf(MainScreenDestination.Home)
                        .toUShort(),
                title = stringResource(MainScreenDestination.Home.title),
            )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    userSessionState: UserSessionState,
    homeUiState: HomeScreenUiState,
    onSearchBarClick: () -> Unit,
    onBlogRefresh: () -> Unit,
    onBlogClick: (Blog) -> Unit = {},
    onCreateBlogClick: () -> Unit,
) {
    val refreshState = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            CreateBlogFabButton {
                onCreateBlogClick()
            }
        },
    ) { innerPadding ->
        PullToRefreshBox(
            state = refreshState,
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            isRefreshing = false,
            onRefresh = {
                // Just for refresh, loading animation will use shimmer instead of
                coroutineScope.launch {
                    onBlogRefresh()
                    refreshState.animateToHidden()
                }
            },
        ) {
            LazyColumn {
                item {
                    HomeGreeting(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clickable {
                                    onBlogRefresh()
                                },
                        userSessionState = userSessionState,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    HomeSearchBar(
                        modifier = modifier.padding(horizontal = 16.dp),
                    ) {
                        onSearchBarClick()
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
                blogContentView(
                    homeUiState = homeUiState,
                    onBlogClick = onBlogClick,
                )
            }
        }
    }
}

@Composable
private fun HomeSearchBar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable {
                    onClick()
                }.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                ),
    ) {
        Text(
            stringResource(Res.string.home_screen_tap_to_search_label),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }
}

private fun LazyListScope.blogContentView(
    homeUiState: HomeScreenUiState,
    onBlogClick: (Blog) -> Unit,
) {
    when {
        homeUiState.errorMessage != null -> {
            item {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(homeUiState.errorMessage)
                }
            }
        }

        homeUiState.isLoading -> {
            item {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        else ->
            blogsView(
                blogs = homeUiState.blogs.toImmutableList(),
                onBlogClick = onBlogClick,
            )
    }
}

private fun LazyListScope.blogsView(
    blogs: ImmutableList<Blog>,
    onBlogClick: (Blog) -> Unit,
) {
    if (blogs.isEmpty()) {
        item {
            EmptyBlogView()
        }
    } else {
        items(
            blogs,
            key = { it.id },
        ) { blog ->
            BlogCard(
                blog = blog,
                modifier =
                    Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                onClick = {
                    onBlogClick(blog)
                },
            )
            val isLast by remember { derivedStateOf { blog == blogs.lastOrNull() } }
            if (!isLast) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun EmptyBlogView() {
    Text("No blogs found")
}

@Composable
private fun BlogCard(
    modifier: Modifier = Modifier,
    blog: Blog,
    onClick: () -> Unit = {},
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CoilImage(
                imageModel = { blog.imageUrl },
                modifier =
                    Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp)),
                imageOptions = ImageOptions(contentScale = ContentScale.Crop),
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    blog.title,
                    style =
                        MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                )
                Text(
                    blog.createdAt.timeAgo(),
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    blog.creator.name,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun HomeGreeting(
    modifier: Modifier = Modifier,
    userSessionState: UserSessionState,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                stringResource(
                    Res.string.home_screen_greeting_label,
                    greetingByTime(),
                    when (userSessionState) {
                        is UserSessionState.Authenticated -> userSessionState.user.name
                        else -> stringResource(Res.string.general_my_friend)
                    },
                ),
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                stringResource(
                    Res.string.home_screen_welcome_to_lets_blog_label,
                ),
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Box(
            modifier =
                Modifier
                    .size(48.dp)
                    .clip(CircleShape),
        ) {
            if (userSessionState is UserSessionState.Authenticated) {
                CoilImage(
                    imageModel = { userSessionState.user.avatarUrl },
                    imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                    loading = {
                        CircularProgressIndicator()
                    },
                )
            }
        }
    }
}

@Composable
private fun greetingByTime(): String {
    val time =
        remember {
            Clock.System
                .now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .hour
        }
    return when {
        time < 12 -> stringResource(Res.string.home_screen_good_morning_label)
        time < 18 -> stringResource(Res.string.home_screen_good_afternoon_label)
        else -> stringResource(Res.string.home_screen_good_evening_label)
    }
}

@Preview
@Composable
fun PreviewBlogCard() {
    MaterialTheme {
        BlogCard(
            blog =
                Blog(
                    id = "libero",
                    title = "pri",
                    content = "verterem",
                    imageUrl = "https://duckduckgo.com/?q=vituperatoribus",
                    category = BlogCategory.TRAVEL,
                    createdAt = 7578,
                    updatedAt = 9168,
                    creator =
                        User(
                            id = "propriae",
                            name = "Gregorio Stone",
                            email = "dadad",
                        ),
                ),
        )
    }
}

@Preview
@Composable
fun PreviewHomeScreenContent() {
    HomeScreenContent(
        userSessionState = UserSessionState.Initial,
        onSearchBarClick = {
        },
        onBlogRefresh = {},
        onBlogClick = {},
        onCreateBlogClick = {},
        homeUiState =
            HomeScreenUiState(
                blogs =
                    persistentListOf(
                        Blog(
                            id = "libero",
                            title = "pri",
                            content = "verterem",
                            imageUrl = "https://duckduckgo.com/?q=vituperatoribus",
                            category = BlogCategory.TRAVEL,
                            createdAt = 7578,
                            updatedAt = 9168,
                            creator =
                                User(
                                    id = "propriae",
                                    name = "Gregorio Stone",
                                    email = "clare.vasquez@example.com",
                                    avatarUrl = null,
                                    follower = 5284,
                                    following = 4253,
                                ),
                        ),
                        Blog(
                            id = "libero1",
                            title = "pri",
                            content = "verterem",
                            imageUrl = "https://duckduckgo.com/?q=vituperatoribus",
                            category = BlogCategory.TRAVEL,
                            createdAt = 7578,
                            updatedAt = 9168,
                            creator =
                                User(
                                    id = "propriae",
                                    name = "Gregorio Stone",
                                    email = "clare.vasquez@example.com",
                                    avatarUrl = null,
                                    follower = 5284,
                                    following = 4253,
                                ),
                        ),
                        Blog(
                            id = "libero2",
                            title = "pri",
                            content = "verterem",
                            imageUrl = "https://duckduckgo.com/?q=vituperatoribus",
                            category = BlogCategory.TRAVEL,
                            createdAt = 7578,
                            updatedAt = 9168,
                            creator =
                                User(
                                    id = "propriae",
                                    name = "Gregorio Stone",
                                    email = "clare.vasquez@example.com",
                                    avatarUrl = null,
                                    follower = 5284,
                                    following = 4253,
                                ),
                        ),
                        Blog(
                            id = "libero4",
                            title = "pri",
                            content = "verterem",
                            imageUrl = "https://duckduckgo.com/?q=vituperatoribus",
                            category = BlogCategory.TRAVEL,
                            createdAt = 7578,
                            updatedAt = 9168,
                            creator =
                                User(
                                    id = "propriae",
                                    name = "Gregorio Stone",
                                    email = "clare.vasquez@example.com",
                                    avatarUrl = null,
                                    follower = 5284,
                                    following = 4253,
                                ),
                        ),
                    ),
            ),
    )
}

@Preview
@Composable
fun PreviewHomeGreetings() {
    HomeGreeting(
        modifier = Modifier.padding(16.dp),
        userSessionState = UserSessionState.Initial,
    )
}
