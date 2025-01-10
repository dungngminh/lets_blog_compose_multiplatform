package me.dungngminh.lets_blog_kmp.presentation.edit_user_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.skydoves.landscapist.coil3.CoilImage
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerType
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.edit_profile_screen_edit_profile_title
import letsblogkmp.composeapp.generated.resources.edit_user_profile_success
import letsblogkmp.composeapp.generated.resources.general_something_went_wrong_please_try_again
import letsblogkmp.composeapp.generated.resources.ic_camera
import letsblogkmp.composeapp.generated.resources.ic_caret_left
import letsblogkmp.composeapp.generated.resources.ic_disk
import letsblogkmp.composeapp.generated.resources.img_placeholder
import letsblogkmp.composeapp.generated.resources.platform_image_picker_title
import letsblogkmp.composeapp.generated.resources.register_page_username_hint_label
import letsblogkmp.composeapp.generated.resources.register_page_username_label
import letsblogkmp.composeapp.generated.resources.validation_error_username_empty
import letsblogkmp.composeapp.generated.resources.validation_error_username_too_short
import me.dungngminh.lets_blog_kmp.LocalWindowSizeClass
import me.dungngminh.lets_blog_kmp.commons.extensions.tabsVisualTransformation
import me.dungngminh.lets_blog_kmp.commons.extensions.toByteArray
import me.dungngminh.lets_blog_kmp.domain.entities.User
import me.dungngminh.lets_blog_kmp.presentation.components.LoadingDialog
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionState
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionViewModel
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf

object EditUserProfileScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val userSessionViewModel = navigator.koinNavigatorScreenModel<UserSessionViewModel>()
        val userSessionState by userSessionViewModel.userSessionState.collectAsStateWithLifecycle()

        val user =
            remember {
                (userSessionState as? UserSessionState.Authenticated)?.user
            }

        LaunchedEffect(userSessionState) {
            if (user == null) {
                navigator.pop()
            }
        }

        val editUserProfileViewModel =
            koinScreenModel<EditUserProfileViewModel> { parametersOf(user) }
        val editUserProfileState by editUserProfileViewModel.uiState.collectAsStateWithLifecycle()

        val imagePickerLauncher =
            rememberFilePickerLauncher(
                type = PickerType.Image,
                title = stringResource(Res.string.platform_image_picker_title),
            ) { file ->
                editUserProfileViewModel.changeImageFile(imageFile = file)
            }

        val snackbarHostState = remember { SnackbarHostState() }

        val coroutineScope = rememberCoroutineScope()

        val currentFocusManager = LocalFocusManager.current

        val windowSizeClass = LocalWindowSizeClass.currentOrThrow

        if (editUserProfileState.isLoading) {
            LoadingDialog()
        }

        LaunchedEffect(Unit) {
            editUserProfileViewModel
                .uiState
                .onEach { state ->
                    when {
                        state.errorMessage != null -> {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(getString(Res.string.general_something_went_wrong_please_try_again))
                                editUserProfileViewModel.onErrorShown()
                            }
                        }

                        state.isEditUserProfileSuccess -> {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(getString(Res.string.edit_user_profile_success))
                                userSessionViewModel.refresh()
                                navigator.pop()
                            }
                        }
                    }
                }.launchIn(this)
        }

        EditUserProfileContent(
            user = user!!,
            snackbarHostState = snackbarHostState,
            editUserProfileState = editUserProfileState,
            isMediumScreen = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium,
            isExpandedScreen = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded,
            onBackClick = {
                navigator.pop()
            },
            onCameraClick = {
                imagePickerLauncher.launch()
            },
            onSaveClick = {
                currentFocusManager.clearFocus()
                editUserProfileViewModel.saveProfile()
            },
            onUsernameChange = editUserProfileViewModel::changeUsername,
        )
    }
}

