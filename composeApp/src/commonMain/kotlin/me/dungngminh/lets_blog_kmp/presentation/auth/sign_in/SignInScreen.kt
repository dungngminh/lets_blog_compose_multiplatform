package me.dungngminh.lets_blog_kmp.presentation.auth.sign_in

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(modifier: Modifier = Modifier, onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                    ) {
                        Icon(Icons.Default.Done, contentDescription = null)
                    }
                },
                title = {

                }
            )
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

        }
    }
}

@Preview
@Composable
fun PreviewSignInScreen() {
    SignInScreen(onBackClick = {

    })
}