package com.krishansubudhiapps.smartnews.data.repository

import android.util.Log // Import Log
import com.krishansubudhiapps.smartnews.data.model.NewsApiResponse
import com.krishansubudhiapps.smartnews.data.remote.NewsApiService
import com.krishansubudhiapps.smartnews.BuildConfig // Import BuildConfig

class NewsRepository(private val newsApiService: NewsApiService) {

    // Get API key from BuildConfig
    private val API_KEY = BuildConfig.NEWS_API_KEY

    suspend fun getNewsArticles(category: String? = null, query: String? = null): Result<NewsApiResponse> {
        // Log the API key being used for debugging
        Log.d("NewsRepository", "Using API Key: $API_KEY")

        return try {
            val response = newsApiService.getTopHeadlines(
                apiKey = API_KEY,
                category = category,
                query = query
            )
            Result.success(response)
        } catch (e: Exception) {
            Log.e("NewsRepository", "Error fetching news", e) // Log errors
            Result.failure(e)
        }
    }
}