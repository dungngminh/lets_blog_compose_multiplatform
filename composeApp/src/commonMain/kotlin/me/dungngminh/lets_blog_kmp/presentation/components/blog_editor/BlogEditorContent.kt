package me.dungngminh.lets_blog_kmp.presentation.components.blog_editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material3.OutlinedRichTextEditor
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.blog_editor_put_your_content_here_label
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogEditorContent(
    modifier: Modifier = Modifier,
    topAppBar: @Composable () -> Unit,
    richTextState: RichTextState,
    isMediumScreen: Boolean = false,
    isExpandedScreen: Boolean = false,
) {
    Scaffold(
        modifier = modifier,
        topBar = topAppBar,
    ) { innerPadding ->
        val spaceWeight =
            remember(isMediumScreen, isExpandedScreen) {
                when {
                    isExpandedScreen -> 0.15f
                    isMediumScreen -> 0.1f
                    else -> 0.05f
                }
            }
        val contentWeight =
            remember(isMediumScreen, isExpandedScreen) {
                when {
                    isExpandedScreen -> 0.7f
                    isMediumScreen -> 0.8f
                    else -> 0.9f
                }
            }
        Row(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .windowInsetsPadding(WindowInsets.ime)
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
        ) {
            Spacer(modifier = Modifier.weight(spaceWeight))
            Column(modifier = Modifier.weight(contentWeight)) {
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
                                    .fillParentMaxSize()
                                    .onPreviewKeyEvent { event ->
                                        onKeyCombination(
                                            richTextState = richTextState,
                                            key = event.key,
                                            isCtrlPressed = event.isCtrlPressed,
                                            type = event.type,
                                        )
                                    },
                            placeholder = {
                                Text(stringResource(Res.string.blog_editor_put_your_content_here_label))
                            },
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(spaceWeight))
        }
    }
}

private fun onKeyCombination(
    richTextState: RichTextState,
    key: Key,
    isCtrlPressed: Boolean,
    type: KeyEventType,
): Boolean =
    if (type != KeyEventType.KeyUp) {
        false
    } else {
        when {
            isCtrlPressed && key == Key.B -> {
                onStyleButtonClick(
                    richTextState = richTextState,
                    styleButtonType = EditorStyleButtonType.BOLD,
                )
                true
            }

            isCtrlPressed && key == Key.I -> {
                onStyleButtonClick(
                    richTextState = richTextState,
                    styleButtonType = EditorStyleButtonType.ITALIC,
                )
                true
            }

            isCtrlPressed && key == Key.U -> {
                onStyleButtonClick(
                    richTextState = richTextState,
                    styleButtonType = EditorStyleButtonType.UNDERLINE,
                )
                true
            }

            isCtrlPressed && key == Key.L -> {
                onStyleButtonClick(
                    richTextState = richTextState,
                    styleButtonType = EditorStyleButtonType.JUSTIFY_LEFT,
                )
                true
            }

            isCtrlPressed && key == Key.E -> {
                onStyleButtonClick(
                    richTextState = richTextState,
                    styleButtonType = EditorStyleButtonType.JUSTIFY_CENTER,
                )
                true
            }

            isCtrlPressed && key == Key.R -> {
                onStyleButtonClick(
                    richTextState = richTextState,
                    styleButtonType = EditorStyleButtonType.JUSTIFY_RIGHT,
                )
                true
            }

            isCtrlPressed && key == Key.J -> {
                onStyleButtonClick(
                    richTextState = richTextState,
                    styleButtonType = EditorStyleButtonType.JUSTIFY_FULL,
                )
                true
            }

            else -> false
        }
    }
