package me.dungngminh.lets_blog_kmp

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import me.dungngminh.lets_blog_kmp.commons.theme.LetsBlogAppTheme
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
        LetsBlogAppTheme {
            val navController = rememberNavController()
            Scaffold(modifier = modifier) {
                AppNavigation(navController = navController)
            }
        }
    }
}
