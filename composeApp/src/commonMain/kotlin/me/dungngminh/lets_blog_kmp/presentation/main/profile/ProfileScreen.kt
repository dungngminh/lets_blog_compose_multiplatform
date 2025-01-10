package me.dungngminh.lets_blog_kmp.presentation.main.profile

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
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
import letsblogkmp.composeapp.generated.resources.general_no
import letsblogkmp.composeapp.generated.resources.general_sign_out_label
import letsblogkmp.composeapp.generated.resources.general_sign_out_message
import letsblogkmp.composeapp.generated.resources.general_yes
import letsblogkmp.composeapp.generated.resources.ic_pencil
import letsblogkmp.composeapp.generated.resources.ic_sign_out
import letsblogkmp.composeapp.generated.resources.img_placeholder
import letsblogkmp.composeapp.generated.resources.profile_screen_no_blog
import me.dungngminh.lets_blog_kmp.LocalWindowSizeClass
import me.dungngminh.lets_blog_kmp.commons.extensions.toJsonStr
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.entities.User
import me.dungngminh.lets_blog_kmp.presentation.components.BlogCard
import me.dungngminh.lets_blog_kmp.presentation.components.Center
import me.dungngminh.lets_blog_kmp.presentation.components.ErrorView
import me.dungngminh.lets_blog_kmp.presentation.components.ErrorViewType
import me.dungngminh.lets_blog_kmp.presentation.create_blog.CreateBlogScreen
import me.dungngminh.lets_blog_kmp.presentation.detail_blog.DetailBlogScreen
import me.dungngminh.lets_blog_kmp.presentation.edit_user_profile.EditUserProfileScreen
import me.dungngminh.lets_blog_kmp.presentation.login.LoginScreen
import me.dungngminh.lets_blog_kmp.presentation.main.MainScreenDestination
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionState
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionViewModel
import me.dungngminh.lets_blog_kmp.presentation.main.profile.components.UnauthenticatedProfileContent
import me.dungngminh.lets_blog_kmp.presentation.setting.SettingScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

object ProfileTab : Tab {
    @Composable
    override fun Content() {
        val rootNavigator = LocalNavigator.currentOrThrow.parent ?: return

        val userSessionViewModel = rootNavigator.koinNavigatorScreenModel<UserSessionViewModel>()

        val userProfileViewModel = koinScreenModel<ProfileViewModel>()

        val userSessionState by userSessionViewModel.userSessionState.collectAsStateWithLifecycle()

        val userBlogState by userProfileViewModel.userBlogState.collectAsStateWithLifecycle()

        val windowSizeClass = LocalWindowSizeClass.currentOrThrow

        var isSignOutDialogOpen by remember { mutableStateOf(false) }

        if (isSignOutDialogOpen) {
            AlertDialog(
                onDismissRequest = { isSignOutDialogOpen = false },
                title = { Text(stringResource(Res.string.general_sign_out_label)) },
                text = { Text(stringResource(Res.string.general_sign_out_message)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            userSessionViewModel.logout()
                            isSignOutDialogOpen = false
                        },
                    ) {
                        Text(stringResource(Res.string.general_yes))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            isSignOutDialogOpen = false
                        },
                    ) {
                        Text(stringResource(Res.string.general_no))
                    }
                },
            )
        }

        if (windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact) {
            ProfileScreenExpandedContent(
                userSessionState = userSessionState,
                onLoginClick = {
                    rootNavigator.push(LoginScreen)
                },
                onRefresh = {
                    userProfileViewModel.refresh()
                    userSessionViewModel.refresh()
                },
                onRetryClick = {
                },
                onSettingClick = {
                    rootNavigator.push(SettingScreen)
                },
                onEditProfileClick = {
                    rootNavigator.push(EditUserProfileScreen)
                },
                userBlogState = userBlogState,
                onBlogClick = { blog ->
                    rootNavigator.push(
                        DetailBlogScreen(
                            blogId = blog.id,
                            blogData = blog.toJsonStr(),
                        ),
                    )
                },
            )
        } else {
            ProfileScreenContent(
                userSessionState = userSessionState,
                userBlogState = userBlogState,
                onLoginClick = {
                    rootNavigator.push(LoginScreen)
                },
                onRefresh = {
                    userProfileViewModel.refresh()
                    userSessionViewModel.refresh()
                },
                onRetryClick = userSessionViewModel::retry,
                onSignOutClick = {
                    isSignOutDialogOpen = true
                },
                onEditProfileClick = {
                    rootNavigator.push(EditUserProfileScreen)
                },
                onBlogClick = { blog ->
                    rootNavigator.push(
                        DetailBlogScreen(
                            blogId = blog.id,
                            blogData = blog.toJsonStr(),
                        ),
                    )
                },
                onCreateBlogClick = {
                    rootNavigator.push(CreateBlogScreen)
                },
                onRetryBlogClick = userProfileViewModel::retry,
            )
        }
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

