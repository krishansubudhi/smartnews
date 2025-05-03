package com.krishansubudhiapps.smartnews

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Settings

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object NewsFeed : Screen("news_feed", "News", Icons.Default.List)
    object TopicSelection : Screen("topic_selection", "Topics", Icons.Default.Settings) // Topic Selection Screen
    // Removed AddTopic
    object Search : Screen("search", "Search", Icons.Default.Search)
}