package me.dungngminh.lets_blog_kmp.presentation.main.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.presentation.components.BlogCard
import me.dungngminh.lets_blog_kmp.presentation.components.Center
import me.dungngminh.lets_blog_kmp.presentation.detail_blog.DetailBlogScreen
import me.dungngminh.lets_blog_kmp.presentation.main.MainScreenDestination
import org.jetbrains.compose.resources.stringResource

@OptIn(FlowPreview::class)
object SearchTab : Tab {
    @Composable
    override fun Content() {
        val rootNavigator = LocalNavigator.currentOrThrow.parent
        val viewModel = koinScreenModel<SearchViewModel>()

        var searchFieldState by remember { mutableStateOf("") }

        val searchUiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            snapshotFlow { searchFieldState }
                .debounce(350)
                .filter { it.isNotBlank() }
                .onEach { viewModel.search(it) }
                .launchIn(this)
        }

        SearchScreenContent(
            searchFieldState = searchFieldState,
            searchUiState = searchUiState,
            onSearchFieldChange = { searchFieldState = it },
            onBlogClick = {
                rootNavigator?.push(DetailBlogScreen(it))
            },
        )
    }

    override val options: TabOptions
        @Composable
        get() =
            TabOptions(
                index =
                    MainScreenDestination.entries
                        .indexOf(MainScreenDestination.Search)
                        .toUShort(),
                title = stringResource(MainScreenDestination.Search.title),
            )
}

@Composable
private fun SearchScreenContent(
    modifier: Modifier = Modifier,
    searchFieldState: String,
    onSearchFieldChange: (String) -> Unit,
    onBlogClick: (Blog) -> Unit = {},
    searchUiState: SearchUiState,
) {
    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .windowInsetsPadding(WindowInsets.ime)
                    .fillMaxSize(),
        ) {
            OutlinedTextField(
                value = searchFieldState,
                onValueChange = onSearchFieldChange,
                modifier =
                    Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))

            when {
                searchUiState.isLoading -> {
                    Center(
                        modifier = Modifier.weight(1f),
                    ) {
                        CircularProgressIndicator()
                    }
                }

                searchUiState.errorMessage != null -> {
                    Center(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            searchUiState.errorMessage,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier =
                            Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp),
                    ) {
                        itemsIndexed(
                            searchUiState.searchedBlogs.toImmutableList(),
                            key = { _, blog -> blog.id },
                        ) { index, blog ->
                            BlogCard(
                                blog = blog,
                                onClick = { onBlogClick(blog) },
                                modifier = Modifier.fillMaxWidth(),
                            )
                            if (index < searchUiState.searchedBlogs.size - 1) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
