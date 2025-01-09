package me.dungngminh.lets_blog_kmp.presentation.create_blog

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntOffset
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.ScreenTransition
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.create_blog_screen_create_blog_title
import me.dungngminh.lets_blog_kmp.LocalWindowSizeClass
import me.dungngminh.lets_blog_kmp.presentation.components.blog_editor.BlogEditorAppBar
import me.dungngminh.lets_blog_kmp.presentation.components.blog_editor.BlogEditorContent
import me.dungngminh.lets_blog_kmp.presentation.preview_blog.PreviewBlogScreen
import me.dungngminh.lets_blog_kmp.presentation.preview_blog.PreviewPublishAction
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

        val windowSizeClass = LocalWindowSizeClass.currentOrThrow

        BlogEditorContent(
            richTextState = richTextState,
            topAppBar = {
                BlogEditorAppBar(
                    onBackClick = {
                        navigator.pop()
                    },
                    onCheckClick = {
                        navigator.push(
                            PreviewBlogScreen(
                                content = richTextState.toHtml(),
                                publishAction = PreviewPublishAction.PUBLISH_NEW,
                            ),
                        )
                    },
                    title = stringResource(Res.string.create_blog_screen_create_blog_title),
                    enableCheckButton = enableCheckButton,
                )
            },
            isMediumScreen = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium,
            isExpandedScreen = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded,
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

@Preview
@Composable
fun Preview_CreateBlogScreen() {
    MaterialTheme {
        BlogEditorContent(
            richTextState = rememberRichTextState(),
            topAppBar = {},
        )
    }
}
