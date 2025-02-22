package me.dungngminh.lets_blog_kmp.presentation.main.favorite

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.favorite_screen_favorite_blogs_label
import letsblogkmp.composeapp.generated.resources.ic_refresh
import me.dungngminh.lets_blog_kmp.LocalWindowSizeClass
import me.dungngminh.lets_blog_kmp.commons.extensions.toJsonStr
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.presentation.components.Center
import me.dungngminh.lets_blog_kmp.presentation.detail_blog.DetailBlogScreen
import me.dungngminh.lets_blog_kmp.presentation.login.LoginScreen
import me.dungngminh.lets_blog_kmp.presentation.main.MainScreenDestination
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionState
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionViewModel
import me.dungngminh.lets_blog_kmp.presentation.main.favorite.components.AuthenticatedFavoriteContent
import me.dungngminh.lets_blog_kmp.presentation.main.profile.components.UnauthenticatedProfileContent
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

object FavoriteTab : Tab {
    @Composable
    override fun Content() {
        val rootNavigator = LocalNavigator.currentOrThrow.parent ?: return

        val userSessionViewModel = rootNavigator.koinNavigatorScreenModel<UserSessionViewModel>()

        val favoriteViewModel = koinScreenModel<FavoriteViewModel>()

        val userSessionState by userSessionViewModel.userSessionState.collectAsStateWithLifecycle()

        val favoriteUiState by favoriteViewModel.uiState.collectAsStateWithLifecycle()

        val windowSizeClass = LocalWindowSizeClass.currentOrThrow

        FavoriteScreenContent(
            favoriteUiState = favoriteUiState,
            userSessionState = userSessionState,
            onBlogClick = { blog ->
                rootNavigator.push(
                    DetailBlogScreen(
                        blogId = blog.id,
                        blogData = blog.toJsonStr(),
                    ),
                )
            },
            onRetryClick = favoriteViewModel::retry,
            onRefresh = favoriteViewModel::refresh,
            onLoginClick = {
                rootNavigator.push(LoginScreen)
            },
            isMediumScreen = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium,
            isExpandedScreen = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded,
        )
    }

    override val options: TabOptions
        @Composable
        get() =
            TabOptions(
                index =
                    MainScreenDestination.entries
                        .indexOf(MainScreenDestination.Favorite)
                        .toUShort(),
                title = stringResource(MainScreenDestination.Favorite.title),
            )
}

@Composable
fun FavoriteScreenContent(
    modifier: Modifier = Modifier,
    favoriteUiState: FavoriteUiState,
    userSessionState: UserSessionState,
    isMediumScreen: Boolean,
    isExpandedScreen: Boolean,
    onBlogClick: (Blog) -> Unit,
    onRetryClick: () -> Unit,
    onRefresh: () -> Unit,
    onLoginClick: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            val showRefresh =
                (isMediumScreen || isExpandedScreen) && userSessionState is UserSessionState.Authenticated
            FavoriteAppBar(
                showRefresh = showRefresh,
                onRefresh = onRefresh,
            )
        },
    ) { innerPadding ->
        when (userSessionState) {
            UserSessionState.Initial, UserSessionState.Loading ->
                Center {
                    CircularProgressIndicator()
                }

            UserSessionState.Unauthenticated ->
                UnauthenticatedProfileContent(
                    modifier = Modifier.padding(innerPadding),
                    onLoginClick = onLoginClick,
                )

            else ->
                AuthenticatedFavoriteContent(
                    modifier =
                        Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                    favoriteUiState = favoriteUiState,
                    isExpandedScreen = isExpandedScreen,
                    isMediumScreen = isMediumScreen,
                    onBlogClick = onBlogClick,
                    onRetryClick = onRetryClick,
                    onRefresh = onRefresh,
                )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteAppBar(
    modifier: Modifier = Modifier,
    showRefresh: Boolean = false,
    onRefresh: () -> Unit,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(stringResource(Res.string.favorite_screen_favorite_blogs_label))
        },
        actions = {
            if (showRefresh) {
                IconButton(
                    onClick = onRefresh,
                ) {
                    Icon(
                        painterResource(Res.drawable.ic_refresh),
                        contentDescription = null,
                    )
                }
            }
        },
    )
}
