package me.dungngminh.lets_blog_kmp.presentation.sign_up

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.ic_eye
import letsblogkmp.composeapp.generated.resources.ic_eye_closed
import letsblogkmp.composeapp.generated.resources.sign_in_page_email_hint_label
import letsblogkmp.composeapp.generated.resources.sign_up_page_already_have_account_label
import letsblogkmp.composeapp.generated.resources.sign_up_page_confirm_password_hint_label
import letsblogkmp.composeapp.generated.resources.sign_up_page_confirm_password_label
import letsblogkmp.composeapp.generated.resources.sign_up_page_email_hint_label
import letsblogkmp.composeapp.generated.resources.sign_up_page_email_label
import letsblogkmp.composeapp.generated.resources.sign_up_page_password_label
import letsblogkmp.composeapp.generated.resources.sign_up_page_sign_in_now_label
import letsblogkmp.composeapp.generated.resources.sign_up_page_sign_up_button_label
import letsblogkmp.composeapp.generated.resources.sign_up_page_sign_up_title
import letsblogkmp.composeapp.generated.resources.sign_up_page_username_hint_label
import letsblogkmp.composeapp.generated.resources.sign_up_page_username_label
import letsblogkmp.composeapp.generated.resources.validation_error_email_empty
import letsblogkmp.composeapp.generated.resources.validation_error_email_invalid
import letsblogkmp.composeapp.generated.resources.validation_error_password_empty
import letsblogkmp.composeapp.generated.resources.validation_error_password_too_short
import letsblogkmp.composeapp.generated.resources.validation_error_username_empty
import letsblogkmp.composeapp.generated.resources.validation_error_username_too_short
import me.dungngminh.lets_blog_kmp.commons.MIN_PASSWORD_LENGTH
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

object SignUpScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<SignUpViewModel>()

        val state by viewModel.state.collectAsStateWithLifecycle()

        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            viewModel.state.collect { state ->
                when {
                    state.error != null -> {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(state.error)
                            viewModel.onErrorShown()
                        }
                    }

                    state.isSignUpSuccess -> {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Sign up success")
                        }
                        navigator.pop()
                    }
                }
            }
        }

        SignUpScreenContent(
            onBackClick = navigator::pop,
            snackbarHostState = snackbarHostState,
            state = state,
            onUsernameChange = viewModel::changeUsername,
            onEmailChange = viewModel::changeEmail,
            onPasswordChange = viewModel::changePassword,
            onConfirmPasswordChange = viewModel::changeConfirmPassword,
            onConfirmPasswordVisibilityToggle = viewModel::toggleConfirmPasswordVisibility,
            onPasswordVisibilityToggle = viewModel::togglePasswordVisibility,
            onSignUpClick = viewModel::signUp,
            onSignInNowClick = navigator::pop,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SignUpScreenContent(
    modifier: Modifier = Modifier,
    state: SignUpState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onConfirmPasswordVisibilityToggle: () -> Unit,
    onSignUpClick: () -> Unit,
    onSignInNowClick: () -> Unit,
) {
    val sortKeyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val bringIntoViewRequest = remember { BringIntoViewRequester() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier,
        topBar = {
            SignUpTopBar(onBackClick = onBackClick)
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .imePadding()
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
        ) {
            Text(
                stringResource(Res.string.sign_up_page_sign_up_title),
                style = MaterialTheme.typography.displayLarge,
            )
            Spacer(modifier = Modifier.height(48.dp))
            OutlinedTextField(
                value = state.username,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .bringIntoView(bringIntoViewRequest),
                isError = isFormError(state.usernameError),
                supportingText = {
                    if (state.usernameError != SignUpValidationError.NONE) {
                        Text(
                            when (state.usernameError) {
                                SignUpValidationError.EMPTY_USERNAME -> stringResource(Res.string.validation_error_username_empty)
                                SignUpValidationError.USERNAME_TOO_SHORT ->
                                    stringResource(
                                        Res.string.validation_error_username_too_short,
                                    )

                                else -> ""
                            },
                        )
                    }
                },
                keyboardOptions =
                    KeyboardOptions(
                        imeAction = ImeAction.Next,
                    ),
                label = {
                    Text(stringResource(Res.string.sign_up_page_username_label))
                },
                placeholder = {
                    Text(stringResource(Res.string.sign_up_page_username_hint_label))
                },
                onValueChange = { onUsernameChange(it) },
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = state.email,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .bringIntoView(bringIntoViewRequest),
                isError = isFormError(state.emailError),
                supportingText = {
                    if (state.emailError != SignUpValidationError.NONE) {
                        Text(
                            when (state.emailError) {
                                SignUpValidationError.EMPTY_EMAIL -> stringResource(Res.string.validation_error_email_empty)
                                SignUpValidationError.INVALID_EMAIL -> stringResource(Res.string.validation_error_email_invalid)
                                else -> ""
                            },
                        )
                    }
                },
                keyboardOptions =
                    KeyboardOptions(
                        imeAction = ImeAction.Next,
                    ),
                label = {
                    Text(stringResource(Res.string.sign_up_page_email_label))
                },
                placeholder = {
                    Text(stringResource(Res.string.sign_in_page_email_hint_label))
                },
                onValueChange = { onEmailChange(it) },
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = state.password,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .bringIntoView(bringIntoViewRequest),
                isError = isFormError(state.passwordError),
                supportingText = {
                    if (state.passwordError != SignUpValidationError.NONE) {
                        Text(
                            when (state.passwordError) {
                                SignUpValidationError.EMPTY_PASSWORD -> stringResource(Res.string.validation_error_password_empty)
                                SignUpValidationError.PASSWORD_TOO_SHORT ->
                                    stringResource(
                                        Res.string.validation_error_password_too_short,
                                        MIN_PASSWORD_LENGTH,
                                    )

                                else -> ""
                            },
                        )
                    }
                },
                keyboardOptions =
                    KeyboardOptions(
                        imeAction = ImeAction.Next,
                    ),
                label = {
                    Text(stringResource(Res.string.sign_up_page_password_label))
                },
                placeholder = {
                    Text(stringResource(Res.string.sign_up_page_email_hint_label))
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

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = state.confirmPassword,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .bringIntoView(bringIntoViewRequest),
                isError = isFormError(state.confirmPasswordError),
                supportingText = {
                    if (state.confirmPasswordError != SignUpValidationError.NONE) {
                        Text(
                            when (state.confirmPasswordError) {
                                SignUpValidationError.PASSWORDS_NOT_MATCH ->
                                    stringResource(
                                        Res.string.validation_error_password_empty,
                                    )

                                SignUpValidationError.EMPTY_PASSWORD -> stringResource(Res.string.validation_error_password_empty)
                                else -> ""
                            },
                        )
                    }
                },
                keyboardActions =
                    KeyboardActions(onGo = {
                        if (state.isSignUpFormValid) {
                            onSignUpClick()
                        } else {
                            sortKeyboardController?.hide()
                        }
                    }),
                keyboardOptions =
                    KeyboardOptions(
                        imeAction = ImeAction.Go,
                    ),
                label = {
                    Text(stringResource(Res.string.sign_up_page_confirm_password_label))
                },
                placeholder = {
                    Text(stringResource(Res.string.sign_up_page_confirm_password_hint_label))
                },
                trailingIcon = {
                    IconButton(
                        onClick = { onConfirmPasswordVisibilityToggle() },
                    ) {
                        Icon(
                            painter =
                                if (state.confirmPasswordVisible) {
                                    painterResource(Res.drawable.ic_eye)
                                } else {
                                    painterResource(Res.drawable.ic_eye_closed)
                                },
                            contentDescription = null,
                        )
                    }
                },
                visualTransformation = if (state.confirmPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                onValueChange = { onConfirmPasswordChange(it) },
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
                        onClick = onSignUpClick,
                        enabled = state.isSignUpFormValid,
                    ) {
                        Text(stringResource(Res.string.sign_up_page_sign_up_button_label))
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
                    stringResource(Res.string.sign_up_page_already_have_account_label),
                    style = MaterialTheme.typography.bodyMedium,
                )
                TextButton(onClick = {
                    onSignInNowClick()
                }) {
                    Text(stringResource(Res.string.sign_up_page_sign_in_now_label))
                }
            }
        }
    }
}

fun isFormError(validationError: SignUpValidationError? = null): Boolean =
    when (validationError) {
        SignUpValidationError.NONE, null -> false
        else -> true
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

@Preview
@Composable
fun Preview_SignInScreen() {
    SignUpScreenContent(
        state = SignUpState(),
        snackbarHostState = SnackbarHostState(),
        onBackClick = {},
        onUsernameChange = {},
        onEmailChange = {},
        onPasswordChange = {},
        onConfirmPasswordChange = {},
        onPasswordVisibilityToggle = {},
        onConfirmPasswordVisibilityToggle = {},
        onSignUpClick = {},
        onSignInNowClick = {},
    )
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.bringIntoView(bringIntoViewRequester: BringIntoViewRequester): Modifier =
    composed {
        val coroutineScope = rememberCoroutineScope()
        this
            .bringIntoViewRequester(bringIntoViewRequester)
            .onFocusEvent {
                if (it.isFocused) {
                    coroutineScope.launch {
                        bringIntoViewRequester.bringIntoView()
                    }
                }
            }
    }
