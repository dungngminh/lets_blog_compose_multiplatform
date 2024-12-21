package me.dungngminh.lets_blog_kmp.presentation.main.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import me.dungngminh.lets_blog_kmp.presentation.main.MainScreenDestination
import org.jetbrains.compose.resources.stringResource

data class ProfileTab(
    val onLoginClick: () -> Unit,
) : Tab {
    @Composable
    override fun Content() {
        ProfileScreenContent(
            onLoginClick = onLoginClick,
        )
    }

    override val options: TabOptions
        @Composable
        get() =
            TabOptions(
                index =
                    MainScreenDestination.entries
                        .indexOf(MainScreenDestination.Profile)
                        .toUShort(),
                title = stringResource(MainScreenDestination.Profile.title),
            )
}

@Composable
private fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
) {
    Scaffold(modifier = modifier) {
        Box(
            modifier = Modifier.padding(it).fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Button(onClick = onLoginClick) {
                Text("Login in")
            }
        }
    }
}
