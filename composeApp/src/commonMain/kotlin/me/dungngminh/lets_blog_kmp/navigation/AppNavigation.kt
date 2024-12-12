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
    Home(title = Res.string.lets_blog_title, icon = Res.drawable, route = Route.Home),
    Search(title = Res.string.lets_blog_title, icon = Res.drawable.email, route = Route.Search),
    Favorite(title = Res.string.lets_blog_title, icon = Res.drawable.email, route = Route.Favorite),
    Profile(title = Res.string.lets_blog_title, icon = Res.drawable.email, route = Route.Profile),
}

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Route.Splash,
    ) {
        composable<Route.Splash> {
            SplashScreen {
                navController.navigate(Route.Onboarding)
            }
        }
        composable<Route.Onboarding> {
            OnboardingScreen {
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
    navigation<Auth>(startDestination = SignIn) {
        composable<SignIn> {
        }
        composable<SignUp> {
        }
    }
}
