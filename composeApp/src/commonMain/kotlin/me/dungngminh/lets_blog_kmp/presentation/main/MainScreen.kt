package me.dungngminh.lets_blog_kmp.presentation.main

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.ic_favorite
import letsblogkmp.composeapp.generated.resources.ic_favorite_filled
import letsblogkmp.composeapp.generated.resources.ic_home
import letsblogkmp.composeapp.generated.resources.ic_home_filled
import letsblogkmp.composeapp.generated.resources.ic_search
import letsblogkmp.composeapp.generated.resources.ic_search_filled
import letsblogkmp.composeapp.generated.resources.ic_user
import letsblogkmp.composeapp.generated.resources.ic_user_filled
import letsblogkmp.composeapp.generated.resources.nav_bar_favorite_label
import letsblogkmp.composeapp.generated.resources.nav_bar_home_label
import letsblogkmp.composeapp.generated.resources.nav_bar_profile_label
import letsblogkmp.composeapp.generated.resources.nav_bar_search_label
import me.dungngminh.lets_blog_kmp.commons.theme.LetsBlogAppTheme
import me.dungngminh.lets_blog_kmp.presentation.main.MainScreenDestination.Favorite
import me.dungngminh.lets_blog_kmp.presentation.main.MainScreenDestination.Home
import me.dungngminh.lets_blog_kmp.presentation.main.MainScreenDestination.Profile
import me.dungngminh.lets_blog_kmp.presentation.main.MainScreenDestination.Search
import me.dungngminh.lets_blog_kmp.presentation.main.favorite.FavoriteTab
import me.dungngminh.lets_blog_kmp.presentation.main.home.HomeTab
import me.dungngminh.lets_blog_kmp.presentation.main.profile.ProfileTab
import me.dungngminh.lets_blog_kmp.presentation.main.search.SearchTab
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

enum class MainScreenDestination(
    val title: StringResource,
    val icon: DrawableResource,
    val selectedIcon: DrawableResource,
    val tab: Tab,
) {
    Home(
        title = Res.string.nav_bar_home_label,
        icon = Res.drawable.ic_home,
        selectedIcon = Res.drawable.ic_home_filled,
        tab = HomeTab,
    ),
    Search(
        title = Res.string.nav_bar_search_label,
        icon = Res.drawable.ic_search,
        selectedIcon = Res.drawable.ic_search_filled,
        tab = SearchTab,
    ),
    Favorite(
        title = Res.string.nav_bar_favorite_label,
        icon = Res.drawable.ic_favorite,
        selectedIcon = Res.drawable.ic_favorite_filled,
        tab = FavoriteTab,
    ),
    Profile(
        title = Res.string.nav_bar_profile_label,
        icon = Res.drawable.ic_user,
        selectedIcon = Res.drawable.ic_user_filled,
        tab = ProfileTab,
    ),
}

class MainScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val userSessionViewModel = navigator.koinNavigatorScreenModel<UserSessionViewModel>()
        val userSessionState by userSessionViewModel.userSessionState.collectAsStateWithLifecycle()
        MainScreenContent(
            userSessionState = userSessionState,
        )
    }
}

@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    userSessionState: UserSessionState,
) {
    TabNavigator(HomeTab) { tabNavigator ->
        NavigationSuiteScaffold(
            modifier = modifier,
            content = {
                CurrentTab()
            },
            navigationSuiteItems = {
                MainScreenDestination.entries
                    .forEach { destination ->
                        val tab = destination.tab
                        val selected = tabNavigator.current == tab
                        item(
                            modifier = modifier,
                            selected = selected,
                            label = {
                                Text(stringResource(destination.title))
                            },
                            onClick = { tabNavigator.current = tab },
                            icon = {
                                Icon(
                                    painter = painterResource(tab.icon(selected)),
                                    contentDescription = stringResource(destination.title),
                                    modifier = Modifier.size(24.dp),
                                )
                            },
                        )
                    }
            },
        )
    }
}

@Composable
private fun Tab.icon(isSelected: Boolean): DrawableResource =
    if (isSelected) {
        when (this) {
            HomeTab -> Home.selectedIcon
            SearchTab -> Search.selectedIcon
            FavoriteTab -> Favorite.selectedIcon
            is ProfileTab -> Profile.selectedIcon
            else -> Res.drawable.ic_home
        }
    } else {
        when (this) {
            HomeTab -> Home.icon
            SearchTab -> Search.icon
            FavoriteTab -> Favorite.icon
            is ProfileTab -> Profile.icon
            else -> Res.drawable.ic_home
        }
    }

@Preview
@Composable
fun MainScreenPreview() {
    LetsBlogAppTheme {
        MainScreenContent(userSessionState = UserSessionState.Unauthenticated)
    }
}
