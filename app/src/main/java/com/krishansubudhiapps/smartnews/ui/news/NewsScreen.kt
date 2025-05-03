package com.krishansubudhiapps.smartnews.ui.news

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import coil.compose.AsyncImage
import com.krishansubudhiapps.smartnews.data.model.NewsArticle
import java.time.format.DateTimeFormatter
import java.util.Locale
import org.koin.androidx.compose.koinViewModel
import java.time.ZonedDateTime
import java.time.LocalDateTime // Import LocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsScreen(viewModel: NewsViewModel = koinViewModel()) {
    val newsArticles by viewModel.newsArticles.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        // TODO: Implement topic selection logic later
        // Fetch technology news from the US to test a simpler query
        viewModel.fetchNews(category = "technology", country = "us")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            errorMessage != null -> {
                Text(
                    text = "Error: ${errorMessage}",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
            newsArticles.isNotEmpty() -> {
                val pagerState = rememberPagerState(pageCount = { newsArticles.size })
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    page ->
                    NewsArticlePage(article = newsArticles[page])
                }
            }
            else -> {
                Text(
                    text = "No news articles found.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun NewsArticlePage(article: NewsArticle) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(scrollState)
    ) {
        article.urlToImage?.let { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = article.title,
                modifier = Modifier
                    .fillMaxWidth().height(200.dp).clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Title
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = article.title ?: "No Title",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Published At
        article.publishedAt?.let { publishedAt ->
            Text(
                text = publishedAt,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Description
        article.description?.let { description ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.LightGray.copy(alpha = 0.5f), shape = RoundedCornerShape(10.dp))
                    .padding(8.dp)
            ){
                Text(
                    text = "TLDR",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 14.sp,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        article.content?.let { newsContent ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = newsContent,
                fontSize = 14.sp,
            )
        }
        // Source Link (using domain from URL)
        article.url?.let { articleUrl ->
            // Extract domain from URL
            val domain = try {
                val uri = Uri.parse(articleUrl)
                uri.host ?: "Source Link" // Use host or default text
            } catch (e: Exception) {
                "Source Link" // Default text on error
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // Handle cases where the URL is malformed or cannot be opened
                            // You could show a Toast message to the user here
                            println("Error opening URL: ${e.message}")
                        }
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = domain, // Display domain name
                    fontSize = 12.sp,
                    color = Color.Blue, // Keep blue color for link appearance
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

        }
        // TODO: Add summary and reference links using Generative AI later
    }
}
