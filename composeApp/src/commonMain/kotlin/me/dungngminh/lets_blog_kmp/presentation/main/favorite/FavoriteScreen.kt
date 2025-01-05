package me.dungngminh.lets_blog_kmp.presentation.main.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.favorite_screen_favorite_blogs_label
import me.dungngminh.lets_blog_kmp.presentation.main.MainScreenDestination
import org.jetbrains.compose.resources.stringResource

object FavoriteTab : Tab {
    @Composable
    override fun Content() {
        FavoriteScreenContent()
    }

    override val options: TabOptions
        @Composable
        get() =
            TabOptions(
                index =
                    MainScreenDestination.entries
                        .indexOf(MainScreenDestination.Favorite)
                        .toUShort(),
                title = stringResource(MainScreenDestination.Favorite.title),
            )
}

@Composable
fun FavoriteScreenContent(modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier) {
        Box(
            modifier =
                Modifier
                    .padding(it)
                    .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text("Favorite Screen")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteAppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(stringResource(Res.string.favorite_screen_favorite_blogs_label))
        },
    )
}
