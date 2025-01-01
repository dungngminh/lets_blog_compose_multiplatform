package me.dungngminh.lets_blog_kmp.presentation.create_blog

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.ScreenTransition
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.OutlinedRichTextEditor
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.create_blog_screen_create_blog_title
import letsblogkmp.composeapp.generated.resources.create_blog_screen_put_your_content_here_label
import letsblogkmp.composeapp.generated.resources.ic_check
import me.dungngminh.lets_blog_kmp.presentation.create_blog.components.EditorStyleFormatBar
import me.dungngminh.lets_blog_kmp.presentation.create_blog.preview.PreviewBlogScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalVoyagerApi::class)
object CreateBlogScreen : Screen, ScreenTransition {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val richTextState = rememberRichTextState()

        val enableCheckButton by remember(richTextState.annotatedString) {
            derivedStateOf {
                richTextState.annotatedString.isNotEmpty()
            }
        }

        CreateBlogScreen(
            onBackClick = {
                navigator.pop()
            },
            onCheckClick = {
                navigator.push(PreviewBlogScreen(richTextState.toHtml()))
            },
            richTextState = richTextState,
            enableCheckButton = enableCheckButton,
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
fun CreateBlogScreen(
    modifier: Modifier = Modifier,
    richTextState: RichTextState,
    onBackClick: () -> Unit = {},
    onCheckClick: () -> Unit = {},
    enableCheckButton: Boolean = false,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CreateBlogAppBar(
                onBackClick = onBackClick,
                onCheckClick = onCheckClick,
                enableCheckButton = enableCheckButton,
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .windowInsetsPadding(WindowInsets.ime)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            EditorStyleFormatBar(
                modifier = Modifier.fillMaxWidth(),
                richTextState = richTextState,
            )
            LazyColumn(
                modifier =
                    Modifier
                        .padding(top = 12.dp)
                        .weight(1f),
            ) {
                item {
                    OutlinedRichTextEditor(
                        state = richTextState,
                        modifier =
                            Modifier
                                .fillParentMaxSize(),
                        placeholder = {
                            Text(stringResource(Res.string.create_blog_screen_put_your_content_here_label))
                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateBlogAppBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onCheckClick: () -> Unit = {},
    enableCheckButton: Boolean = true,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
            ) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "create_post_back_button",
                )
            }
        },
        title = {
            Text(stringResource(Res.string.create_blog_screen_create_blog_title))
        },
        actions = {
            IconButton(
                enabled = enableCheckButton,
                onClick = onCheckClick,
            ) {
                Icon(
                    painterResource(Res.drawable.ic_check),
                    contentDescription = "create_post_done_button",
                )
            }
        },
    )
}

@Preview
@Composable
fun Preview_CreateBlogScreen() {
    MaterialTheme {
        CreateBlogScreen(
            richTextState = rememberRichTextState(),
        )
    }
}
