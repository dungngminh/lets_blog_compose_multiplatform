package me.dungngminh.lets_blog_kmp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.fab_create_blog_label
import letsblogkmp.composeapp.generated.resources.favorite_screen_no_favorite_blog_label
import letsblogkmp.composeapp.generated.resources.general_retry
import letsblogkmp.composeapp.generated.resources.general_something_went_wrong_please_try_again
import letsblogkmp.composeapp.generated.resources.home_screen_no_blog_label
import letsblogkmp.composeapp.generated.resources.img_empty_blog
import letsblogkmp.composeapp.generated.resources.img_error
import letsblogkmp.composeapp.generated.resources.profile_screen_you_dont_have_any_blog_label
import letsblogkmp.composeapp.generated.resources.search_screen_empty_result_blog_label
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

enum class ErrorViewType(
    val messageRes: StringResource,
    val drawableRes: DrawableResource,
    val actionButtonMessageRes: StringResource = Res.string.general_retry,
    val showActionButton: Boolean,
) {
    EMPTY_FAVORITE_BLOG(
        messageRes = Res.string.favorite_screen_no_favorite_blog_label,
        drawableRes = Res.drawable.img_empty_blog,
        showActionButton = false,
    ),
    EMPTY_HOME_BLOG(
        messageRes = Res.string.home_screen_no_blog_label,
        drawableRes = Res.drawable.img_empty_blog,
        showActionButton = false,
    ),
    EMPTY_RESULT_BLOG(
        messageRes = Res.string.search_screen_empty_result_blog_label,
        drawableRes = Res.drawable.img_empty_blog,
        showActionButton = false,
    ),
    GENERAL_ERROR(
        messageRes = Res.string.general_something_went_wrong_please_try_again,
        drawableRes = Res.drawable.img_error,
        showActionButton = true,
    ),
    EMPTY_USER_BLOG(
        messageRes = Res.string.profile_screen_you_dont_have_any_blog_label,
        drawableRes = Res.drawable.img_empty_blog,
        actionButtonMessageRes = Res.string.fab_create_blog_label,
        showActionButton = true,
    ),
}

@Composable
fun ErrorView(
    modifier: Modifier = Modifier,
    type: ErrorViewType,
    onActionClick: () -> Unit = {},
    skipSpacing: Boolean = false,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (!skipSpacing) {
            Spacer(modifier = Modifier.weight(0.4f))
        }
        Image(
            painterResource(type.drawableRes),
            contentDescription = null,
            modifier = Modifier.height(300.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            stringResource(type.messageRes),
            style =
                MaterialTheme.typography.bodyLarge
                    .copy(fontWeight = FontWeight.Medium),
            textAlign = TextAlign.Center,
        )
        if (type.showActionButton) {
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onActionClick,
            ) {
                Text(stringResource(type.actionButtonMessageRes))
            }
        }
        if (!skipSpacing) {
            Spacer(modifier = Modifier.weight(0.6f))
        }
    }
}
