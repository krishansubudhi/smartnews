package com.example.newsapp.ui.news

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.newsapp.data.model.NewsArticle
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsScreen(viewModel: NewsViewModel = koinViewModel()) {
    val newsArticles by viewModel.newsArticles.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        // TODO: Implement topic selection logic later
        viewModel.fetchNews(category = "technology") // Fetch technology news initially
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        article.urlToImage?.let { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = article.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Text(
            text = article.title ?: "No Title",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        article.source?.name?.let { sourceName ->
            Text(
                text = "Source: $sourceName",
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        article.description?.let { description ->
            Text(
                text = description,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        // TODO: Add summary and reference links using Generative AI later
    }
}
