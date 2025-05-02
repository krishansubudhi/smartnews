package com.krishansubudhiapps.smartnews.ui.components

import android.R
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class NewsArticle(
    val imageUrl: Int, // Using Int for drawable resource for dummy data
    val title: String,
    val summary: String,
    val link: String
)

@Composable
fun NewsArticleCard(article: NewsArticle, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Card(
        modifier = modifier // Apply the passed modifier here
            .fillMaxSize()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Use theme surface color for card background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Display Image using painterResource
            if (article.imageUrl != -1) { // Check if a valid drawable resource is provided
                Image(
                    painter = painterResource(id = article.imageUrl),
                    contentDescription = "News Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant), // Use a theme color for placeholder background
                    contentScale = ContentScale.Crop // Crop the image to fill the bounds
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface // Use theme onSurface color for title
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = article.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant // Use a slightly different theme color for summary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = article.link,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary, // Use theme primary color for link
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.link))
                    context.startActivity(intent)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNewsArticleCard() {
    val dummyArticle = NewsArticle(
        imageUrl = R.drawable.ic_menu_gallery, // Using a built-in drawable for now
        title = "Dummy News Headline",
        summary = "This is a summary of the dummy news article. It provides a brief overview of the content for the user to quickly understand the topic.",
        link = "https://www.example.com/dummynews"
    )
    NewsArticleCard(article = dummyArticle)
}
