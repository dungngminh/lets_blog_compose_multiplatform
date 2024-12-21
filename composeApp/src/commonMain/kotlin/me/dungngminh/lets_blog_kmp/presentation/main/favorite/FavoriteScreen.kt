package me.dungngminh.lets_blog_kmp.presentation.main.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
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
private fun FavoriteScreenContent(modifier: Modifier = Modifier) {
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
