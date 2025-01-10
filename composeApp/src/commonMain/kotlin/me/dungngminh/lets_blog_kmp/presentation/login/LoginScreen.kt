package me.dungngminh.lets_blog_kmp.presentation.login

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.ic_caret_left
import letsblogkmp.composeapp.generated.resources.ic_eye
import letsblogkmp.composeapp.generated.resources.ic_eye_closed
import letsblogkmp.composeapp.generated.resources.img_login
import letsblogkmp.composeapp.generated.resources.login_page_email_hint_label
import letsblogkmp.composeapp.generated.resources.login_page_email_label
import letsblogkmp.composeapp.generated.resources.login_page_login_button_label
import letsblogkmp.composeapp.generated.resources.login_page_login_title
import letsblogkmp.composeapp.generated.resources.login_page_new_in_lets_blog_label
import letsblogkmp.composeapp.generated.resources.login_page_password_hint_label
import letsblogkmp.composeapp.generated.resources.login_page_password_label
import letsblogkmp.composeapp.generated.resources.login_page_register_now_label
import letsblogkmp.composeapp.generated.resources.validation_error_email_empty
import letsblogkmp.composeapp.generated.resources.validation_error_email_invalid
import letsblogkmp.composeapp.generated.resources.validation_error_password_empty
import letsblogkmp.composeapp.generated.resources.validation_error_password_too_short
import me.dungngminh.lets_blog_kmp.LocalWindowSizeClass
import me.dungngminh.lets_blog_kmp.commons.MIN_PASSWORD_LENGTH
import me.dungngminh.lets_blog_kmp.commons.extensions.tabsVisualTransformation
import me.dungngminh.lets_blog_kmp.presentation.register.RegisterScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

object LoginScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val snackbarHostState = remember { SnackbarHostState() }
        val sortKeyboardController = LocalSoftwareKeyboardController.current
        val viewModel = koinScreenModel<LoginViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.state.collect { state ->
                when {
                    state.isLoginSuccess -> {
                        navigator?.pop()
                    }

                    state.error != null -> {
                        launch {
                            snackbarHostState.showSnackbar(state.error)
                        }
                        viewModel.onErrorMessageShown()
                    }
                }
            }
        }

        LoginScreenContent(
            state = state,
            snackbarHostState = snackbarHostState,
            onBackClick = {
                navigator?.pop()
            },
            onEmailChange = viewModel::changeEmail,
            onPasswordChange = viewModel::changePassword,
            onSignUpClick = {
                navigator?.push(RegisterScreen)
            },
            onPasswordVisibilityToggle = viewModel::togglePasswordVisibility,
            onLoginClick = {
                viewModel.signIn()
                sortKeyboardController?.hide()
            },
        )
    }
}

@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    state: LoginState,
    onBackClick: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onLoginClick: () -> Unit,
) {
    val windowSizeClass = LocalWindowSizeClass.currentOrThrow

    val isExpandedScreen = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            LoginTopBar(onBackClick = onBackClick)
        },
        modifier = modifier,
    ) { innerPadding ->
        if (isExpandedScreen) {
            Row(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Image(
                    painterResource(Res.drawable.img_login),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .weight(0.55f)
                            .height(500.dp),
                )
                LoginForm(
                    modifier =
                        Modifier
                            .weight(0.45f),
                    state = state,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                    onSignUpClick = onSignUpClick,
                    onPasswordVisibilityToggle = onPasswordVisibilityToggle,
                    onLoginClick = onLoginClick,
                )
            }
        } else {
            LoginForm(
                modifier = Modifier.padding(innerPadding),
                state = state,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onSignUpClick = onSignUpClick,
                onPasswordVisibilityToggle = onPasswordVisibilityToggle,
                onLoginClick = onLoginClick,
            )
        }
    }
}

@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    state: LoginState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onLoginClick: () -> Unit,
) {
    Column(
        modifier =
            modifier
                .padding(16.dp),
    ) {
        Text(
            stringResource(Res.string.login_page_login_title),
            style = MaterialTheme.typography.displayLarge,
        )
        Spacer(modifier = Modifier.height(48.dp))
        OutlinedTextField(
            value = state.email,
            modifier = Modifier.fillMaxWidth(),
            isError =
                when (state.emailError) {
                    null, LoginValidationError.NONE -> false
                    else -> true
                },
            keyboardOptions =
                KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
            supportingText = {
                if (state.emailError != LoginValidationError.NONE) {
                    Text(
                        when (state.emailError) {
                            LoginValidationError.EMPTY_EMAIL -> stringResource(Res.string.validation_error_email_empty)
                            LoginValidationError.INVALID_EMAIL -> stringResource(Res.string.validation_error_email_invalid)
                            else -> ""
                        },
                    )
                }
            },
            label = {
                Text(stringResource(Res.string.login_page_email_label))
            },
            placeholder = {
                Text(stringResource(Res.string.login_page_email_hint_label))
            },
            onValueChange = { onEmailChange(it) },
            visualTransformation = tabsVisualTransformation,
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = state.password,
            modifier = Modifier.fillMaxWidth(),
            isError =
                when (state.passwordError) {
                    null, LoginValidationError.NONE -> false
                    else -> true
                },
            keyboardActions =
                KeyboardActions(onGo = {
                    if (state.isLoginFormValid) {
                        onLoginClick()
                    }
                }),
            keyboardOptions =
                KeyboardOptions(
                    imeAction = ImeAction.Go,
                ),
            supportingText = {
                if (state.passwordError != LoginValidationError.NONE) {
                    Text(
                        when (state.passwordError) {
                            LoginValidationError.EMPTY_PASSWORD -> stringResource(Res.string.validation_error_password_empty)
                            LoginValidationError.PASSWORD_TOO_SHORT ->
                                stringResource(
                                    Res.string.validation_error_password_too_short,
                                    MIN_PASSWORD_LENGTH,
                                )

                            else -> ""
                        },
                    )
                }
            },
            label = {
                Text(stringResource(Res.string.login_page_password_label))
            },
            placeholder = {
                Text(stringResource(Res.string.login_page_password_hint_label))
            },
            trailingIcon = {
                IconButton(
                    onClick = { onPasswordVisibilityToggle() },
                ) {
                    Icon(
                        painter =
                            if (state.passwordVisible) {
                                painterResource(Res.drawable.ic_eye)
                            } else {
                                painterResource(Res.drawable.ic_eye_closed)
                            },
                        contentDescription = null,
                    )
                }
            },
            visualTransformation = if (state.passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            onValueChange = { onPasswordChange(it) },
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = onLoginClick,
                    enabled = state.isLoginFormValid,
                ) {
                    Text(stringResource(Res.string.login_page_login_button_label))
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(Res.string.login_page_new_in_lets_blog_label),
                style = MaterialTheme.typography.bodyMedium,
            )
            TextButton(onClick = onSignUpClick) {
                Text(stringResource(Res.string.login_page_register_now_label))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
            ) {
                Icon(
                    painterResource(Res.drawable.ic_caret_left),
                    contentDescription = null,
                )
            }
        },
        title = {
        },
    )
}

@Preview
@Composable
fun Preview_LoginScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    LoginScreenContent(
        onBackClick = {
        },
        onSignUpClick = {
        },
        state =
            LoginState(
                isLoading = false,
                isLoginFormValid = true,
                email = "ngminhdung1311@gmail.com",
            ),
        onEmailChange = {
        },
        onPasswordChange = {
        },
        onPasswordVisibilityToggle = {
        },
        onLoginClick = {},
        snackbarHostState = snackbarHostState,
    )
}
