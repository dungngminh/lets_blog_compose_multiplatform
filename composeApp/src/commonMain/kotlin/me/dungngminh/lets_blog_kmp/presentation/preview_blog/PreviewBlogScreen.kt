package me.dungngminh.lets_blog_kmp.presentation.preview_blog

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.ScreenTransition
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import io.github.vinceglb.filekit.compose.PickerResultLauncher
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerType
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.create_blog_preview_blog_category_label
import letsblogkmp.composeapp.generated.resources.create_blog_preview_blog_content_title
import letsblogkmp.composeapp.generated.resources.create_blog_preview_blog_image
import letsblogkmp.composeapp.generated.resources.create_blog_preview_blog_title
import letsblogkmp.composeapp.generated.resources.create_blog_preview_screen_preview_blog_title
import letsblogkmp.composeapp.generated.resources.create_blog_preview_screen_title_not_empty_label
import letsblogkmp.composeapp.generated.resources.ic_image
import letsblogkmp.composeapp.generated.resources.ic_rocket_launch
import letsblogkmp.composeapp.generated.resources.ic_x
import letsblogkmp.composeapp.generated.resources.platform_image_picker_title
import me.dungngminh.lets_blog_kmp.LocalWindowSizeClass
import me.dungngminh.lets_blog_kmp.commons.extensions.toByteArray
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.entities.BlogCategory
import me.dungngminh.lets_blog_kmp.presentation.components.LoadingDialog
import me.dungngminh.lets_blog_kmp.presentation.main.MainScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf

enum class PreviewPublishAction {
    PUBLISH_NEW,
    PUBLISH_EDIT,
}

@OptIn(ExperimentalVoyagerApi::class)
class PreviewBlogScreen(
    private val blog: Blog? = null,
    private val content: String,
    private val publishAction: PreviewPublishAction,
) : Screen,
    ScreenTransition {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val viewModel = koinScreenModel<PreviewBlogViewModel> { parametersOf(content, blog) }

        val previewBlogUiState by viewModel.uiState.collectAsStateWithLifecycle()

        val richTextState = rememberRichTextState()

        val windowSizeClass = LocalWindowSizeClass.currentOrThrow

        var isCategoryDropDownExpanded by remember { mutableStateOf(false) }

        val imagePickerLauncher =
            rememberFilePickerLauncher(
                type = PickerType.Image,
                title = stringResource(Res.string.platform_image_picker_title),
            ) { file ->
                viewModel.changeImageFile(file)
            }

        LaunchedEffect(Unit) {
            richTextState.setHtml(content)
            viewModel.uiState
                .onEach { uiState ->
                    when {
                        uiState.isPublishSuccess -> {
                            navigator.popUntil { it is MainScreen }
                        }

                        uiState.errorMessage != null -> {
                            viewModel.onErrorMessageShown()
                        }
                    }
                }.launchIn(this)
        }

        if (previewBlogUiState.isLoading) {
            LoadingDialog()
        }

        if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {
            PreviewBlogExpandedContent(
                createBlogPreviewUiState = previewBlogUiState,
                richTextState = richTextState,
                onBackClick = {
                    navigator.pop()
                },
                onCreateBlockClick = {
                    when (publishAction) {
                        PreviewPublishAction.PUBLISH_NEW -> viewModel.publishBlog()
                        PreviewPublishAction.PUBLISH_EDIT -> blog?.let(viewModel::editBlog)
                    }
                },
                onCategoryClick = viewModel::changeCategory,
                onTitleChange = viewModel::changeTitle,
                onImageChange = viewModel::changeImageFile,
                isCategoryDropDownExpanded = isCategoryDropDownExpanded,
                imagePickerLauncher = imagePickerLauncher,
                onCategoryDropDownExpandedChange = { isCategoryDropDownExpanded = it },
            )
        } else {
            PreviewBlogCompactContent(
                createBlogPreviewUiState = previewBlogUiState,
                richTextState = richTextState,
                onBackClick = {
                    navigator.pop()
                },
                onCreateBlockClick = {
                    when (publishAction) {
                        PreviewPublishAction.PUBLISH_NEW -> viewModel.publishBlog()
                        PreviewPublishAction.PUBLISH_EDIT -> blog?.let(viewModel::editBlog)
                    }
                },
                onCategoryClick = viewModel::changeCategory,
                onTitleChange = viewModel::changeTitle,
                isCategoryDropDownExpanded = isCategoryDropDownExpanded,
                imagePickerLauncher = imagePickerLauncher,
                onCategoryDropDownExpandedChange = { isCategoryDropDownExpanded = it },
                onDeleteImageClick = viewModel::deleteImage,
            )
        }
    }

    override fun enter(lastEvent: StackEvent): EnterTransition =
        slideIn { size ->
            val x = if (lastEvent == StackEvent.Pop) -size.width else size.width
            IntOffset(x = x, y = 0)
        }

    override fun exit(lastEvent: StackEvent): ExitTransition =
        slideOut { size ->
            val x = if (lastEvent == StackEvent.Pop) size.width else -size.width
            IntOffset(x = x, y = 0)
        }
}

