package com.krishansubudhiapps.smartnews.data

import com.krishansubudhiapps.smartnews.network.NewsApiService

class NewsRepository(private val newsApiService: NewsApiService) {
    suspend fun getTopHeadlines(country: String, apiKey: String): NewsApiResponse? {
        return try {
            val response = newsApiService.getTopHeadlines(country, apiKey)
            if (response.isSuccessful) {
                response.body()
            } else {
                // Handle error appropriately
                null
            }
        } catch (e: Exception) {
            // Handle exception appropriately
            null
        }
    }
}