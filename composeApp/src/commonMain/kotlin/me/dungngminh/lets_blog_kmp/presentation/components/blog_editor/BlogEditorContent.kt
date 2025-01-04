package me.dungngminh.lets_blog_kmp.presentation.components.blog_editor

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
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
) {
    Scaffold(
        modifier = modifier,
        topBar = topAppBar,
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
                            Text(stringResource(Res.string.blog_editor_put_your_content_here_label))
                        },
                    )
                }
            }
        }
    }
}
