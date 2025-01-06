package me.dungngminh.lets_blog_kmp.presentation.main.home

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.home_screen_other_blogs_label
import letsblogkmp.composeapp.generated.resources.home_screen_popular_blogs_label
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.entities.BlogCategory
import me.dungngminh.lets_blog_kmp.domain.entities.User
import me.dungngminh.lets_blog_kmp.presentation.components.BlogCard
import me.dungngminh.lets_blog_kmp.presentation.components.Center
import me.dungngminh.lets_blog_kmp.presentation.components.CreateBlogFabButton
import me.dungngminh.lets_blog_kmp.presentation.components.PopularBlogCard
import me.dungngminh.lets_blog_kmp.presentation.create_blog.CreateBlogScreen
import me.dungngminh.lets_blog_kmp.presentation.detail_blog.DetailBlogScreen
import me.dungngminh.lets_blog_kmp.presentation.main.MainScreenDestination
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionState
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionViewModel
import me.dungngminh.lets_blog_kmp.presentation.main.home.components.EmptyBlogView
import me.dungngminh.lets_blog_kmp.presentation.main.home.components.HomeGreeting
import me.dungngminh.lets_blog_kmp.presentation.main.home.components.HomeSearchBar
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
            onBlogRefresh = homeViewModel::fetchBlogs,
            onBlogClick = {
                parentNavigator.push(DetailBlogScreen(it))
            },
            onCreateBlogClick = {
                parentNavigator.push(CreateBlogScreen)
            },
            onFavoriteBlogClick = homeViewModel::favoritePopularBlog,
            onUnFavoriteBlogClick = homeViewModel::unFavoritePopularBlog,
            fetchNewBlogs = homeViewModel::loadMoreBlogs,
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
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    userSessionState: UserSessionState,
    homeUiState: HomeScreenUiState,
    onSearchBarClick: () -> Unit,
    onBlogRefresh: () -> Unit,
    onBlogClick: (Blog) -> Unit = {},
    onFavoriteBlogClick: (Blog) -> Unit,
    onCreateBlogClick: () -> Unit,
    onUnFavoriteBlogClick: (Blog) -> Unit,
    fetchNewBlogs: () -> Unit,
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
            LazyColumn(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp),
            ) {
                item {
                    HomeGreeting(
                        modifier =
                            Modifier
                                .padding(16.dp),
                        username =
                            when (userSessionState) {
                                is UserSessionState.Authenticated -> userSessionState.user?.name
                                else -> null
                            },
                        userAvatarUrl =
                            when (userSessionState) {
                                is UserSessionState.Authenticated -> userSessionState.user?.avatarUrl
                                else -> null
                            },
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    HomeSearchBar(
                        modifier = modifier.padding(horizontal = 16.dp),
                        onClick = onSearchBarClick,
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
                blogContentView(
                    homeUiState = homeUiState,
                    onBlogClick = onBlogClick,
                    onFavoriteBlogClick = onFavoriteBlogClick,
                    onUnFavoriteBlogClick = onUnFavoriteBlogClick,
                    fetchNewBlogs = fetchNewBlogs,
                    user =
                        when (userSessionState) {
                            is UserSessionState.Authenticated -> userSessionState.user
                            else -> null
                        },
                )
            }
        }
    }
}

fun LazyListScope.blogContentView(
    homeUiState: HomeScreenUiState,
    user: User? = null,
    onBlogClick: (Blog) -> Unit,
    onFavoriteBlogClick: (Blog) -> Unit,
    onUnFavoriteBlogClick: (Blog) -> Unit,
    fetchNewBlogs: () -> Unit,
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
                Center {
                    CircularProgressIndicator()
                }
            }
        }

        else ->
            blogsView(
                blogs = homeUiState.blogs.toImmutableList(),
                popularBlogs = homeUiState.popularBlogs.toImmutableList(),
                onBlogClick = onBlogClick,
                onFavoriteBlogClick = onFavoriteBlogClick,
                onUnFavoriteBlogClick = onUnFavoriteBlogClick,
                isLoadingMore = homeUiState.isLoadingMore,
                fetchNewBlogs = fetchNewBlogs,
                user = user,
            )
    }
}

fun LazyListScope.blogsView(
    popularBlogs: ImmutableList<Blog>,
    blogs: ImmutableList<Blog>,
    user: User? = null,
    isLoadingMore: Boolean = false,
    onBlogClick: (Blog) -> Unit,
    onFavoriteBlogClick: (Blog) -> Unit,
    onUnFavoriteBlogClick: (Blog) -> Unit,
    fetchNewBlogs: () -> Unit,
) {
    val isBlogListEmpty = blogs.isEmpty() && popularBlogs.isEmpty()
    if (isBlogListEmpty) {
        item {
            EmptyBlogView()
        }
    } else {
        // Popular blogs
        popularBlogContentView(
            popularBlogs = popularBlogs,
            onBlogClick = onBlogClick,
            onUnFavoriteBlogClick = onUnFavoriteBlogClick,
            onFavoriteBlogClick = onFavoriteBlogClick,
            user = user,
        )
        // Normal blogs
        if (popularBlogs.isNotEmpty()) {
            item {
                Text(
                    stringResource(Res.string.home_screen_other_blogs_label),
                    modifier =
                        Modifier.padding(
                            top = 16.dp,
                            bottom = 12.dp,
                            start = 16.dp,
                        ),
                )
            }
        }
        otherBlogsContentView(
            blogs = blogs,
            onBlogClick = onBlogClick,
            isLoadingMore = isLoadingMore,
            fetchNewBlogs = fetchNewBlogs,
        )
    }
}

fun LazyListScope.otherBlogsContentView(
    blogs: ImmutableList<Blog>,
    isLoadingMore: Boolean,
    onBlogClick: (Blog) -> Unit,
    fetchNewBlogs: () -> Unit,
) {
    val threadHold = 5
    itemsIndexed(
        blogs,
        key = { _, blog -> blog.id },
    ) { index, blog ->
        if ((index + threadHold) >= blogs.size && !isLoadingMore) {
            fetchNewBlogs()
        }
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
        if (index < blogs.size - 1) {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

fun LazyListScope.popularBlogContentView(
    popularBlogs: ImmutableList<Blog>,
    user: User? = null,
    onBlogClick: (Blog) -> Unit,
    onFavoriteBlogClick: (Blog) -> Unit,
    onUnFavoriteBlogClick: (Blog) -> Unit,
) {
    if (popularBlogs.isNotEmpty()) {
        item {
            Text(
                stringResource(Res.string.home_screen_popular_blogs_label),
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                modifier =
                    Modifier
                        .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
            ) {
                itemsIndexed(
                    popularBlogs,
                    key = { _, blog -> blog.id },
                ) { index, blog ->
                    PopularBlogCard(
                        blog = blog,
                        onClick = onBlogClick,
                        onFavoriteClick = onFavoriteBlogClick,
                        onUnFavoriteBlogClick = onUnFavoriteBlogClick,
                        user = user,
                    )
                    val isLast = index == popularBlogs.size - 1
                    if (!isLast) {
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview_HomeScreenContent() {
    HomeScreenContent(
        userSessionState = UserSessionState.Initial,
        onSearchBarClick = {
        },
        onBlogRefresh = {},
        onBlogClick = {},
        onCreateBlogClick = {},
        onFavoriteBlogClick = {},
        onUnFavoriteBlogClick = {},
        homeUiState =
            HomeScreenUiState(
                isLoading = false,
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
        fetchNewBlogs = {},
    )
}