@Composable
fun EditUserProfileContent(
    modifier: Modifier = Modifier,
    user: User,
    snackbarHostState: SnackbarHostState,
    editUserProfileState: EditUserProfileState,
    isMediumScreen: Boolean = false,
    isExpandedScreen: Boolean = false,
    onBackClick: () -> Unit,
    onCameraClick: () -> Unit,
    onUsernameChange: (String) -> Unit,
    onSaveClick: () -> Unit,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier,
        topBar = {
            EditUserProfileTopBar(
                onBackClick = onBackClick,
                enableSaveButton = editUserProfileState.isEditUserProfileFormValid,
                onSaveClick = onSaveClick,
            )
        },
    ) { innerPadding ->
        val spaceWeight =
            remember(isMediumScreen, isExpandedScreen) {
                when {
                    isExpandedScreen -> 0.25f
                    isMediumScreen -> 0.2f
                    else -> 0.05f
                }
            }
        val contentWeight =
            remember(isMediumScreen, isExpandedScreen) {
                when {
                    isExpandedScreen -> 0.5f
                    isMediumScreen -> 0.6f
                    else -> 0.9f
                }
            }
        Row {
            Spacer(modifier = Modifier.weight(spaceWeight))
            Column(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .windowInsetsPadding(WindowInsets.ime)
                        .weight(contentWeight),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(80.dp))
                AvatarBox(
                    localImageFile = editUserProfileState.selectedImagePath,
                    avatarUrl = user.avatarUrl,
                    onCameraClick = onCameraClick,
                )
                Spacer(modifier = Modifier.height(42.dp))
                UserNameTextField(
                    userName = editUserProfileState.userName,
                    onUsernameChange = onUsernameChange,
                    userProfileValidationError = editUserProfileState.userNameError,
                )
            }
            Spacer(modifier = Modifier.weight(spaceWeight))
        }
    }
}

@Composable
fun UserNameTextField(
    modifier: Modifier = Modifier,
    userName: String,
    onUsernameChange: (String) -> Unit,
    userProfileValidationError: EditUserProfileValidationError? = null,
) {
    OutlinedTextField(
        value = userName,
        modifier =
            modifier
                .fillMaxWidth(),
        isError =
            when (userProfileValidationError) {
                EditUserProfileValidationError.NONE, null -> false
                else -> true
            },
        supportingText = {
            if (userProfileValidationError != EditUserProfileValidationError.NONE) {
                Text(
                    when (userProfileValidationError) {
                        EditUserProfileValidationError.EMPTY_USERNAME -> stringResource(Res.string.validation_error_username_empty)
                        EditUserProfileValidationError.USERNAME_TOO_SHORT ->
                            stringResource(
                                Res.string.validation_error_username_too_short,
                            )

                        else -> ""
                    },
                )
            }
        },
        keyboardOptions =
            KeyboardOptions(imeAction = ImeAction.Done),
        label = {
            Text(stringResource(Res.string.register_page_username_label))
        },
        placeholder = {
            Text(stringResource(Res.string.register_page_username_hint_label))
        },
        onValueChange = { onUsernameChange(it) },
        visualTransformation = tabsVisualTransformation,
    )
}

@Composable
fun AvatarBox(
    modifier: Modifier = Modifier,
    localImageFile: PlatformFile? = null,
    avatarUrl: String? = null,
    onCameraClick: () -> Unit = {},
) {
    var imageBytes by remember(localImageFile) {
        mutableStateOf<ByteArray?>(null)
    }

    LaunchedEffect(localImageFile) {
        imageBytes = localImageFile?.toByteArray()
    }

    Box(modifier = modifier) {
        CoilImage(
            imageModel = { avatarUrl ?: imageBytes },
            loading = {
                CircularProgressIndicator()
            },
            failure = {
                Image(
                    painterResource(Res.drawable.img_placeholder),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            },
            modifier =
                Modifier
                    .size(200.dp)
                    .clip(CircleShape),
        )
        FilledIconButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            onClick = onCameraClick,
        ) {
            Icon(
                painterResource(Res.drawable.ic_camera),
                contentDescription = null,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserProfileTopBar(
    modifier: Modifier = Modifier,
    enableSaveButton: Boolean,
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                stringResource(Res.string.edit_profile_screen_edit_profile_title),
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painterResource(Res.drawable.ic_caret_left),
                    contentDescription = null,
                )
            }
        },
        actions = {
            IconButton(
                onClick = onSaveClick,
                enabled = enableSaveButton,
            ) {
                Icon(
                    painterResource(Res.drawable.ic_disk),
                    contentDescription = null,
                )
            }
        },
    )
}
