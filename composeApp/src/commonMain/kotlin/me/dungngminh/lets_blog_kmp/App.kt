package me.dungngminh.lets_blog_kmp

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScaleTransition
import me.dungngminh.lets_blog_kmp.commons.theme.LetsBlogAppTheme
import me.dungngminh.lets_blog_kmp.di.AppModule
import me.dungngminh.lets_blog_kmp.presentation.splash.SplashScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    KoinApplication(
        application = {
            modules(AppModule)
        },
    ) {
        LetsBlogAppTheme {
            Navigator(SplashScreen()) { navigator ->
                ScaleTransition(navigator)
            }
        }
    }
}
