package me.dungngminh.lets_blog_kmp.presentation.components.blog_editor

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.ic_check
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogEditorAppBar(
    modifier: Modifier = Modifier,
    title: String,
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
            Text(title)
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
