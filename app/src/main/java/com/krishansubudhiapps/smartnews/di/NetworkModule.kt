package com.krishansubudhiapps.smartnews.di

import com.krishansubudhiapps.smartnews.data.remote.NewsApiService
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }
    single { provideNewsApiService(get()) }
}

fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .build()
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    // TODO: Replace with your News API base URL
    return Retrofit.Builder()
        .baseUrl("https://newsapi.org/v2/") // Example base URL
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideNewsApiService(retrofit: Retrofit): NewsApiService {
    return retrofit.create(NewsApiService::class.java)
}