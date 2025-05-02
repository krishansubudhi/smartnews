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
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.krishansubudhiapps.smartnews.ui.screens.NewsScreen
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
        NavHost(navController = navController, startDestination = Screen.Topics.route, modifier = Modifier.padding(innerPadding)) {
            composable(Screen.Topics.route) {
                // NewsScreen will be the content for the Topics route
                NewsScreen()
            }
            composable(Screen.AddTopic.route) {
                // Placeholder for Add Topic Screen
                AddTopicScreen()
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
    val items = listOf(Screen.Topics, Screen.AddTopic, Screen.Search)
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // Avoid building up a large stack of destinations on the back stack as users select items
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                        // Launch the singleTop flag to avoid multiple copies of the same destination when reselecting the same item
                        launchSingleTop = true
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
