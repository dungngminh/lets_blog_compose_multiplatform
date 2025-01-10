package me.dungngminh.lets_blog_kmp.presentation.main.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import kotlinx.collections.immutable.toImmutableList
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.ic_search_filled
import letsblogkmp.composeapp.generated.resources.search_screen_empty_query_label
import letsblogkmp.composeapp.generated.resources.search_screen_search_hint_label
import letsblogkmp.composeapp.generated.resources.search_screen_top_bar_title
import me.dungngminh.lets_blog_kmp.LocalWindowSizeClass
import me.dungngminh.lets_blog_kmp.commons.extensions.toJsonStr
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.presentation.components.BlogCard
import me.dungngminh.lets_blog_kmp.presentation.components.Center
import me.dungngminh.lets_blog_kmp.presentation.components.CreateBlogFabButton
import me.dungngminh.lets_blog_kmp.presentation.components.ErrorView
import me.dungngminh.lets_blog_kmp.presentation.components.ErrorViewType
import me.dungngminh.lets_blog_kmp.presentation.create_blog.CreateBlogScreen
import me.dungngminh.lets_blog_kmp.presentation.detail_blog.DetailBlogScreen
import me.dungngminh.lets_blog_kmp.presentation.main.MainScreenDestination
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionState
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

object SearchTab : Tab {
    @Composable
    override fun Content() {
        val rootNavigator = LocalNavigator.currentOrThrow.parent ?: return

        val userSessionViewModel = rootNavigator.koinNavigatorScreenModel<UserSessionViewModel>()

        val userSessionState by userSessionViewModel.userSessionState.collectAsStateWithLifecycle()

        val viewModel = koinScreenModel<SearchViewModel>()

        val searchUiState by viewModel.searchState.collectAsStateWithLifecycle()

        val windowSizeClass = LocalWindowSizeClass.currentOrThrow

        SearchScreenContent(
            searchFieldState = viewModel.searchFieldState,
            userSessionState = userSessionState,
            searchUiState = searchUiState,
            onSearchFieldChange = viewModel::onSearchChange,
            isMediumScreen = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium,
            isExpandedScreen = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded,
            onBlogClick = { blog ->
                rootNavigator?.push(
                    DetailBlogScreen(
                        blogId = blog.id,
                        blogData = blog.toJsonStr(),
                    ),
                )
            },
            onCreateBlogClick = {
                rootNavigator?.push(CreateBlogScreen)
            },
            onRetryClick = viewModel::retry,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreenContent(
    modifier: Modifier = Modifier,
    searchUiState: SearchUiState,
    userSessionState: UserSessionState,
    searchFieldState: String,
    isMediumScreen: Boolean = false,
    isExpandedScreen: Boolean = false,
    onSearchFieldChange: (String) -> Unit,
    onBlogClick: (Blog) -> Unit = {},
    onCreateBlogClick: () -> Unit = {},
    onRetryClick: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(Res.string.search_screen_top_bar_title),
                    )
                },
            )
        },
        floatingActionButton = {
            if (userSessionState is UserSessionState.Authenticated) {
                CreateBlogFabButton {
                    onCreateBlogClick()
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .windowInsetsPadding(WindowInsets.ime)
                    .fillMaxSize(),
        ) {
            SearchField(
                searchFieldState = searchFieldState,
                onSearchFieldChange = onSearchFieldChange,
                modifier =
                    Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))

            when (searchUiState) {
                SearchUiState.Loading -> {
                    Center(
                        modifier = Modifier.weight(1f),
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is SearchUiState.EmptyQuery, SearchUiState.Idle ->
                    CenterMessage(
                        modifier = Modifier.weight(1f),
                        message = stringResource(Res.string.search_screen_empty_query_label),
                    )

                is SearchUiState.Error ->
                    ErrorView(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .weight(1f),
                        type = ErrorViewType.GENERAL_ERROR,
                        onActionClick = onRetryClick,
                    )

                is SearchUiState.EmptyResult ->
                    ErrorView(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .weight(1f),
                        type = ErrorViewType.EMPTY_RESULT_BLOG,
                    )

                is SearchUiState.Success -> {
                    LazyVerticalGrid(
                        columns =
                            GridCells.Fixed(
                                when {
                                    isExpandedScreen -> 3
                                    isMediumScreen -> 2
                                    else -> 1
                                },
                            ),
                        modifier = Modifier.weight(1f),
                        contentPadding =
                            PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 24.dp,
                            ),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(
                            searchUiState.searchedBlogs.toImmutableList(),
                            key = { blog -> blog.id },
                        ) { blog ->
                            BlogCard(
                                blog = blog,
                                onClick = { onBlogClick(blog) },
                                modifier = Modifier.fillMaxWidth(),
                                isMediumScreen = isMediumScreen,
                                isExpandedScreen = isExpandedScreen,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CenterMessage(
    modifier: Modifier = Modifier,
    message: String,
) {
    Center(modifier = modifier) {
        Text(
            message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    searchFieldState: String,
    onSearchFieldChange: (String) -> Unit,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer),
    ) {
        TextField(
            value = searchFieldState,
            onValueChange = onSearchFieldChange,
            leadingIcon = {
                Icon(
                    painterResource(Res.drawable.ic_search_filled),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(24.dp),
                )
            },
            singleLine = true,
            placeholder = {
                Text(
                    stringResource(Res.string.search_screen_search_hint_label),
                )
            },
            colors =
                TextFieldDefaults
                    .colors()
                    .copy(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
        )
    }
}
