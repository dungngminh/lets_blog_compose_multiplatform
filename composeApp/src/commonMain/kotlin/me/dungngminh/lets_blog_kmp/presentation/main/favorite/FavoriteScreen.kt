package me.dungngminh.lets_blog_kmp.presentation.main.favorite

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.presentation.detail_blog.DetailBlogScreen
import me.dungngminh.lets_blog_kmp.presentation.main.MainScreenDestination
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionState
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionViewModel
import me.dungngminh.lets_blog_kmp.presentation.main.favorite.components.AuthenticatedFavoriteContent
import org.jetbrains.compose.resources.stringResource

object FavoriteTab : Tab {
    @Composable
    override fun Content() {
        val parent = LocalNavigator.currentOrThrow.parent ?: return

        val userSessionViewModel = parent.koinNavigatorScreenModel<UserSessionViewModel>()

        val favoriteViewModel = koinScreenModel<FavoriteViewModel>()

        val userSessionState by userSessionViewModel.userSessionState.collectAsStateWithLifecycle()

        val favoriteUiState by favoriteViewModel.uiState.collectAsStateWithLifecycle()

        FavoriteScreenContent(
            favoriteUiState = favoriteUiState,
            userSessionState = userSessionState,
            onBlogClick = {
                parent.push(DetailBlogScreen(it))
            },
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
    onBlogClick: (Blog) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            FavoriteAppBar()
        },
    ) { innerPadding ->
        when (userSessionState) {
            is UserSessionState.Authenticated ->
                AuthenticatedFavoriteContent(
                    modifier =
                        Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                    favoriteUiState = favoriteUiState,
                    onBlogClick = onBlogClick,
                    user = userSessionState.user,
                )

            else -> Unit
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(stringResource(Res.string.favorite_screen_favorite_blogs_label))
        },
    )
}
