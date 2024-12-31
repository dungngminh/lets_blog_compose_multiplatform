package me.dungngminh.lets_blog_kmp.presentation.create_blog.preview

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.create_blog_preview_blog_category_label
import letsblogkmp.composeapp.generated.resources.create_blog_preview_blog_title
import letsblogkmp.composeapp.generated.resources.create_blog_preview_screen_preview_blog_title
import letsblogkmp.composeapp.generated.resources.create_blog_preview_screen_title_not_empty_label
import letsblogkmp.composeapp.generated.resources.ic_rocket_launch
import me.dungngminh.lets_blog_kmp.domain.entities.BlogCategory
import me.dungngminh.lets_blog_kmp.presentation.components.Center
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalVoyagerApi::class)
class PreviewBlogScreen(
    private val content: String,
) : Screen,
    ScreenTransition {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val viewModel = koinScreenModel<PreviewBlogViewModel> { parametersOf(content) }

        val createBlogPreviewUiState by viewModel.uiState.collectAsStateWithLifecycle()

        val richTextState = rememberRichTextState()

        LaunchedEffect(Unit) {
            richTextState.setText(content)
        }

        if (createBlogPreviewUiState.isLoading) {
            Center {
                CircularProgressIndicator()
            }
        }

        CreateBlogPreviewContent(
            createBlogPreviewUiState = createBlogPreviewUiState,
            richTextState = richTextState,
            onBackClick = {
                navigator.pop()
            },
            onCreateBlockClick = viewModel::publishBlog,
        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBlogPreviewContent(
    modifier: Modifier = Modifier,
    createBlogPreviewUiState: CreateBlogPreviewUiState,
    richTextState: RichTextState,
    onBackClick: () -> Unit = {},
    onCreateBlockClick: () -> Unit = {},
    onCategoryClick: (BlogCategory) -> Unit = {},
    onTitleChange: (String) -> Unit = {},
) {
    var isCategoryDropDownExpanded by remember { mutableStateOf(false) }

    Scaffold(modifier = modifier, topBar = {
        CreateBlogPreviewAppBar(
            onPublishBlogClick = onCreateBlockClick,
            onBackClick = onBackClick,
            enablePublishButton = createBlogPreviewUiState.isFormValid,
        )
    }) { innerPadding ->
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
                BlogTitleTextField(
                    modifier = Modifier.fillMaxWidth(),
                    createBlogPreviewUiState = createBlogPreviewUiState,
                    onTitleChange = onTitleChange,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                BlogCategoryDropdownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    selectedCategory = createBlogPreviewUiState.category,
                    isCategoryDropDownExpanded = isCategoryDropDownExpanded,
                    onExpandedChange = { isCategoryDropDownExpanded = it },
                    onDismissRequest = { isCategoryDropDownExpanded = false },
                    onCategoryClick = onCategoryClick,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                RichText(state = richTextState)
            }
        }
    }
}

@Composable
private fun BlogTitleTextField(
    modifier: Modifier = Modifier,
    createBlogPreviewUiState: CreateBlogPreviewUiState,
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
            modifier =
                Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
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
private fun CreateBlogPreviewAppBar(
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
