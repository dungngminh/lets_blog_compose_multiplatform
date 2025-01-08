package me.dungngminh.lets_blog_kmp.presentation.splash

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.alexzhirkevich.compottie.DotLottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.lets_blog_title
import me.dungngminh.lets_blog_kmp.AppViewModel
import me.dungngminh.lets_blog_kmp.presentation.main.MainScreen
import me.dungngminh.lets_blog_kmp.presentation.onboarding.OnboardingScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

class SplashScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val appViewModel = koinScreenModel<AppViewModel>()

        val state by appViewModel.state.collectAsStateWithLifecycle()

        SplashScreenContent(
            onSplashDone = {
                if (state.isOnboardingCompleted) {
                    navigator.replace(MainScreen())
                } else {
                    navigator.replace(OnboardingScreen())
                }
            },
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun SplashScreenContent(
    modifier: Modifier = Modifier,
    onSplashDone: () -> Unit,
) {
    val lottieComposition by rememberLottieComposition {
        LottieCompositionSpec.DotLottie(
            Res.readBytes("files/splash.lottie"),
        )
    }

    val progress by animateLottieCompositionAsState(lottieComposition)

    val isSplashDone by derivedStateOf { progress >= 0.5f }

    LaunchedEffect(isSplashDone) {
        if (isSplashDone) {
            onSplashDone()
        }
    }

    Scaffold(modifier = modifier) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column {
                Image(
                    painter = rememberLottiePainter(composition = lottieComposition),
                    contentDescription = "Splash animation",
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        stringResource(Res.string.lets_blog_title),
                        style = MaterialTheme.typography.headlineLarge,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreenContent(onSplashDone = {})
}
