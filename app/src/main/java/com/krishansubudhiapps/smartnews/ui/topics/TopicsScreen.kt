package com.krishansubudhiapps.smartnews.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TopicsScreen(onTopicSelected: (String) -> Unit) {
    val topics = listOf(
        "Technology",
        "Sports",
        "Business",
        "Health",
        "Science",
        "Entertainment"
    )
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Select a Topic",
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn {
            items(topics) { topic ->
                Text(
                    text = topic,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTopicSelected(topic.lowercase()) }
                        .padding(16.dp)
                )
                Divider()
            }
        }
    }
}

@Preview
@Composable
fun TopicsScreenPreview() {
    TopicsScreen(onTopicSelected = {})
}

