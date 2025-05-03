package com.krishansubudhiapps.smartnews.network

import com.krishansubudhiapps.smartnews.data.model.NewsDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsDataApiService {

    @GET("latest")
    suspend fun getNews(
        @Query("apikey") apiKey: String,
        @Query("q") query: String? = null,
        @Query("country") country: String? = null, // Added country parameter back
        @Query("language") language: String = "en"
    ): Response<NewsDataResponse>
}