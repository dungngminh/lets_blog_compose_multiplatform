package me.dungngminh.lets_blog_kmp.presentation.main.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import me.dungngminh.lets_blog_kmp.presentation.main.MainScreenDestination
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionState
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionViewModel
import me.dungngminh.lets_blog_kmp.presentation.sign_in.SignInScreen
import org.jetbrains.compose.resources.stringResource

object ProfileTab : Tab {
    @Composable
    override fun Content() {
        val parent = LocalNavigator.currentOrThrow.parent ?: return
        val userSessionViewModel = parent.koinNavigatorScreenModel<UserSessionViewModel>()
        val userSessionState by userSessionViewModel.userSessionState.collectAsStateWithLifecycle()
        ProfileScreenContent(
            userSessionState = userSessionState,
            onLogoutClick = userSessionViewModel::logout,
            onLoginClick = {
                parent.push(SignInScreen)
            },
            onRefreshClick = userSessionViewModel::refresh,
            onRetryClick = {
            },
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
    userSessionState: UserSessionState,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onRetryClick: () -> Unit,
) {
    Scaffold(modifier = modifier) {
        Box(
            modifier = Modifier.padding(it).fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                when (userSessionState) {
                    is UserSessionState.Authenticated -> {
                        Text("Welcome, ${userSessionState.user.name}")
                        Button(onClick = onLogoutClick) {
                            Text("Log out")
                        }
                        Button(onClick = onRefreshClick) {
                            Text("Refresh")
                        }
                    }

                    is UserSessionState.Initial -> {
                        Text("Loading...")
                    }

                    is UserSessionState.Error -> {
                        Text("Error: ${userSessionState.message}")
                        Button(onClick = onRetryClick) {
                            Text("Retry")
                        }
                    }

                    is UserSessionState.Unauthenticated -> {
                        Text("Please log in")
                        Button(onClick = onLoginClick) {
                            Text("Log in")
                        }
                    }
                }
            }
        }
    }
}
