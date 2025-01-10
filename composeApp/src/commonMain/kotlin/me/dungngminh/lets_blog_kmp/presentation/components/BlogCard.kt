package me.dungngminh.lets_blog_kmp.presentation.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.img_placeholder
import me.dungngminh.lets_blog_kmp.commons.extensions.timeAgo
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.entities.BlogCategory
import me.dungngminh.lets_blog_kmp.domain.entities.User
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BlogCard(
    modifier: Modifier = Modifier,
    blog: Blog,
    onClick: () -> Unit = {},
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CoilImage(
                imageModel = { blog.imageUrl },
                modifier =
                    Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp)),
                imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                loading = {
                    CircularProgressIndicator()
                },
                failure = {
                    Image(
                        painterResource(Res.drawable.img_placeholder),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                },
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    blog.title,
                    style =
                        MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                        ),
                    minLines = 1,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    stringResource(blog.category.localizedStringRes()) + " - " + blog.createdAt.timeAgo(),
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    blog.creator.name,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Preview
@Composable
fun Preview_BlogCard() {
    MaterialTheme {
        BlogCard(
            blog =
                Blog(
                    id = "libero",
                    title = "pri",
                    content = "verterem",
                    imageUrl = "https://duckduckgo.com/?q=vituperatoribus",
                    category = BlogCategory.TRAVEL,
                    createdAt = 7578,
                    updatedAt = 9168,
                    creator =
                        User(
                            id = "propriae",
                            name = "Gregorio Stone",
                            email = "dadad",
                        ),
                ),
        )
    }
}
