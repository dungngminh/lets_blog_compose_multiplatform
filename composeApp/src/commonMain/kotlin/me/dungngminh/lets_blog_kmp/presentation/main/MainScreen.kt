package me.dungngminh.lets_blog_kmp.presentation.main

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.fab_create_blog_label
import letsblogkmp.composeapp.generated.resources.ic_favorite
import letsblogkmp.composeapp.generated.resources.ic_favorite_filled
import letsblogkmp.composeapp.generated.resources.ic_home
import letsblogkmp.composeapp.generated.resources.ic_home_filled
import letsblogkmp.composeapp.generated.resources.ic_pencil
import letsblogkmp.composeapp.generated.resources.ic_search
import letsblogkmp.composeapp.generated.resources.ic_search_filled
import letsblogkmp.composeapp.generated.resources.ic_user
import letsblogkmp.composeapp.generated.resources.ic_user_filled
import letsblogkmp.composeapp.generated.resources.nav_bar_favorite_label
import letsblogkmp.composeapp.generated.resources.nav_bar_home_label
import letsblogkmp.composeapp.generated.resources.nav_bar_profile_label
import letsblogkmp.composeapp.generated.resources.nav_bar_search_label
import me.dungngminh.lets_blog_kmp.commons.theme.LetsBlogAppTheme
import me.dungngminh.lets_blog_kmp.presentation.create_blog.CreateBlogScreen
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
        MainScreenContent(
            onFabClick = {
                navigator.push(CreateBlogScreen)
            },
        )
    }
}

@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    onFabClick: () -> Unit,
) {
    TabNavigator(HomeTab) {
        Scaffold(
            modifier = modifier,
            content = {
                CurrentTab()
            },
            floatingActionButton = {
                CreateBlogFabButton(onFabClick = onFabClick)
            },
            bottomBar = {
                MainNavigationBar()
            },
        )
    }
}

@Composable
private fun CreateBlogFabButton(
    modifier: Modifier = Modifier,
    onFabClick: () -> Unit,
) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        onClick = onFabClick,
    ) {
        Icon(
            painterResource(Res.drawable.ic_pencil),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
        )
        Spacer(Modifier.width(8.dp))
        Text(stringResource(Res.string.fab_create_blog_label))
    }
}

@Composable
private fun MainNavigationBar(modifier: Modifier = Modifier) {
    NavigationBar(modifier = modifier) {
        MainScreenDestination.entries
            .forEach {
                TabNavigationItem(tab = it.tab)
            }
    }
}

@Composable
private fun RowScope.TabNavigationItem(
    modifier: Modifier = Modifier,
    tab: Tab,
) {
    val tabNavigator = LocalTabNavigator.current
    val selected = tabNavigator.current == tab
    val tabOptions = tab.options
    NavigationBarItem(
        modifier = modifier,
        selected = selected,
        label = {
            Text(tabOptions.title)
        },
        onClick = { tabNavigator.current = tab },
        icon = {
            Icon(
                painter = painterResource(tab.icon(selected)),
                contentDescription = tabOptions.title,
                modifier = Modifier.size(24.dp),
            )
        },
    )
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
    }
}