@Composable
private fun ProfileScreenExpandedContent(
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
    Scaffold {
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    userSessionState: UserSessionState,
    userBlogState: UserBlogState,
    onLoginClick: () -> Unit = {},
    onRefresh: () -> Unit = {},
    onRetryClick: () -> Unit = {},
    onSignOutClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    onBlogClick: (Blog) -> Unit = {},
    onCreateBlogClick: () -> Unit = {},
    onRetryBlogClick: () -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (userSessionState is UserSessionState.Authenticated) {
                ProfileAppBar(
                    user = userSessionState.user,
                    onSignOutClick = onSignOutClick,
                    onEditProfileClick = onEditProfileClick,
                    scrollBehavior = scrollBehavior,
                )
            }
        },
    ) { innerPadding ->
        when (userSessionState) {
            UserSessionState.Initial, UserSessionState.Loading ->
                Center {
                    CircularProgressIndicator()
                }

            is UserSessionState.AuthenticatedFetchDataError ->
                ErrorView(
                    modifier =
                        Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                    type = ErrorViewType.GENERAL_ERROR,
                    onActionClick = onRetryClick,
                )

            UserSessionState.Unauthenticated ->
                UnauthenticatedProfileContent(
                    modifier = Modifier.padding(innerPadding),
                    onLoginClick = onLoginClick,
                )

            is UserSessionState.Authenticated -> {
                AuthenticatedProfileScreen(
                    modifier =
                        Modifier
                            .padding(innerPadding),
                    onRefresh = onRefresh,
                    userBlogState = userBlogState,
                    onBlogClick = onBlogClick,
                    onCreateBlogClick = onCreateBlogClick,
                    onRetryBlogClick = onRetryBlogClick,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticatedProfileScreen(
    modifier: Modifier = Modifier,
    userBlogState: UserBlogState,
    onRefresh: () -> Unit = {},
    onRetryBlogClick: () -> Unit = {},
    onBlogClick: (Blog) -> Unit = {},
    onCreateBlogClick: () -> Unit = {},
) {
    val refreshState = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()

    when (userBlogState) {
        UserBlogState.Loading, UserBlogState.Uninitialized ->
            Center(modifier = modifier) {
                CircularProgressIndicator()
            }

        is UserBlogState.Error ->
            ErrorView(
                modifier = modifier.fillMaxWidth(),
                type = ErrorViewType.GENERAL_ERROR,
                onActionClick = onRetryBlogClick,
            )

        UserBlogState.EmptyBlog -> {
            ErrorView(
                modifier = modifier.fillMaxWidth(),
                type = ErrorViewType.EMPTY_USER_BLOG,
                onActionClick = onCreateBlogClick,
            )
        }

        is UserBlogState.Success -> {
            PullToRefreshBox(
                state = refreshState,
                modifier = modifier,
                isRefreshing = false,
                onRefresh = {
                    coroutineScope.launch {
                        onRefresh()
                        refreshState.animateToHidden()
                    }
                },
            ) {
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
    onSignOutClick: () -> Unit,
    user: User,
    onEditProfileClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val isExpanded by remember { derivedStateOf { scrollBehavior.state.collapsedFraction == 0f } }
    val transition = updateTransition(scrollBehavior.state.collapsedFraction)
    val avatarSize by transition.animateDp {
        60.dp + (42.dp - 60.dp) * it
    }
    MediumTopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        expandedHeight = 150.dp,
        title = {
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
                        imageModel = { user.avatarUrl },
                        loading = {
                            CircularProgressIndicator()
                        },
                        failure = {
                            Image(
                                painterResource(Res.drawable.img_placeholder),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                            )
                        },
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
                        if (user.blogCount == 0) {
                            // Idk plural zero is not working
                            stringResource(Res.string.profile_screen_no_blog)
                        } else {
                            pluralStringResource(
                                Res.plurals.general_blog_count,
                                user.blogCount,
                                user.blogCount,
                            )
                        },
                        style =
                            MaterialTheme.typography.titleMedium.copy(
                                fontSize = if (isExpanded) 16.sp else 14.sp,
                            ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onEditProfileClick) {
                Icon(
                    painterResource(Res.drawable.ic_pencil),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            }
            IconButton(
                onClick = onSignOutClick,
            ) {
                Icon(
                    painterResource(Res.drawable.ic_sign_out),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
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
