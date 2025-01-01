package me.dungngminh.lets_blog_kmp.presentation.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun LoadingDialog(
    onDismissRequest: () -> Unit = {},
    dismissible: Boolean = false,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties =
            DialogProperties(
                dismissOnClickOutside = dismissible,
                dismissOnBackPress = dismissible,
            ),
    ) {
        Center {
            CircularProgressIndicator()
        }
    }
}
