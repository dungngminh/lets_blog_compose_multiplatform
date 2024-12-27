package me.dungngminh.lets_blog_kmp.presentation.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.fab_create_blog_label
import letsblogkmp.composeapp.generated.resources.ic_pencil
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CreateBlogFabButton(
    modifier: Modifier = Modifier,
    onFabClick: () -> Unit,
) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        onClick = onFabClick,
    ) {
        Icon(
            painterResource(Res.drawable.ic_pencil),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
        )
        Spacer(Modifier.width(8.dp))
        Text(stringResource(Res.string.fab_create_blog_label))
    }
}

@Preview
@Composable
fun PreviewCreateBlogFabButton() {
    MaterialTheme {
        CreateBlogFabButton { }
    }
}
