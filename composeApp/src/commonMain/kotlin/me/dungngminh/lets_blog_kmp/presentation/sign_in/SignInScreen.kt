package me.dungngminh.lets_blog_kmp.presentation.sign_in

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.ic_eye
import letsblogkmp.composeapp.generated.resources.ic_eye_closed
import letsblogkmp.composeapp.generated.resources.sign_in_page_email_hint_label
import letsblogkmp.composeapp.generated.resources.sign_in_page_email_label
import letsblogkmp.composeapp.generated.resources.sign_in_page_new_in_lets_blog_label
import letsblogkmp.composeapp.generated.resources.sign_in_page_password_label
import letsblogkmp.composeapp.generated.resources.sign_in_page_sign_in_button_label
import letsblogkmp.composeapp.generated.resources.sign_in_page_sign_in_title
import letsblogkmp.composeapp.generated.resources.sign_in_page_sign_up_now_label
import letsblogkmp.composeapp.generated.resources.validation_error_email_empty
import letsblogkmp.composeapp.generated.resources.validation_error_email_invalid
import letsblogkmp.composeapp.generated.resources.validation_error_password_empty
import letsblogkmp.composeapp.generated.resources.validation_error_password_too_short
import me.dungngminh.lets_blog_kmp.commons.MIN_PASSWORD_LENGTH
import me.dungngminh.lets_blog_kmp.presentation.sign_up.SignUpScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

object SignInScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel { SignInViewModel() }

        val state by viewModel.state.collectAsStateWithLifecycle()
        SignInScreenContent(
            state = state,
            onBackClick = {
                navigator.pop()
            },
            onEmailChange = viewModel::changeEmail,
            onPasswordChange = viewModel::changePassword,
            onSignUpClick = {
                navigator.push(SignUpScreen)
            },
            onPasswordVisibilityToggle = viewModel::togglePasswordVisibility,
            onSignInClick = viewModel::signIn,
        )
    }
}

@Composable
fun SignInScreenContent(
    modifier: Modifier = Modifier,
    state: SignInState,
    onBackClick: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onSignInClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            SignInTopBar(
                onBackClick = onBackClick,
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                stringResource(Res.string.sign_in_page_sign_in_title),
                style = MaterialTheme.typography.displayLarge,
            )
            Spacer(modifier = Modifier.height(48.dp))
            OutlinedTextField(
                value = state.email,
                modifier = Modifier.fillMaxWidth(),
                isError = state.emailError != SignInValidationError.NONE,
                supportingText = {
                    if (state.emailError != SignInValidationError.NONE) {
                        Text(
                            when (state.emailError) {
                                SignInValidationError.EMPTY_EMAIL -> stringResource(Res.string.validation_error_email_empty)
                                SignInValidationError.INVALID_EMAIL -> stringResource(Res.string.validation_error_email_invalid)
                                else -> ""
                            },
                        )
                    }
                },
                label = {
                    Text(stringResource(Res.string.sign_in_page_email_label))
                },
                placeholder = {
                    Text(stringResource(Res.string.sign_in_page_email_hint_label))
                },
                onValueChange = { onEmailChange(it) },
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = state.password,
                modifier = Modifier.fillMaxWidth(),
                isError = state.passwordError != SignInValidationError.NONE,
                supportingText = {
                    if (state.passwordError != SignInValidationError.NONE) {
                        Text(
                            when (state.passwordError) {
                                SignInValidationError.EMPTY_PASSWORD -> stringResource(Res.string.validation_error_password_empty)
                                SignInValidationError.PASSWORD_TOO_SHORT ->
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
                    Text(stringResource(Res.string.sign_in_page_password_label))
                },
                placeholder = {
                    Text(stringResource(Res.string.sign_in_page_email_hint_label))
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
                        onClick = onSignInClick,
                        enabled = state.isSignInFormValid,
                    ) {
                        Text(stringResource(Res.string.sign_in_page_sign_in_button_label))
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
                    stringResource(Res.string.sign_in_page_new_in_lets_blog_label),
                    style = MaterialTheme.typography.bodyMedium,
                )
                TextButton(onClick = onSignUpClick) {
                    Text(stringResource(Res.string.sign_in_page_sign_up_now_label))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInTopBar(onBackClick: () -> Unit) {
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

@Preview
@Composable
fun PreviewSignInScreen() {
    SignInScreenContent(
        onBackClick = {
        },
        onSignUpClick = {
        },
        state =
            SignInState(
                isLoading = false,
                isSignInFormValid = true,
                email = "ngminhdung1311@gmail.com",
            ),
        onEmailChange = {
        },
        onPasswordChange = {
        },
        onPasswordVisibilityToggle = {
        },
        onSignInClick = {},
    )
}
