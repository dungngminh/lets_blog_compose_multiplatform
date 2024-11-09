package me.dungngminh.lets_blog_kmp.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onSplashDone: () -> Unit,
) {
    Scaffold(modifier = modifier) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column {
                Text("Hello Compose Multiplatform")

                TextButton(
                    onClick = onSplashDone,
                ) {
                    Text("Navigate to main")
                }
            }
        }
    }
}
