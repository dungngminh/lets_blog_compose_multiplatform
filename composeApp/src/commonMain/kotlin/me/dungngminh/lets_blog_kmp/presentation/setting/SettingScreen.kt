package me.dungngminh.lets_blog_kmp.presentation.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.setting_screen_logout_label
import letsblogkmp.composeapp.generated.resources.setting_screen_setting_app_bar_title
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionState
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionViewModel
import org.jetbrains.compose.resources.stringResource

object SettingScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val userSessionViewModel = navigator.koinNavigatorScreenModel<UserSessionViewModel>()

        val userSessionState by userSessionViewModel.userSessionState.collectAsStateWithLifecycle()

        SettingContent(
            onBackClick = {
                navigator.pop()
            },
            userSessionState = userSessionState,
            onLogoutClick = userSessionViewModel::logout,
        )
    }
}

@Composable
fun SettingContent(
    modifier: Modifier = Modifier,
    userSessionState: UserSessionState,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            SettingAppBar(
                onBackClick = onBackClick,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            when (userSessionState) {
                is UserSessionState.Authenticated -> {
                    ListItem(
                        modifier =
                            Modifier.clickable {
                                onLogoutClick()
                            },
                        headlineContent = {
                            Text(stringResource(Res.string.setting_screen_logout_label))
                        },
                    )
                }

                else -> Unit
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingAppBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(stringResource(Res.string.setting_screen_setting_app_bar_title))
        },
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
    )
}
