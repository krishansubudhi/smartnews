package com.krishansubudhiapps.smartnews.di

import com.example.newsapp.data.repository.NewsRepository // Corrected import
import com.example.newsapp.ui.news.NewsViewModel // Corrected import
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Provides the NewsRepository instance, injecting the NewsApiService provided by NetworkModule
    single { NewsRepository(get()) }

    // Provides the NewsViewModel instance, injecting the NewsRepository
    viewModel { NewsViewModel(get()) }
}