package me.dungngminh.lets_blog_kmp.presentation.components.blog_editor

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.ic_align_center
import letsblogkmp.composeapp.generated.resources.ic_align_justify
import letsblogkmp.composeapp.generated.resources.ic_align_left
import letsblogkmp.composeapp.generated.resources.ic_align_right
import letsblogkmp.composeapp.generated.resources.ic_bold
import letsblogkmp.composeapp.generated.resources.ic_code_simple
import letsblogkmp.composeapp.generated.resources.ic_dash_list
import letsblogkmp.composeapp.generated.resources.ic_heading_1
import letsblogkmp.composeapp.generated.resources.ic_heading_2
import letsblogkmp.composeapp.generated.resources.ic_heading_3
import letsblogkmp.composeapp.generated.resources.ic_heading_4
import letsblogkmp.composeapp.generated.resources.ic_heading_5
import letsblogkmp.composeapp.generated.resources.ic_italic
import letsblogkmp.composeapp.generated.resources.ic_number_list
import letsblogkmp.composeapp.generated.resources.ic_strikethrough
import letsblogkmp.composeapp.generated.resources.ic_underline
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

enum class EditorStyleButtonType {
    BOLD,
    ITALIC,
    UNDERLINE,
    STRIKETHROUGH,
    JUSTIFY_LEFT,
    JUSTIFY_CENTER,
    JUSTIFY_RIGHT,
    JUSTIFY_FULL,
    H1,
    H2,
    H3,
    H4,
    H5,
    CODE_BLOCK,
    NUMBERED_LIST,
    BULLET_LIST,
    ;

