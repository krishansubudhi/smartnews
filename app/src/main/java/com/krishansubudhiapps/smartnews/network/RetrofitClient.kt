package com.krishansubudhiapps.smartnews.data

import com.krishansubudhiapps.smartnews.data.model.NewsDataResponse
import com.krishansubudhiapps.smartnews.network.NewsDataApiService
import com.krishansubudhiapps.smartnews.BuildConfig

class NewsRepository(private val newsDataApiService: NewsDataApiService) {

    suspend fun getNewsArticles(category: String? = null, country: String? = null): Result<NewsDataResponse> {
        return try {
            val apiKey = BuildConfig.NEWS_API_KEY // Get API key from BuildConfig
            if (apiKey.isEmpty()) {
                return Result.failure(Exception("NewsData.io API key not found in local.properties"))
            }
            val response = newsDataApiService.getNews(apiKey = apiKey, category = category, country = country)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error fetching news: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}