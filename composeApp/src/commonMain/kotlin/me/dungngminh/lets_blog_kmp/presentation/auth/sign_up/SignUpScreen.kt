package me.dungngminh.lets_blog_kmp.presentation.auth.sign_up

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SignUpScreen(modifier: Modifier = Modifier) {
}

@Composable
fun SignUpScreenContent(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            SignUpTopBar(onBackClick = { })
        },
    ) {
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
            ) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        title = {
        },
    )
}
