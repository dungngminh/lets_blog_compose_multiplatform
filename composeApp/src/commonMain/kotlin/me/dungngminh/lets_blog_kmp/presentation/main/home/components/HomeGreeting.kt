package me.dungngminh.lets_blog_kmp.presentation.main.home.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.general_my_friend
import letsblogkmp.composeapp.generated.resources.home_screen_good_afternoon_label
import letsblogkmp.composeapp.generated.resources.home_screen_good_evening_label
import letsblogkmp.composeapp.generated.resources.home_screen_good_morning_label
import letsblogkmp.composeapp.generated.resources.home_screen_greeting_label
import letsblogkmp.composeapp.generated.resources.home_screen_welcome_to_lets_blog_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeGreeting(
    modifier: Modifier = Modifier,
    username: String? = null,
    userAvatarUrl: String? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                stringResource(
                    Res.string.home_screen_greeting_label,
                    greetingByTime(),
                    username ?: stringResource(Res.string.general_my_friend),
                ),
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                stringResource(
                    Res.string.home_screen_welcome_to_lets_blog_label,
                ),
                style =
                    MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium,
                    ),
            )
        }
        Box(
            modifier =
                Modifier
                    .size(36.dp)
                    .clip(CircleShape),
        ) {
            if (userAvatarUrl != null) {
                CoilImage(
                    imageModel = { userAvatarUrl },
                    imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                    loading = {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun greetingByTime(): String {
    val time =
        remember {
            Clock.System
                .now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .hour
        }
    return when {
        time < 12 -> stringResource(Res.string.home_screen_good_morning_label)
        time < 18 -> stringResource(Res.string.home_screen_good_afternoon_label)
        else -> stringResource(Res.string.home_screen_good_evening_label)
    }
}

@Preview
@Composable
fun Preview_HomeGreetings() {
    HomeGreeting(
        modifier = Modifier.padding(16.dp),
        username = "dungngminh",
        userAvatarUrl = "https://avatars.githubusercontent.com/u/38183218?v=4",
    )
}
