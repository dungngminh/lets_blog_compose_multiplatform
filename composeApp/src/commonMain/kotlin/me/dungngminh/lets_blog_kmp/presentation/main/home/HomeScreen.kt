package me.dungngminh.lets_blog_kmp.presentation.main.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import me.dungngminh.lets_blog_kmp.presentation.main.MainScreenDestination
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionState
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionViewModel
import org.jetbrains.compose.resources.stringResource

object HomeTab : Tab {
    @Composable
    override fun Content() {
        val parentNavigator = LocalNavigator.currentOrThrow.parent ?: return
        val userSessionViewModel = parentNavigator.koinNavigatorScreenModel<UserSessionViewModel>()
        val userSessionState by userSessionViewModel.userSessionState.collectAsStateWithLifecycle()
        HomeScreenContent(
            userSessionState = userSessionState,
        )
    }

    override val options: TabOptions
        @Composable
        get() =
            TabOptions(
                index =
                    MainScreenDestination.entries
                        .indexOf(MainScreenDestination.Home)
                        .toUShort(),
                title = stringResource(MainScreenDestination.Home.title),
            )
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    userSessionState: UserSessionState,
) {
    Scaffold(modifier = modifier) {
        Box(
            modifier =
                Modifier
                    .padding(it)
                    .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                when (userSessionState) {
                    is UserSessionState.Authenticated -> userSessionState.user.name
                    is UserSessionState.Error -> userSessionState.message
                    UserSessionState.Initial -> "Loading..."
                    UserSessionState.Unauthenticated -> "Unauthenticated"
                },
            )
        }
    }
}
