package me.dungngminh.lets_blog_kmp.presentation.main.profile.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.img_blog_time
import letsblogkmp.composeapp.generated.resources.profile_screen_login_and_deep_dive_blog_world
import letsblogkmp.composeapp.generated.resources.profile_screen_login_label
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun UnauthenticatedProfileContent(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painterResource(Res.drawable.img_blog_time),
            contentDescription = null,
            modifier = Modifier.height(300.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            stringResource(Res.string.profile_screen_login_and_deep_dive_blog_world),
            style =
                MaterialTheme
                    .typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onLoginClick) {
            Text(stringResource(Res.string.profile_screen_login_label))
        }
    }
}

@Preview
@Composable
fun Preview_UnauthenticatedProfileScreen() {
    MaterialTheme {
        UnauthenticatedProfileContent(
            onLoginClick = {},
        )
    }
}
