package me.dungngminh.lets_blog_kmp.presentation.onboarding

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.onboarding1
import letsblogkmp.composeapp.generated.resources.onboarding2
import letsblogkmp.composeapp.generated.resources.onboarding3
import letsblogkmp.composeapp.generated.resources.onboarding_page_done_button_label
import letsblogkmp.composeapp.generated.resources.onboarding_page_next_button_label
import letsblogkmp.composeapp.generated.resources.onboarding_screen_description_1
import letsblogkmp.composeapp.generated.resources.onboarding_screen_description_2
import letsblogkmp.composeapp.generated.resources.onboarding_screen_description_3
import letsblogkmp.composeapp.generated.resources.onboarding_screen_title_1
import letsblogkmp.composeapp.generated.resources.onboarding_screen_title_2
import letsblogkmp.composeapp.generated.resources.onboarding_screen_title_3
import me.dungngminh.lets_blog_kmp.AppViewModel
import me.dungngminh.lets_blog_kmp.commons.theme.LetsBlogAppTheme
import me.dungngminh.lets_blog_kmp.presentation.main.MainScreen
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.absoluteValue

data class OnboardingContent(
    val title: StringResource,
    val description: StringResource,
    val image: DrawableResource,
)

class OnboardingScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val appViewModel = koinScreenModel<AppViewModel>()
        OnboardingScreenContent(
            onDoneClick = {
                appViewModel.saveIsOnboardingCompleted()
                navigator.replace(MainScreen())
            },
        )
    }
}

@Composable
fun OnboardingScreenContent(
    modifier: Modifier = Modifier,
    onDoneClick: () -> Unit,
) {
    val onboardingContent =
        listOf(
            OnboardingContent(
                title = Res.string.onboarding_screen_title_1,
                description = Res.string.onboarding_screen_description_1,
                image = Res.drawable.onboarding1,
            ),
            OnboardingContent(
                title = Res.string.onboarding_screen_title_2,
                description = Res.string.onboarding_screen_description_2,
                image = Res.drawable.onboarding2,
            ),
            OnboardingContent(
                title = Res.string.onboarding_screen_title_3,
                description = Res.string.onboarding_screen_description_3,
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
            ) { page ->
                val content = onboardingContent[page]
                OnboardingContentView(
                    content = content,
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp)
                            .graphicsLayer {
                                val pageOffset =
                                    (
                                        (pagerState.currentPage - page) +
                                            pagerState
                                                .currentPageOffsetFraction
                                    ).absoluteValue

                                alpha =
                                    lerp(
                                        start = 0.2f,
                                        stop = 1f,
                                        fraction = 1f - pageOffset.coerceIn(0f, 1f),
                                    )
                            },
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier =
                    Modifier
                        .padding(bottom = 24.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                onboardingContent.indices.forEachIndexed { index, _ ->
                    Spacer(modifier = Modifier.width(8.dp))
                    OnboardingIndicator(
                        isSelected = index == pagerState.currentPage,
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
                        Text(stringResource(Res.string.onboarding_page_done_button_label))
                    } else {
                        Text(stringResource(Res.string.onboarding_page_next_button_label))
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(content.image),
            contentDescription = null,
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .padding(8.dp),
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            stringResource(content.title),
            style =
                MaterialTheme.typography.headlineMedium
                    .copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            stringResource(content.description),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
fun OnboardingScreenPreview() {
    LetsBlogAppTheme {
        OnboardingScreenContent(onDoneClick = {})
    }
}
