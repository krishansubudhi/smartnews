package com.krishansubudhiapps.smartnews.data.repository

import com.krishansubudhiapps.smartnews.data.model.NewsDataResponse
import com.krishansubudhiapps.smartnews.network.NewsDataApiService
import com.krishansubudhiapps.smartnews.BuildConfig
import android.util.Log
import java.util.concurrent.TimeUnit // Import TimeUnit for time calculations
import kotlin.collections.Map // Explicitly import Map


// Data class to hold cached news response and timestamp
data class CachedNews(
    val response: NewsDataResponse,
    val timestamp: Long
)

class NewsRepository(private val newsDataApiService: NewsDataApiService) {

    // In-memory cache for news responses. Key: "query_country", Value: CachedNews
    private val newsCache: MutableMap<String, CachedNews> = mutableMapOf()
    private val CACHE_TTL_MINUTES = 5 // Cache time-to-live in minutes

    suspend fun getNewsArticles(query: String? = null, country: String? = null): Result<NewsDataResponse> {
        val cacheKey = "${query ?: ""}_${country ?: ""}" // Create a unique key for cache

        // Check if a valid cached response exists
        val cachedEntry = newsCache[cacheKey]
        val currentTime = System.currentTimeMillis()

        if (cachedEntry != null && currentTime - cachedEntry.timestamp < TimeUnit.MINUTES.toMillis(CACHE_TTL_MINUTES.toLong())) {
            Log.d("NewsRepository", "Returning cached data for key: $cacheKey")
            return Result.success(cachedEntry.response)
        }

        // If no valid cached response, fetch from API
        return try {
            val apiKey = BuildConfig.NEWS_API_KEY
            if (apiKey.isEmpty()) {
                return Result.failure(Exception("NewsData.io API key not found in local.properties"))
            }

            val response = newsDataApiService.getNews(apiKey = apiKey, query = query, country = country)

            if (response.isSuccessful && response.body() != null) {
                val newsDataResponse = response.body()!!
                // Cache the new response
                newsCache[cacheKey] = CachedNews(newsDataResponse, currentTime)
                Log.d("NewsRepository", "Cached new data for key: $cacheKey")
                Result.success(newsDataResponse)
            } else {
                Result.failure(Exception("Error fetching news: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("NewsRepository", "Exception during API call for key: $cacheKey", e)
            Result.failure(e)
        }
    }
}