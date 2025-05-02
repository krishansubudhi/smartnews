package com.krishansubudhiapps.smartnews

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Topics : Screen("topics", "Topics", Icons.Default.List)
    object AddTopic : Screen("add_topic", "Add Topic", Icons.Default.Add)
    object Search : Screen("search", "Search", Icons.Default.Search)
}