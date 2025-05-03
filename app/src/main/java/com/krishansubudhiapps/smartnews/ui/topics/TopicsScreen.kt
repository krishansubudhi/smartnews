package com.krishansubudhiapps.smartnews.ui.topics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.krishansubudhiapps.smartnews.data.model.Topic
import com.krishansubudhiapps.smartnews.ui.news.NewsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicsScreen(
    navController: NavHostController,
    viewModel: NewsViewModel = koinViewModel()
) {
    val availableTopics by viewModel.availableTopics.collectAsState()
    val selectedTopics by viewModel.selectedTopics.collectAsState()

    var currentSelectedTopics by remember { mutableStateOf(selectedTopics) }

    LaunchedEffect(selectedTopics) {
        currentSelectedTopics = selectedTopics
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Topics") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Button(onClick = {
                        viewModel.updateSelectedTopics(currentSelectedTopics)
                        navController.popBackStack()
                    }) {
                        Text("Done")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
        ) {
            Text(
                text = "Selected: ${currentSelectedTopics.size}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp)
            )
            Text(
                text = "Select your topics of interest:",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
            )
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(availableTopics) { topic ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                currentSelectedTopics = if (currentSelectedTopics.contains(topic)) {
                                    currentSelectedTopics - topic
                                } else {
                                    currentSelectedTopics + topic
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = topic.name,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Checkbox(
                            checked = currentSelectedTopics.contains(topic),
                            onCheckedChange = { isChecked: Boolean ->
                                currentSelectedTopics = if (isChecked) {
                                    currentSelectedTopics + topic
                                } else {
                                    currentSelectedTopics - topic
                                }
                            }
                        )
                    }
                    Divider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}