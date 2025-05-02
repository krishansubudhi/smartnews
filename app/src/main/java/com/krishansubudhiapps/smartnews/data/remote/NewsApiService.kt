package com.krishansubudhiapps.smartnews.data.remote

import com.krishansubudhiapps.smartnews.data.model.NewsApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("apiKey") apiKey: String,
        @Query("country") country: String = "us",
        @Query("category") category: String? = null,
        @Query("q") query: String? = null
    ): NewsApiResponse
}