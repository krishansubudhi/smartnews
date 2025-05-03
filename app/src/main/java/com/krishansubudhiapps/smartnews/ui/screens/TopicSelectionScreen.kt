package com.krishansubudhiapps.smartnews.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons // Import Icons
import androidx.compose.material.icons.filled.ArrowBack // Import ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon // Import Icon
import androidx.compose.material3.IconButton // Import IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton // Import TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.krishansubudhiapps.smartnews.data.model.Topic
import com.krishansubudhiapps.smartnews.ui.news.NewsViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicSelectionScreen(navController: NavHostController, viewModel: NewsViewModel = koinViewModel()) {
    val allTopics by viewModel.availableTopics.collectAsState()
    val selectedTopics by viewModel.selectedTopics.collectAsState()

    // Determine if the Done button should be enabled (e.g., at least 1 topic selected for now)
    val isDoneButtonEnabled = selectedTopics.isNotEmpty() // Using isNotEmpty for now, can change to >= 3 later

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Topics") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // Back arrow
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            // Explicitly update ViewModel with current selection and navigate back
                            viewModel.updateSelectedTopics(selectedTopics)
                            navController.popBackStack()
                        },
                        enabled = isDoneButtonEnabled // Enable/disable Done button
                    ) {
                        Text("Done")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            // Display selected topic count
            Text(
                text = "Selected: ${selectedTopics.size}",
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(text = "Select your topics of interest:")
            LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                items(allTopics) { topic ->
                    val isSelected = selectedTopics.contains(topic)
                    ListItem(
                        headlineContent = { Text(topic.name) },
                        trailingContent = {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = { isChecked ->
                                    val updatedSelectedTopics = if (isChecked) {
                                        selectedTopics + topic
                                    } else {
                                        selectedTopics - topic
                                    }
                                    // Update ViewModel immediately on checkbox change
                                    viewModel.updateSelectedTopics(updatedSelectedTopics)
                                    Log.d("TopicSelectionScreen", "Checkbox changed. Updating selected topics to: ${updatedSelectedTopics.map { it.name }}") // Log update
                                }
                            )
                        },
                        modifier = Modifier.clickable {
                             val currentlySelected = selectedTopics.contains(topic)
                             val updatedSelectedTopics = if (!currentlySelected) {
                                 selectedTopics + topic
                             } else {
                                 selectedTopics - topic
                             }
                             // Update ViewModel immediately on list item click
                             viewModel.updateSelectedTopics(updatedSelectedTopics)
                             Log.d("TopicSelectionScreen", "ListItem clicked. Updating selected topics to: ${updatedSelectedTopics.map { it.name }}") // Log update
                        }
                    )
                }
            }
        }
    }
}