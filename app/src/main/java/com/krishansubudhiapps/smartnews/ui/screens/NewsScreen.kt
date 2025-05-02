package com.krishansubudhiapps.smartnews.ui.screens

import android.R
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.krishansubudhiapps.smartnews.ui.components.NewsArticle
import com.krishansubudhiapps.smartnews.ui.components.NewsArticleCard
import androidx.compose.foundation.ExperimentalFoundationApi

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsScreen() {
    val newsItems = listOf(
        NewsArticle(
            imageUrl = R.drawable.ic_menu_gallery, // Placeholder drawable
            title = "Scientists Discover New Species of Butterfly in Rainforest",
            summary = "A team of researchers exploring a remote part of the Amazon rainforest has discovered a stunning new species of butterfly with iridescent blue wings. This discovery highlights the importance of preserving biodiversity in these vulnerable ecosystems.",
            link = "https://www.example.com/science/newbutterfly"
        ),
        NewsArticle(
            imageUrl = R.drawable.ic_menu_gallery, // Using a known placeholder drawable
            title = "Local Cafe Wins 'Best Brew' Award",
            summary = "'The Daily Grind' cafe downtown has been awarded the coveted 'Best Brew' award by the city's coffee enthusiasts. Their unique blend and cozy atmosphere were cited as key factors.",
            link = "https://www.example.com/local/bestbrew"
        ),
        NewsArticle(
            imageUrl = R.drawable.ic_menu_gallery, // Using a known placeholder drawable
            title = "New Study Shows Benefits of Reading Daily",
            summary = "According to a study from the University of Reading, incorporating daily reading into your routine can significantly improve cognitive function and reduce stress levels.",
            link = "https://www.example.com/health/readingbenefits"
        )
    )

    val pagerState = rememberPagerState(pageCount = { newsItems.size })

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
    ) {
        page ->
        NewsArticleCard(
            article = newsItems[page],
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNewsScreen() {
    NewsScreen()
}
