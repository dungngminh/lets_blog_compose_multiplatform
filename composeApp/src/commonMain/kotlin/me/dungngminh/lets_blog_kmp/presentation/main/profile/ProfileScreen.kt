package me.dungngminh.lets_blog_kmp.presentation.main.profile

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.skydoves.landscapist.coil3.CoilImage
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.ic_setting
import me.dungngminh.lets_blog_kmp.domain.entities.User
import me.dungngminh.lets_blog_kmp.presentation.components.Center
import me.dungngminh.lets_blog_kmp.presentation.main.MainScreenDestination
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionState
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionViewModel
import me.dungngminh.lets_blog_kmp.presentation.setting.SettingScreen
import me.dungngminh.lets_blog_kmp.presentation.sign_in.SignInScreen
import org.jetbrains.compose.resources.painterResource
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
            onSettingClick = {
                parent.push(SettingScreen)
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
    onSettingClick: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            ProfileAppBar {
                onSettingClick()
            }
        },
    ) { innerPadding ->
        when (userSessionState) {
            UserSessionState.Unauthenticated -> TODO()
            is UserSessionState.Error -> TODO()
            UserSessionState.Initial -> TODO()
            is UserSessionState.Authenticated -> {
                if (userSessionState.user == null) {
                    Center {
                        CircularProgressIndicator()
                    }
                } else {
                    AuthenticatedProfileScreen(
                        modifier =
                            Modifier
                                .padding(innerPadding)
                                .fillMaxSize(),
                        user = userSessionState.user,
                    )
                }
            }
        }
    }
}

@Composable
fun AuthenticatedProfileScreen(
    modifier: Modifier = Modifier,
    user: User,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            ProfileInfo(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                user = user,
            )
        }
    }
}

@Composable
fun ProfileInfo(
    modifier: Modifier = Modifier,
    user: User,
) {
    Row(modifier = modifier) {
        Box(
            modifier =
                Modifier
                    .size(56.dp)
                    .clip(CircleShape),
        ) {
            CoilImage(
                imageModel = { user.avatarUrl },
                modifier = Modifier.fillMaxSize(),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(user.name)
            Text(user.email)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileAppBar(
    modifier: Modifier = Modifier,
    onSettingClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {},
        actions = {
            IconButton(
                onClick = onSettingClick,
            ) {
                Icon(
                    painterResource(Res.drawable.ic_setting),
                    contentDescription = null,
                )
            }
        },
    )
}

@Preview
@Composable
fun Preview_AuthenticatedProfileScreen() {
    MaterialTheme {
        AuthenticatedProfileScreen(
            user =
                User(
                    id = "at",
                    name = "Glenda Finley",
                    email = "mabel.navarro@example.com",
                    avatarUrl = null,
                    follower = 3445,
                    following = 9635,
                ),
        )
    }
}
