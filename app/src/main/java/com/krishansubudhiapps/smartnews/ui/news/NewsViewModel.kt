package com.krishansubudhiapps.smartnews.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krishansubudhiapps.smartnews.data.model.NewsArticle
import com.krishansubudhiapps.smartnews.data.model.Source // Import Source
import com.krishansubudhiapps.smartnews.data.model.NewsDataResponse // Import NewsDataResponse
import com.krishansubudhiapps.smartnews.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private val _newsArticles = MutableStateFlow<List<NewsArticle>>(emptyList())
    val newsArticles: StateFlow<List<NewsArticle>> = _newsArticles

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Function to map NewsDataArticle to NewsArticle
    private fun mapNewsDataArticleToNewsArticle(newsDataArticle: com.krishansubudhiapps.smartnews.data.model.NewsDataArticle): NewsArticle {
        // Add logging for link and sourceName
        Log.d("NewsViewModel", "Mapping article: Title = ${newsDataArticle.title}, Link = ${newsDataArticle.link}, Source Name = ${newsDataArticle.sourceName}")

        return NewsArticle(
            title = newsDataArticle.title,
            description = newsDataArticle.description,
            content = newsDataArticle.content,
            url = newsDataArticle.link ?: "", // Use link as url
            urlToImage = newsDataArticle.imageUrl,
            publishedAt = newsDataArticle.pubDate,
            source = Source(id = newsDataArticle.sourceId, name = newsDataArticle.sourceName), // Correctly reference Source
            author = newsDataArticle.creator?.firstOrNull() // Map first creator as author
            // TODO: Map AI summary and other relevant fields if needed
        )
    }

    fun fetchNews(category: String? = null, country: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result: Result<NewsDataResponse> = newsRepository.getNewsArticles(category = category, country = country) // Explicitly define type
            result.onSuccess { newsDataResponse -> // newsDataResponse is already of type NewsDataResponse
                // Add logging for the number of articles received
                Log.d("NewsViewModel", "Received ${newsDataResponse.results.size} articles from API.")
                // Map NewsDataArticle list to NewsArticle list
                _newsArticles.value = newsDataResponse.results.map { mapNewsDataArticleToNewsArticle(it) }
            }.onFailure {
                _errorMessage.value = it.message
                Log.e("NewsViewModel", "Error fetching news", it)
            }
            _isLoading.value = false
        }
    }
}