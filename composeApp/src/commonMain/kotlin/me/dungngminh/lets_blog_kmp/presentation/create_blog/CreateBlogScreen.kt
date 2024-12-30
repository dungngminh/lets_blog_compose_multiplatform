package me.dungngminh.lets_blog_kmp.presentation.create_blog

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.style.TextAlign
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
import letsblogkmp.composeapp.generated.resources.ic_check
import letsblogkmp.composeapp.generated.resources.ic_favorite
import letsblogkmp.composeapp.generated.resources.ic_favorite_filled
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalVoyagerApi::class)
object CreateBlogScreen : Screen, ScreenTransition {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val richTextState = rememberRichTextState()

        CreateBlogScreen(
            onBackClick = {
                navigator.pop()
            },
            richTextState = richTextState,
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
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CreateBlogAppBar(
                onBackClick = onBackClick,
                onCheckClick = onCheckClick,
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .windowInsetsPadding(WindowInsets.ime)
                    .fillMaxSize()
                    .padding(16.dp),
        ) {
            Row {
                IconButton(onClick = {
                    richTextState.addParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center))
                }) {
                    Icon(
                        if (richTextState.currentParagraphStyle.textAlign == TextAlign.Center) {
                            painterResource(
                                Res.drawable.ic_favorite_filled,
                            )
                        } else {
                            painterResource(
                                Res.drawable.ic_favorite,
                            )
                        },
                        contentDescription = "create_post_done_button",
                    )
                }
            }
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
fun PreviewCreateBlogScreen() {
    MaterialTheme {
        CreateBlogScreen(
            richTextState = rememberRichTextState(),
        )
    }
}