@Composable
fun PreviewBlogExpandedContent(
    modifier: Modifier = Modifier,
    createBlogPreviewUiState: PreviewBlogUiState,
    richTextState: RichTextState,
    onBackClick: () -> Unit = {},
    onCreateBlockClick: () -> Unit = {},
    onCategoryClick: (BlogCategory) -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onImageChange: (PlatformFile?) -> Unit = {},
    isCategoryDropDownExpanded: Boolean = true,
    imagePickerLauncher: PickerResultLauncher,
    onCategoryDropDownExpandedChange: (Boolean) -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            PreviewBlogAppBar(
                onPublishBlogClick = onCreateBlockClick,
                onBackClick = onBackClick,
                enablePublishButton = createBlogPreviewUiState.isFormValid,
            )
        },
    ) {}
}

@Composable
fun PreviewBlogCompactContent(
    modifier: Modifier = Modifier,
    createBlogPreviewUiState: PreviewBlogUiState,
    richTextState: RichTextState,
    onBackClick: () -> Unit = {},
    onCreateBlockClick: () -> Unit = {},
    onCategoryClick: (BlogCategory) -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onDeleteImageClick: () -> Unit,
    isCategoryDropDownExpanded: Boolean = true,
    imagePickerLauncher: PickerResultLauncher,
    onCategoryDropDownExpandedChange: (Boolean) -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            PreviewBlogAppBar(
                onPublishBlogClick = onCreateBlockClick,
                onBackClick = onBackClick,
                enablePublishButton = createBlogPreviewUiState.isFormValid,
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(WindowInsets.ime)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .padding(bottom = 16.dp),
        ) {
            item {
                Text(
                    stringResource(Res.string.create_blog_preview_blog_image),
                    style =
                        MaterialTheme
                            .typography.titleMedium
                            .copy(fontWeight = FontWeight.SemiBold),
                )
                Spacer(modifier = Modifier.height(8.dp))
                PreviewImageBox(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(350.dp),
                    imageFile = createBlogPreviewUiState.imageFile,
                    networkImage = createBlogPreviewUiState.networkImageUrl,
                    onPickImageClick = {
                        imagePickerLauncher.launch()
                    },
                    onDeleteImageClick = onDeleteImageClick,
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                BlogTitleTextField(
                    modifier = Modifier.fillMaxWidth(),
                    createBlogPreviewUiState = createBlogPreviewUiState,
                    onTitleChange = onTitleChange,
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                BlogCategoryDropdownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    selectedCategory = createBlogPreviewUiState.category,
                    isCategoryDropDownExpanded = isCategoryDropDownExpanded,
                    onExpandedChange = onCategoryDropDownExpandedChange,
                    onDismissRequest = { onCategoryDropDownExpandedChange(false) },
                    onCategoryClick = onCategoryClick,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(
                    stringResource(Res.string.create_blog_preview_blog_content_title),
                    style =
                        MaterialTheme
                            .typography.titleMedium
                            .copy(fontWeight = FontWeight.SemiBold),
                )
                Spacer(modifier = Modifier.height(12.dp))
                RichText(
                    state = richTextState,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun PreviewImageBox(
    modifier: Modifier = Modifier,
    networkImage: String? = null,
    imageFile: PlatformFile? = null,
    onPickImageClick: () -> Unit = {},
    onDeleteImageClick: () -> Unit = {},
) {
    var imageBytes by remember(imageFile) {
        mutableStateOf<ByteArray?>(null)
    }

    LaunchedEffect(imageFile) {
        imageBytes = imageFile?.toByteArray()
    }

    Box(
        modifier =
            modifier
                .clip(MaterialTheme.shapes.medium)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = MaterialTheme.shapes.medium,
                ).clickable {
                    if (imageFile == null) {
                        onPickImageClick()
                    }
                },
    ) {
        if (imageFile != null || networkImage != null) {
            Box {
                CoilImage(
                    modifier = Modifier.fillMaxSize(),
                    imageModel = {
                        when {
                            networkImage != null -> networkImage
                            imageFile != null -> imageBytes
                            else -> null
                        }
                    },
                    imageOptions =
                        ImageOptions(
                            contentScale = ContentScale.Crop,
                        ),
                )
                FilledTonalIconButton(
                    onClick = onDeleteImageClick,
                    modifier = Modifier.align(Alignment.TopEnd).padding(4.dp),
                ) {
                    Icon(
                        painterResource(Res.drawable.ic_x),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    painterResource(Res.drawable.ic_image),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    stringResource(Res.string.platform_image_picker_title),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Composable
private fun BlogTitleTextField(
    modifier: Modifier = Modifier,
    createBlogPreviewUiState: PreviewBlogUiState,
    onTitleChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = createBlogPreviewUiState.title,
        onValueChange = onTitleChange,
        isError = createBlogPreviewUiState.titleError == CreateBlogPreviewValidationError.EMPTY,
        label = {
            Text(
                text = stringResource(Res.string.create_blog_preview_blog_title),
            )
        },
        singleLine = true,
        modifier = modifier,
        supportingText = {
            if (createBlogPreviewUiState.titleError == CreateBlogPreviewValidationError.EMPTY) {
                Text(
                    text = stringResource(Res.string.create_blog_preview_screen_title_not_empty_label),
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BlogCategoryDropdownMenu(
    modifier: Modifier = Modifier,
    selectedCategory: BlogCategory,
    isCategoryDropDownExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    onCategoryClick: (BlogCategory) -> Unit,
) {
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = isCategoryDropDownExpanded,
        onExpandedChange = onExpandedChange,
    ) {
        OutlinedTextField(
            value = stringResource(selectedCategory.localizedStringRes()),
            onValueChange = {},
            modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
            readOnly = true,
            label = {
                Text(
                    text = stringResource(Res.string.create_blog_preview_blog_category_label),
                )
            },
            singleLine = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryDropDownExpanded)
            },
        )
        ExposedDropdownMenu(
            expanded = isCategoryDropDownExpanded,
            onDismissRequest = onDismissRequest,
        ) {
            BlogCategory.entries.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(stringResource(option.localizedStringRes()))
                    },
                    onClick = {
                        onCategoryClick(option)
                        onExpandedChange(false)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PreviewBlogAppBar(
    modifier: Modifier = Modifier,
    onPublishBlogClick: () -> Unit,
    onBackClick: () -> Unit,
    enablePublishButton: Boolean = false,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(Res.string.create_blog_preview_screen_preview_blog_title),
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        actions = {
            IconButton(
                onClick = onPublishBlogClick,
                enabled = enablePublishButton,
            ) {
                Icon(
                    painterResource(Res.drawable.ic_rocket_launch),
                    contentDescription = null,
                )
            }
        },
    )
}
