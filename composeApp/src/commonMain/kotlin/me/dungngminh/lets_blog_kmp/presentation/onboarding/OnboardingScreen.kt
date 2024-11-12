package me.dungngminh.lets_blog_kmp.presentation.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onDoneClick: () -> Unit,
) {
    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
        ) {
        }
    }
}
