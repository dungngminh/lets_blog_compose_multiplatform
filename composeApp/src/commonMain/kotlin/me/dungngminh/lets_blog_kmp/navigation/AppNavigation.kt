package me.dungngminh.lets_blog_kmp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import me.dungngminh.lets_blog_kmp.presentation.splash.SplashScreen

@Serializable
object Splash

@Serializable
object Onboarding

@Serializable
object Main

@Serializable
object Home

@Serializable
object Search

@Serializable
object Favorite

@Serializable
object Profile

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Splash,
    ) {
        composable<Splash> {
            SplashScreen {
            }
        }
    }
}
