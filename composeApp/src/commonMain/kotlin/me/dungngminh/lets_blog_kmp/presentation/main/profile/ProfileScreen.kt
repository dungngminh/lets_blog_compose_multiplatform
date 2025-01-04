package me.dungngminh.lets_blog_kmp.presentation.main.profile

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.skydoves.landscapist.coil3.CoilImage
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.general_blog_count
import letsblogkmp.composeapp.generated.resources.ic_pencil
import letsblogkmp.composeapp.generated.resources.ic_setting
import me.dungngminh.lets_blog_kmp.LocalWindowSizeClass
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.entities.User
import me.dungngminh.lets_blog_kmp.presentation.components.BlogCard
import me.dungngminh.lets_blog_kmp.presentation.components.Center
import me.dungngminh.lets_blog_kmp.presentation.detail_blog.DetailBlogScreen
import me.dungngminh.lets_blog_kmp.presentation.edit_user_profile.EditUserProfileScreen
import me.dungngminh.lets_blog_kmp.presentation.main.MainScreenDestination
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionState
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionViewModel
import me.dungngminh.lets_blog_kmp.presentation.main.profile.components.ErrorProfileContent
import me.dungngminh.lets_blog_kmp.presentation.main.profile.components.UnauthenticatedProfileContent
import me.dungngminh.lets_blog_kmp.presentation.setting.SettingScreen
import me.dungngminh.lets_blog_kmp.presentation.sign_in.SignInScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

object ProfileTab : Tab {
    @Composable
    override fun Content() {
        val parent = LocalNavigator.currentOrThrow.parent ?: return

        val userSessionViewModel = parent.koinNavigatorScreenModel<UserSessionViewModel>()

        val userProfileViewModel = koinScreenModel<ProfileViewModel>()

        val userSessionState by userSessionViewModel.userSessionState.collectAsStateWithLifecycle()

        val userBlogState by userProfileViewModel.userBlogState.collectAsStateWithLifecycle()

        ProfileScreenContent(
            userSessionState = userSessionState,
            userBlogState = userBlogState,
            onLoginClick = {
                parent.push(SignInScreen)
            },
            onRefresh = {
                userProfileViewModel.refresh()
                userSessionViewModel.refresh()
            },
            onRetryClick = {
            },
            onSettingClick = {
                parent.push(SettingScreen)
            },
            onEditProfileClick = {
                parent.push(EditUserProfileScreen)
            },
            onBlogClick = { blog ->
                parent.push(DetailBlogScreen(blog))
            },
        )
    }

    override val options: TabOptions
        @Composable
        get() =
            TabOptions(
                index =
                    MainScreenDestination.entries
                        .indexOf(MainScreenDestination.Profile)
                        .toUShort(),
                title = stringResource(MainScreenDestination.Profile.title),
            )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    userSessionState: UserSessionState,
    onLoginClick: () -> Unit,
    onRefresh: () -> Unit,
    onRetryClick: () -> Unit,
    onSettingClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onBlogClick: (Blog) -> Unit,
    userBlogState: UserBlogState,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val windowSizeClass = LocalWindowSizeClass.currentOrThrow

    val isLargeScreen by remember { derivedStateOf { windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact } }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (!isLargeScreen) {
                ProfileAppBar(
                    user = (userSessionState as? UserSessionState.Authenticated)?.user,
                    onSettingClick = onSettingClick,
                    onEditProfileClick = onEditProfileClick,
                    scrollBehavior = scrollBehavior,
                )
            }
        },
    ) { innerPadding ->
        when (userSessionState) {
            UserSessionState.Initial ->
                Center {
                    CircularProgressIndicator()
                }

            is UserSessionState.AuthenticatedFetchDataError ->
                ErrorProfileContent(
                    modifier = Modifier.padding(innerPadding),
                    onRetryClick = onRetryClick,
                )

            UserSessionState.Unauthenticated ->
                UnauthenticatedProfileContent(
                    modifier = Modifier.padding(innerPadding),
                    onLoginClick = onLoginClick,
                )

            is UserSessionState.Authenticated -> {
                if (userSessionState.user == null) {
                    Center {
                        CircularProgressIndicator()
                    }
                } else {
                    AuthenticatedProfileScreen(
                        modifier =
                            Modifier
                                .padding(innerPadding),
                        onRefresh = onRefresh,
                        userBlogState = userBlogState,
                        onBlogClick = onBlogClick,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticatedProfileScreen(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    userBlogState: UserBlogState,
    onBlogClick: (Blog) -> Unit = {},
) {
    val refreshState = rememberPullToRefreshState()

    val coroutineScope = rememberCoroutineScope()

    PullToRefreshBox(
        modifier = modifier,
        state = refreshState,
        isRefreshing = false,
        onRefresh = {
            coroutineScope.launch {
                onRefresh()
                refreshState.animateToHidden()
            }
        },
    ) {
        when (userBlogState) {
            UserBlogState.Uninitialized -> Unit
            UserBlogState.Loading ->
                Center {
                    CircularProgressIndicator()
                }

            is UserBlogState.Error ->
                Center {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.error)
                }

            is UserBlogState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                ) {
                    itemsIndexed(
                        userBlogState.blogs.toImmutableList(),
                        key = { _, blog -> blog.id },
                    ) { index, blog ->
                        BlogCard(
                            blog = blog,
                            onClick = { onBlogClick(blog) },
                        )
                        if (index < userBlogState.blogs.size - 1) {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileAppBar(
    modifier: Modifier = Modifier,
    onSettingClick: () -> Unit,
    user: User? = null,
    onEditProfileClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val collapsedValue by remember(scrollBehavior) { derivedStateOf { scrollBehavior.state.collapsedFraction } }
    val isExpanded by remember(collapsedValue) { derivedStateOf { collapsedValue == 0f } }
    val transition = updateTransition(collapsedValue)
    val avatarSize by transition.animateDp {
        60.dp + (42.dp - 60.dp) * it
    }
    MediumTopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        expandedHeight = 150.dp,
        title = {
            if (user != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier =
                            Modifier
                                .size(avatarSize)
                                .clip(CircleShape),
                    ) {
                        CoilImage(
                            imageModel = { "https://avatars.githubusercontent.com/u/63831488?v=4" },
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                    Spacer(
                        modifier = Modifier.width(16.dp),
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            user.name,
                            style =
                                MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = if (isExpanded) 24.sp else 22.sp,
                                ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            pluralStringResource(
                                Res.plurals.general_blog_count,
                                user.blogCount,
                                user.blogCount,
                            ),
                            style =
                                MaterialTheme.typography.titleMedium.copy(
                                    fontSize = if (isExpanded) 16.sp else 14.sp,
                                ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        },
        actions = {
            if (user != null) {
                IconButton(onClick = onEditProfileClick) {
                    Icon(
                        painterResource(Res.drawable.ic_pencil),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
            IconButton(
                onClick = onSettingClick,
            ) {
                Icon(
                    painterResource(Res.drawable.ic_setting),
                    contentDescription = null,
                )
            }
        },
        colors =
            TopAppBarDefaults
                .centerAlignedTopAppBarColors()
                .copy(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                ),
    )
}
