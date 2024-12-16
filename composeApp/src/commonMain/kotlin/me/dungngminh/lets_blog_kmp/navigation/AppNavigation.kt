package me.dungngminh.lets_blog_kmp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.ic_favorite
import letsblogkmp.composeapp.generated.resources.ic_favorite_filled
import letsblogkmp.composeapp.generated.resources.ic_home
import letsblogkmp.composeapp.generated.resources.ic_home_filled
import letsblogkmp.composeapp.generated.resources.ic_search
import letsblogkmp.composeapp.generated.resources.ic_search_filled
import letsblogkmp.composeapp.generated.resources.ic_user
import letsblogkmp.composeapp.generated.resources.ic_user_filled
import letsblogkmp.composeapp.generated.resources.lets_blog_title
import me.dungngminh.lets_blog_kmp.presentation.main.favorite.FavoriteScreen
import me.dungngminh.lets_blog_kmp.presentation.main.home.HomeScreen
import me.dungngminh.lets_blog_kmp.presentation.main.profile.ProfileScreen
import me.dungngminh.lets_blog_kmp.presentation.main.search.SearchScreen
import me.dungngminh.lets_blog_kmp.presentation.onboarding.OnboardingScreen
import me.dungngminh.lets_blog_kmp.presentation.splash.SplashScreen
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

sealed interface Route {
    @Serializable
    data object Splash : Route

    @Serializable
    data object Onboarding : Route

    @Serializable
    data object Main : Route

    @Serializable
    data object Home : Route

    @Serializable
    data object Search : Route

    @Serializable
    data object Favorite : Route

    @Serializable
    data object Profile : Route

    @Serializable
    data object Auth : Route

    @Serializable
    data object SignIn : Route

    @Serializable
    data object SignUp : Route
}

enum class MainScreenDestination(
    val title: StringResource,
    val icon: DrawableResource,
    val selectedIcon: DrawableResource,
    val route: Route,
) {
    Home(
        title = Res.string.lets_blog_title,
        icon = Res.drawable.ic_home,
        selectedIcon = Res.drawable.ic_home_filled,
        route = Route.Home,
    ),
    Search(
        title = Res.string.lets_blog_title,
        icon = Res.drawable.ic_search,
        selectedIcon = Res.drawable.ic_search_filled,
        route = Route.Search,
    ),
    Favorite(
        title = Res.string.lets_blog_title,
        icon = Res.drawable.ic_favorite,
        selectedIcon = Res.drawable.ic_favorite_filled,
        route = Route.Favorite,
    ),
    Profile(
        title = Res.string.lets_blog_title,
        icon = Res.drawable.ic_user,
        selectedIcon = Res.drawable.ic_user_filled,
        route = Route.Profile,
    ),
}

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Route.Splash,
    ) {
        composable<Route.Splash> {
            SplashScreen {
                navController.navigate(Route.Onboarding) {
                    popUpTo<Route.Splash> {
                        inclusive = true
                    }
                }
            }
        }
        composable<Route.Onboarding> {
            OnboardingScreen {
                navController.navigate(Route.Main) {
                    popUpTo<Route.Onboarding> {
                        inclusive = true
                    }
                }
            }
        }
        authGraph()
        mainGraph()
    }
}

fun NavGraphBuilder.mainGraph() {
    navigation<Route.Main>(startDestination = Route.Home) {
        composable<Route.Home> {
            HomeScreen()
        }
        composable<Route.Search> {
            SearchScreen()
        }
        composable<Route.Favorite> {
            FavoriteScreen()
        }
        composable<Route.Profile> {
            ProfileScreen()
        }
    }
}

fun NavGraphBuilder.authGraph() {
    navigation<Route.Auth>(startDestination = Route.SignIn) {
        composable<Route.SignIn> {
        }
        composable<Route.SignUp> {
        }
    }
}
