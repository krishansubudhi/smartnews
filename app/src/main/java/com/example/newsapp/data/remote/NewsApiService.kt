package com.example.newsapp.data.remote

import com.example.newsapp.data.model.NewsApiResponse
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