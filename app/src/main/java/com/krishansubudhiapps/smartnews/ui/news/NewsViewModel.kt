package com.krishansubudhiapps.smartnews.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krishansubudhiapps.smartnews.BuildConfig
import com.krishansubudhiapps.smartnews.data.Article
import com.krishansubudhiapps.smartnews.data.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles

    // TODO: Manage API Key securely, e.g., using BuildConfig
    private val apiKey = BuildConfig.NEWS_API_KEY

    init {
        fetchNews()
    }

    fun fetchNews(country: String = "us") {
        viewModelScope.launch {
            val response = newsRepository.getTopHeadlines(country, apiKey)
            response?.articles?.let {
                _articles.value = it
            }
        }
    }
}