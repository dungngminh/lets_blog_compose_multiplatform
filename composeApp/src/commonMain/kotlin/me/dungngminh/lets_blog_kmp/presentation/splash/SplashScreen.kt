package me.dungngminh.lets_blog_kmp.presentation.splash

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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.aakira.napier.Napier
import io.github.alexzhirkevich.compottie.DotLottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.lets_blog_title
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onSplashDone: () -> Unit,
) {
    val currentOnSplashDone by rememberUpdatedState(onSplashDone)

    val lottieComposition by rememberLottieComposition {
        LottieCompositionSpec.DotLottie(
            Res.readBytes("files/splash.lottie"),
        )
    }

    val progress by animateLottieCompositionAsState(lottieComposition)

    val isSplashDone by derivedStateOf { progress >= 0.5f }

    LaunchedEffect(isSplashDone) {
        if (isSplashDone) {
            Napier.v("Checker")
            currentOnSplashDone()
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
                    painter =
                        rememberLottiePainter(composition = lottieComposition),
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
                        style = MaterialTheme.typography.h4,
                    )
                }
            }
        }
    }
}
