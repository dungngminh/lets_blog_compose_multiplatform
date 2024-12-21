package me.dungngminh.lets_blog_kmp.presentation.create_blog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

object CreateBlogScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        CreateBlogScreen(
            onBackClick = {
                navigator.pop()
            },
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CreateBlogScreen(
        modifier: Modifier = Modifier,
        onBackClick: () -> Unit,
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = onBackClick,
                        ) {
                            Icon(
                                Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "create_post_back_button",
                            )
                        }
                    },
                    title = {
                    },
                )
            },
        ) {
        }
    }
}
