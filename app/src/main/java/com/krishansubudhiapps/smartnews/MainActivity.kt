package com.krishansubudhiapps.smartnews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable // Ensure composable is imported
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.krishansubudhiapps.smartnews.ui.news.NewsScreen
import com.krishansubudhiapps.smartnews.ui.screens.TopicSelectionScreen // Import TopicSelectionScreen
import com.krishansubudhiapps.smartnews.ui.theme.SmartNewsTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartNewsTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SmartNews") }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = Screen.NewsFeed.route, modifier = Modifier.padding(innerPadding)) {
            composable(Screen.NewsFeed.route) {
                NewsScreen(navController = navController) // Pass navController to NewsScreen
            }
            composable(Screen.TopicSelection.route) {
                TopicSelectionScreen(navController = navController) // Pass navController
            }
            composable(Screen.Search.route) {
                // Placeholder for Search Screen
                SearchScreen()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    // Updated list for bottom nav: NewsFeed, TopicSelection, Search
    val items = listOf(Screen.NewsFeed, Screen.TopicSelection, Screen.Search) // Use NewsFeed and TopicSelection
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    if (navController.currentDestination?.route != screen.route) { // Prevent navigating to the same destination
                        navController.navigate(screen.route) {
                            // Removed popUpTo and launchSingleTop for simpler navigation
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun AddTopicScreen() {
    // Simple placeholder screen for Add Topic
    Text("Add Topic Screen")
}

@Composable
fun SearchScreen() {
    // Simple placeholder screen for Search
    Text("Search Screen")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SmartNewsTheme {
        MainScreen()
    }
}
