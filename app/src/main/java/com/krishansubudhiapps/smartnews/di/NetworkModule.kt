package com.krishansubudhiapps.smartnews.di

import com.krishansubudhiapps.smartnews.network.NewsDataApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor // Import HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.krishansubudhiapps.smartnews.BuildConfig // Import BuildConfig


val networkModule = module {

    single { provideOkHttpClient() }
    single { provideMoshi() }
    single { provideRetrofit(get(), get()) }
    single { provideNewsDataApiService(get()) }
}

fun provideOkHttpClient(): OkHttpClient {
    // Create a logging interceptor
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        // Set the level to BODY to log request and response headers and bodies
        level = HttpLoggingInterceptor.Level.BODY
    }

    return OkHttpClient.Builder()
        // Add the logging interceptor
        .addInterceptor(loggingInterceptor)
        .build()
}

fun provideMoshi(): Moshi {
    return Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
}

fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://newsdata.io/api/1/") // NewsData.io base URL
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
}

fun provideNewsDataApiService(retrofit: Retrofit): NewsDataApiService {
    return retrofit.create(NewsDataApiService::class.java)
}