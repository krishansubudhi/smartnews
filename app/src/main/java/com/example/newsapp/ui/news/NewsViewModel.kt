package com.example.newsapp.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.model.NewsArticle
import com.example.newsapp.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private val _newsArticles = MutableStateFlow<List<NewsArticle>>(emptyList())
    val newsArticles: StateFlow<List<NewsArticle>> = _newsArticles

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchNews(category: String? = null, query: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = newsRepository.getNewsArticles(category, query)
            result.onSuccess {
                _newsArticles.value = it.articles
            }.onFailure {
                _errorMessage.value = it.message
            }
            _isLoading.value = false
        }
    }
}