    val icon: DrawableResource
        get() =
            when (this) {
                BOLD -> Res.drawable.ic_bold
                ITALIC -> Res.drawable.ic_italic
                UNDERLINE -> Res.drawable.ic_underline
                STRIKETHROUGH -> Res.drawable.ic_strikethrough
                JUSTIFY_LEFT -> Res.drawable.ic_align_left
                JUSTIFY_CENTER -> Res.drawable.ic_align_center
                JUSTIFY_RIGHT -> Res.drawable.ic_align_right
                JUSTIFY_FULL -> Res.drawable.ic_align_justify
                CODE_BLOCK -> Res.drawable.ic_code_simple
                NUMBERED_LIST -> Res.drawable.ic_number_list
                BULLET_LIST -> Res.drawable.ic_dash_list
                H1 -> Res.drawable.ic_heading_1
                H2 -> Res.drawable.ic_heading_2
                H3 -> Res.drawable.ic_heading_3
                H4 -> Res.drawable.ic_heading_4
                H5 -> Res.drawable.ic_heading_5
            }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditorStyleFormatBar(
    modifier: Modifier = Modifier,
    richTextState: RichTextState = rememberRichTextState(),
) {
    FlowRow(
        modifier = modifier,
    ) {
        EditorStyleButtonType.entries
            .forEach { styleButtonType ->
                val isSelected by remember {
                    derivedStateOf {
                        isStyleButtonSelected(
                            richTextState = richTextState,
                            styleButtonType = styleButtonType,
                        )
                    }
                }
                EditorStyleButton(
                    isSelected = isSelected,
                    onClick = {
                        onStyleButtonClick(
                            richTextState = richTextState,
                            styleButtonType = styleButtonType,
                        )
                    },
                    styleButtonType = styleButtonType,
                )
            }
    }
}

private fun isStyleButtonSelected(
    richTextState: RichTextState,
    styleButtonType: EditorStyleButtonType,
): Boolean =
    when (styleButtonType) {
        EditorStyleButtonType.BOLD -> {
            richTextState.currentSpanStyle.fontWeight == FontWeight.ExtraBold
        }

        EditorStyleButtonType.ITALIC -> {
            richTextState.currentSpanStyle.fontStyle == FontStyle.Italic
        }

        EditorStyleButtonType.UNDERLINE -> {
            richTextState.currentSpanStyle.textDecoration?.contains(TextDecoration.Underline) == true
        }

        EditorStyleButtonType.STRIKETHROUGH -> {
            richTextState.currentSpanStyle.textDecoration?.contains(TextDecoration.LineThrough) == true
        }

        EditorStyleButtonType.JUSTIFY_LEFT -> {
            richTextState.currentParagraphStyle.textAlign == TextAlign.Left
        }

        EditorStyleButtonType.JUSTIFY_CENTER -> {
            richTextState.currentParagraphStyle.textAlign == TextAlign.Center
        }

        EditorStyleButtonType.JUSTIFY_RIGHT -> {
            richTextState.currentParagraphStyle.textAlign == TextAlign.Right
        }

        EditorStyleButtonType.JUSTIFY_FULL -> {
            richTextState.currentParagraphStyle.textAlign == TextAlign.Justify
        }

        EditorStyleButtonType.CODE_BLOCK -> {
            richTextState.isCodeSpan
        }

        EditorStyleButtonType.NUMBERED_LIST -> {
            richTextState.isOrderedList
        }

        EditorStyleButtonType.BULLET_LIST -> {
            richTextState.isUnorderedList
        }

        EditorStyleButtonType.H1 -> richTextState.currentSpanStyle.fontSize == 40.sp
        EditorStyleButtonType.H2 -> richTextState.currentSpanStyle.fontSize == 32.sp
        EditorStyleButtonType.H3 -> richTextState.currentSpanStyle.fontSize == 24.sp
        EditorStyleButtonType.H4 -> richTextState.currentSpanStyle.fontSize == 20.sp
        EditorStyleButtonType.H5 -> richTextState.currentSpanStyle.fontSize == 16.sp
    }

fun onStyleButtonClick(
    richTextState: RichTextState,
    styleButtonType: EditorStyleButtonType,
) {
    when (styleButtonType) {
        EditorStyleButtonType.BOLD -> {
            richTextState.toggleSpanStyle(
                SpanStyle(
                    fontWeight = FontWeight.ExtraBold,
                ),
            )
        }

        EditorStyleButtonType.ITALIC -> {
            richTextState.toggleSpanStyle(
                SpanStyle(
                    fontStyle = FontStyle.Italic,
                ),
            )
        }

        EditorStyleButtonType.UNDERLINE -> {
            richTextState.toggleSpanStyle(
                SpanStyle(
                    textDecoration = TextDecoration.Underline,
                ),
            )
        }

        EditorStyleButtonType.STRIKETHROUGH -> {
            richTextState.toggleSpanStyle(
                SpanStyle(
                    textDecoration = TextDecoration.LineThrough,
                ),
            )
        }

        EditorStyleButtonType.JUSTIFY_LEFT -> {
            richTextState.addParagraphStyle(
                ParagraphStyle(
                    textAlign = TextAlign.Left,
                ),
            )
        }

        EditorStyleButtonType.JUSTIFY_CENTER -> {
            richTextState.addParagraphStyle(
                ParagraphStyle(
                    textAlign = TextAlign.Center,
                ),
            )
        }

        EditorStyleButtonType.JUSTIFY_RIGHT -> {
            richTextState.addParagraphStyle(
                ParagraphStyle(
                    textAlign = TextAlign.Right,
                ),
            )
        }

        EditorStyleButtonType.JUSTIFY_FULL -> {
            richTextState.addParagraphStyle(
                ParagraphStyle(
                    textAlign = TextAlign.Justify,
                ),
            )
        }

        EditorStyleButtonType.CODE_BLOCK -> {
            richTextState.toggleCodeSpan()
        }

        EditorStyleButtonType.NUMBERED_LIST -> {
            richTextState.toggleOrderedList()
        }

        EditorStyleButtonType.BULLET_LIST -> {
            richTextState.toggleUnorderedList()
        }

        EditorStyleButtonType.H1 -> {
            richTextState.toggleSpanStyle(
                SpanStyle(
                    fontSize = 40.sp,
                ),
            )
        }

        EditorStyleButtonType.H2 -> {
            richTextState.toggleSpanStyle(
                SpanStyle(
                    fontSize = 32.sp,
                ),
            )
        }

        EditorStyleButtonType.H3 -> {
            richTextState.toggleSpanStyle(
                SpanStyle(
                    fontSize = 24.sp,
                ),
            )
        }

        EditorStyleButtonType.H4 -> {
            richTextState.toggleSpanStyle(
                SpanStyle(
                    fontSize = 20.sp,
                ),
            )
        }

        EditorStyleButtonType.H5 -> {
            richTextState.toggleSpanStyle(
                SpanStyle(
                    fontSize = 16.sp,
                ),
            )
        }
    }
}

@Composable
private fun EditorStyleButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    styleButtonType: EditorStyleButtonType,
) {
    if (isSelected) {
        FilledIconButton(
            onClick = onClick,
            modifier =
                modifier
                    .focusProperties { canFocus = false },
        ) {
            Icon(
                painterResource(styleButtonType.icon),
                contentDescription = null,
            )
        }
    } else {
        IconButton(
            onClick = onClick,
            modifier =
                modifier
                    .focusProperties { canFocus = false },
        ) {
            Icon(
                painterResource(styleButtonType.icon),
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
fun Preview_EditorStyleFormatBar() {
    MaterialTheme {
        EditorStyleFormatBar()
    }
}
