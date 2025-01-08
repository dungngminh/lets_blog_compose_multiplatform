package me.dungngminh.lets_blog_kmp.presentation.detail_blog.summary

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import io.github.alexzhirkevich.compottie.DotLottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.summary_screen_ai_is_summarizing
import letsblogkmp.composeapp.generated.resources.summary_screen_summary_title
import me.dungngminh.lets_blog_kmp.presentation.components.ErrorView
import me.dungngminh.lets_blog_kmp.presentation.components.ErrorViewType
import me.dungngminh.lets_blog_kmp.presentation.detail_blog.SummaryBlogContentState
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Composable
fun SummaryBlogContentBottomSheet(
    modifier: Modifier = Modifier,
    summaryBlogContentState: SummaryBlogContentState,
) {
    Column(
        modifier =
            modifier
                .windowInsetsPadding(WindowInsets.navigationBars)
                .fillMaxWidth()
                .fillMaxHeight(0.95f)
                .padding(horizontal = 16.dp),
    ) {
        when (summaryBlogContentState) {
            SummaryBlogContentState.Initial,
            SummaryBlogContentState.Loading,
            -> SummaryBlogContentLoading()

            is SummaryBlogContentState.Error ->
                ErrorView(
                    type = ErrorViewType.GENERAL_ERROR,
                )

            is SummaryBlogContentState.Success ->
                SummaryBlogContent(
                    summarizedContent = summaryBlogContentState.summarizedContent,
                )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SummaryBlogContentLoading(modifier: Modifier = Modifier) {
    val lottieComposition by rememberLottieComposition {
        LottieCompositionSpec.DotLottie(
            Res.readBytes("files/gemini.lottie"),
        )
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = rememberLottiePainter(composition = lottieComposition),
            contentDescription = "Gemini loading",
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(250.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            stringResource(Res.string.summary_screen_ai_is_summarizing),
            style =
                MaterialTheme.typography.bodyLarge
                    .copy(fontWeight = FontWeight.Medium),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun SummaryBlogContent(
    modifier: Modifier = Modifier,
    summarizedContent: String,
) {
    val richTextState = rememberRichTextState()
    LaunchedEffect(summarizedContent) {
        richTextState.setMarkdown(summarizedContent)
    }

    LazyColumn(modifier = modifier) {
        item {
            Text(
                stringResource(Res.string.summary_screen_summary_title),
                style =
                    MaterialTheme.typography.titleLarge
                        .copy(fontWeight = FontWeight.Medium),
            )
            Spacer(modifier = Modifier.height(12.dp))
            RichText(
                richTextState,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Preview
@Composable
fun Preview_SummaryBlogContentLoading() {
    SummaryBlogContentLoading()
}
