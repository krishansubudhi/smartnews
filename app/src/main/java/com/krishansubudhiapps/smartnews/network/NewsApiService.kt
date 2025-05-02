package com.krishansubudhiapps.smartnews.network

import com.krishansubudhiapps.smartnews.data.NewsApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "in",
        @Query("apiKey") apiKey: String
    ): Response<NewsApiResponse>
}