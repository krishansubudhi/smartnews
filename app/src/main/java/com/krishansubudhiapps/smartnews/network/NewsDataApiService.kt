package com.krishansubudhiapps.smartnews.network

import com.krishansubudhiapps.smartnews.data.model.NewsDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsDataApiService {

    @GET("news")
    suspend fun getNews(
        @Query("apikey") apiKey: String,
        @Query("category") category: String? = null,
        @Query("country") country: String? = null,
        @Query("language") language: String = "en"
    ): Response<NewsDataResponse>
}
