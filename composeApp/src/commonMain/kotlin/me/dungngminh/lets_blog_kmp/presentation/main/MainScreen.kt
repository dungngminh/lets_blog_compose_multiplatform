package me.dungngminh.lets_blog_kmp.presentation.main

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import me.dungngminh.lets_blog_kmp.navigation.MainScreenDestination
import me.dungngminh.lets_blog_kmp.navigation.Route
import me.dungngminh.lets_blog_kmp.presentation.main.favorite.FavoriteScreen
import me.dungngminh.lets_blog_kmp.presentation.main.home.HomeScreen
import me.dungngminh.lets_blog_kmp.presentation.main.profile.ProfileScreen
import me.dungngminh.lets_blog_kmp.presentation.main.search.SearchScreen
import org.jetbrains.compose.resources.painterResource

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                MainScreenDestination.entries
                    .forEach { destination ->
                        val selected =
                            currentDestination?.hierarchy?.any {
                                it.hasRoute(
                                    destination.route::class.toString(),
                                    arguments = null,
                                )
                            } ==
                                true
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    painter =
                                        if (selected) {
                                            painterResource(
                                                destination.selectedIcon,
                                            )
                                        } else {
                                            painterResource(destination.icon)
                                        },
                                    contentDescription = null,
                                )
                            },
                            selected = selected,
                            onClick = {
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        )
                    }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Home,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<Route.Home> {
                HomeScreen()
            }
            composable<Route.Search> {
                SearchScreen()
            }
            composable<Route.Favorite> {
                FavoriteScreen()
            }
            composable<Route.Profile> {
                ProfileScreen()
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}
