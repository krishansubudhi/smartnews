package com.krishansubudhiapps.smartnews.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krishansubudhiapps.smartnews.data.model.NewsArticle
import com.krishansubudhiapps.smartnews.data.model.Source
import com.krishansubudhiapps.smartnews.data.model.NewsDataResponse
import com.krishansubudhiapps.smartnews.data.repository.NewsRepository
import com.krishansubudhiapps.smartnews.data.repository.TopicPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import com.krishansubudhiapps.smartnews.data.model.Topic
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.collect


class NewsViewModel(
    private val newsRepository: NewsRepository,
    private val topicPreferencesRepository: TopicPreferencesRepository
) : ViewModel() {

    private val _newsArticles = MutableStateFlow<List<NewsArticle>>(emptyList())
    val newsArticles: StateFlow<List<NewsArticle>> = _newsArticles

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _availableTopics = MutableStateFlow<List<Topic>>(
        listOf(
            Topic(name = "Business"),
            Topic(name = "Entertainment"),
            Topic(name = "Health"),
            Topic(name = "Science"),
            Topic(name = "Sports"),
            Topic(name = "Technology"),
            Topic(name = "World"),
            Topic(name = "Indian Politics", country = "in"),
            Topic(name = "US Headlines", country = "us"),
            Topic(name = "AI Agents"),
            Topic(name = "Software Engineering Agents")
        )
    )
    val availableTopics: StateFlow<List<Topic>> = _availableTopics

    private val _selectedTopics = MutableStateFlow<List<Topic>>(emptyList())
    val selectedTopics: StateFlow<List<Topic>> = _selectedTopics

    init {
        viewModelScope.launch {
            // Load selected topics from DataStore on ViewModel initialization
            topicPreferencesRepository.selectedTopics.collect { topics ->
                _selectedTopics.value = topics
                // Fetch news after topics are loaded
                fetchNewsForSelectedTopics()
            }
        }
    }

    fun fetchNewsForSelectedTopics() {
        Log.d("NewsViewModel", "fetchNewsForSelectedTopics called with topics: ${_selectedTopics.value.map { it.name }}")
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _newsArticles.value = emptyList()

            val articles = mutableListOf<NewsArticle>()
            var errorOccurred = false

            if (_selectedTopics.value.isEmpty()) {
                 _errorMessage.value = "No topics selected."
            } else {
                for (topic in _selectedTopics.value) {
                    val result = newsRepository.getNewsArticles(query = topic.name, country = topic.country)

                    result.onSuccess { newsDataResponse ->
                        Log.d("NewsViewModel", "Received ${newsDataResponse.results.size} articles for topic: ${topic.name}")
                        articles.addAll(newsDataResponse.results.map { mapNewsDataArticleToNewsArticle(it) })
                    }.onFailure {
                        _errorMessage.value = "Error fetching news for topic ${topic.name}: ${it.message}"
                        Log.e("NewsViewModel", "Error fetching news for topic ${topic.name}", it)
                        errorOccurred = true
                    }
                }
                _newsArticles.value = articles.distinctBy { it.url ?: "" }

                if (errorOccurred && _errorMessage.value == null) {
                    _errorMessage.value = "Error fetching news for one or more topics."
                } else if (!errorOccurred && articles.isEmpty()) {
                    _errorMessage.value = "No news articles found for the selected topics."
                }
            }

            _isLoading.value = false
        }
    }

    private fun mapNewsDataArticleToNewsArticle(newsDataArticle: com.krishansubudhiapps.smartnews.data.model.NewsDataArticle): NewsArticle {
        Log.d("NewsViewModel", "Mapping article details:")
        Log.d("NewsViewModel", "  Title: ${newsDataArticle.title}")
        Log.d("NewsViewModel", "  Description: ${newsDataArticle.description}")
        Log.d("NewsViewModel", "  Content: ${newsDataArticle.content}")
        Log.d("NewsViewModel", "  Published At: ${newsDataArticle.pubDate}")
        Log.d("NewsViewModel", "  Image URL: ${newsDataArticle.imageUrl}")
        Log.d("NewsViewModel", "  Link (URL): ${newsDataArticle.link}")
        Log.d("NewsViewModel", "  Source Name: ${newsDataArticle.sourceName}")
        Log.d("NewsViewModel", "  Article ID: ${newsDataArticle.articleId}")

        return NewsArticle(
            title = newsDataArticle.title,
            description = newsDataArticle.description,
            content = newsDataArticle.content,
            url = newsDataArticle.link ?: "",
            urlToImage = newsDataArticle.imageUrl,
            publishedAt = newsDataArticle.pubDate,
            source = Source(id = newsDataArticle.sourceId, name = newsDataArticle.sourceName),
            author = newsDataArticle.creator?.firstOrNull()
        )
    }

    fun updateSelectedTopics(topics: List<Topic>) {
        Log.d("NewsViewModel", "Updating selected topics to: ${topics.map { it.name }}")
        _selectedTopics.value = topics
        viewModelScope.launch {
             topicPreferencesRepository.saveSelectedTopics(topics)
        }
        // News fetching is now triggered by the DataStore flow collection
    }
}