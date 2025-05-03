package com.krishansubudhiapps.smartnews.ui.news

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button // Import Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.Card // Import Card
import androidx.compose.material3.CardDefaults // Import CardDefaults
import androidx.compose.material3.MaterialTheme // Import MaterialTheme
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
import androidx.compose.ui.unit.em
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavHostController // Import NavHostController
import coil.compose.AsyncImage
import com.krishansubudhiapps.smartnews.data.model.NewsArticle
import java.time.format.DateTimeFormatter
import java.util.Locale
import org.koin.androidx.compose.koinViewModel
import java.time.ZonedDateTime
import java.time.LocalDateTime // Import LocalDateTime
import com.krishansubudhiapps.smartnews.Screen // Import Screen


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsScreen(navController: NavHostController, viewModel: NewsViewModel = koinViewModel()) { // Added navController parameter
    val newsArticles by viewModel.newsArticles.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()



    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray) // Set main background to LightGray
    ) {
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

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Padding around the card
        colors = CardDefaults.cardColors(containerColor = Color.Gray), // Card background is Gray
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Elevation
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp) // Padding inside the card
        ) {
            article.urlToImage?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = article.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Title
            Text(
                text = article.title ?: "No Title",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White, // Text color is White
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Published At
            article.publishedAt?.let { publishedAt ->
                Text(
                    text = publishedAt,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f), // Text color is White with transparency
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Description
            article.description?.let { description ->
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.White, // Text color is White
                    modifier = Modifier.padding(top = 8.dp)
                )
            }


            // Source Link (using domain from URL)
            article.url?.let { articleUrl ->
                Spacer(modifier = Modifier.height(8.dp))
                val domain = try {
                    val uri = Uri.parse(articleUrl)
                    uri.host ?: "Source Link"
                } catch (e: Exception) {
                    "Source Link"
                }

                Text(
                    text = domain,
                    fontSize = 12.sp,
                    color = Color.Red, // Link color is Red
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clickable {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                println("Error opening URL: ${e.message}")
                            }
                        }
                )
            }
        }
    }
}