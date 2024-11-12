package me.dungngminh.lets_blog_kmp

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import me.dungngminh.lets_blog_kmp.di.AppModule
import me.dungngminh.lets_blog_kmp.navigation.AppNavigation
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App(modifier: Modifier = Modifier) {
    KoinApplication(
        application = {
            modules(AppModule)
        },
    ) {
        MaterialTheme {
            val navController = rememberNavController()
            Scaffold(modifier = modifier) {
                AppNavigation(navController = navController)
            }
        }
    }
}
