package me.dungngminh.lets_blog_kmp.presentation.onboarding

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.onboarding1
import letsblogkmp.composeapp.generated.resources.onboarding2
import letsblogkmp.composeapp.generated.resources.onboarding3
import me.dungngminh.lets_blog_kmp.commons.theme.LetsBlogAppTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

data class OnboardingContent(
    val title: String,
    val description: String,
    val image: DrawableResource,
)

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onDoneClick: () -> Unit,
) {
    val onboardingContent =
        listOf(
            OnboardingContent(
                title = "What should I know about Blogie?",
                description =
                    "Blogie is an open platform where 170 million\n" +
                        "readers come to find insightful and dynamic\n" +
                        "thinking.",
                image = Res.drawable.onboarding1,
            ),
            OnboardingContent(
                title =
                    "Write your ideas on\n" +
                        "the blog",
                description = "You can write your idea here and share it with other people.",
                image = Res.drawable.onboarding2,
            ),
            OnboardingContent(
                title =
                    "Read according to\n" +
                        "your passion ",
                description =
                    "You can read and explore your passion by\n" +
                        "using this application.",
                image = Res.drawable.onboarding3,
            ),
        )
    val pagerState =
        rememberPagerState(pageCount = {
            onboardingContent.size
        })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
        ) {
            HorizontalPager(
                modifier = Modifier.weight(1f),
                state = pagerState,
            ) {
                val content = onboardingContent[it]
                OnboardingContentView(
                    content = content,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                )
            }
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                for (i in onboardingContent.indices) {
                    Spacer(modifier = Modifier.width(8.dp))
                    OnboardingIndicator(
                        isSelected = i == pagerState.currentPage,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                val isLastPage = pagerState.currentPage == onboardingContent.size - 1
                TextButton(onClick = {
                    if (isLastPage) {
                        onDoneClick()
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                }) {
                    if (isLastPage) {
                        Text("Done")
                    } else {
                        Text("Next")
                    }
                }
            }
        }
    }
}

@Composable
fun OnboardingIndicator(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
) {
    val color =
        if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        }
    Box(
        modifier =
            modifier
                .height(8.dp)
                .width(8.dp)
                .background(
                    color = color,
                    shape = MaterialTheme.shapes.small,
                ),
    )
}

@Composable
fun OnboardingContentView(
    modifier: Modifier = Modifier,
    content: OnboardingContent,
) {
    Column(
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(content.image),
            contentDescription = null,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(400.dp),
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            content.title,
            style =
                MaterialTheme.typography.headlineMedium
                    .copy(color = MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            content.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
fun OnboardingScreenPreview() {
    LetsBlogAppTheme {
        OnboardingScreen(onDoneClick = {})
    }
}
