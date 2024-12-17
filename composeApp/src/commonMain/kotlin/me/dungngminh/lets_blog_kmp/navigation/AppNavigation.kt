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
import letsblogkmp.composeapp.generated.resources.nav_bar_favorite_label
import letsblogkmp.composeapp.generated.resources.nav_bar_home_label
import letsblogkmp.composeapp.generated.resources.nav_bar_profile_label
import letsblogkmp.composeapp.generated.resources.nav_bar_search_label
import me.dungngminh.lets_blog_kmp.presentation.auth.sign_in.SignInScreen
import me.dungngminh.lets_blog_kmp.presentation.main.MainScreen
import me.dungngminh.lets_blog_kmp.presentation.onboarding.OnboardingScreen
import me.dungngminh.lets_blog_kmp.presentation.splash.SplashScreen
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

interface AppRoute

@Serializable
object SplashRoute : AppRoute

@Serializable
object OnboardingRoute : AppRoute

@Serializable
object MainRoute : AppRoute

@Serializable
object HomeRoute : AppRoute

@Serializable
object SearchRoute : AppRoute

@Serializable
object FavoriteRoute : AppRoute

@Serializable
object ProfileRoute : AppRoute

@Serializable
object AuthRoute : AppRoute

@Serializable
object SignInRoute : AppRoute

@Serializable
object SignUpRoute : AppRoute

enum class MainScreenDestination(
    val title: StringResource,
    val icon: DrawableResource,
    val selectedIcon: DrawableResource,
    val route: AppRoute,
) {
    Home(
        title = Res.string.nav_bar_home_label,
        icon = Res.drawable.ic_home,
        selectedIcon = Res.drawable.ic_home_filled,
        route = HomeRoute,
    ),
    Search(
        title = Res.string.nav_bar_search_label,
        icon = Res.drawable.ic_search,
        selectedIcon = Res.drawable.ic_search_filled,
        route = SearchRoute,
    ),
    Favorite(
        title = Res.string.nav_bar_favorite_label,
        icon = Res.drawable.ic_favorite,
        selectedIcon = Res.drawable.ic_favorite_filled,
        route = FavoriteRoute,
    ),
    Profile(
        title = Res.string.nav_bar_profile_label,
        icon = Res.drawable.ic_user,
        selectedIcon = Res.drawable.ic_user_filled,
        route = ProfileRoute,
    ),
}

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = SplashRoute,
    ) {
        composable<SplashRoute> {
            SplashScreen {
                navController.navigate(OnboardingRoute) {
                    popUpTo<SplashRoute> {
                        inclusive = true
                    }
                }
            }
        }
        authGraph(navController)
        composable<OnboardingRoute> {
            OnboardingScreen {
                navController.navigate(MainRoute) {
                    popUpTo<OnboardingRoute> {
                        inclusive = true
                    }
                }
            }
        }
        composable<MainRoute> {
            MainScreen(
                onLoginClick = {
                    navController.navigate(AuthRoute)
                }
            )
        }
    }
}

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation<AuthRoute>(startDestination = SignInRoute) {
        composable<SignInRoute> {
            SignInScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable<SignUpRoute> {
        }
    }
}
