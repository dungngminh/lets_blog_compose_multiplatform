package me.dungngminh.lets_blog_kmp.presentation.edit_blog

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntOffset
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.ScreenTransition
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.edit_blog_screen_edit_blog_title
import me.dungngminh.lets_blog_kmp.LocalWindowSizeClass
import me.dungngminh.lets_blog_kmp.commons.extensions.fromJsonStr
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.presentation.components.blog_editor.BlogEditorAppBar
import me.dungngminh.lets_blog_kmp.presentation.components.blog_editor.BlogEditorContent
import me.dungngminh.lets_blog_kmp.presentation.preview_blog.PreviewBlogScreen
import me.dungngminh.lets_blog_kmp.presentation.preview_blog.PreviewPublishAction
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalVoyagerApi::class)
class EditBlogScreen(
    private val blogId: String,
    private val blogData: String,
) : Screen,
    ScreenTransition {
    override val key: ScreenKey
        get() = "EditBlogScreen($blogId)"

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val blog = remember { blogData.fromJsonStr<Blog>() }

        val viewModel = koinScreenModel<EditBlogViewModel> { parametersOf(blog) }

        val enableCheckButton by remember(viewModel.richTextState.annotatedString) {
            derivedStateOf {
                viewModel.richTextState.annotatedString.isNotEmpty()
            }
        }

        val windowSizeClass = LocalWindowSizeClass.currentOrThrow

        BlogEditorContent(
            richTextState = viewModel.richTextState,
            topAppBar = {
                BlogEditorAppBar(
                    onBackClick = {
                        navigator.pop()
                    },
                    onCheckClick = {
                        navigator.push(
                            PreviewBlogScreen(
                                content = viewModel.richTextState.toHtml(),
                                blog = blog,
                                publishAction = PreviewPublishAction.PUBLISH_EDIT,
                            ),
                        )
                    },
                    title = stringResource(Res.string.edit_blog_screen_edit_blog_title),
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
