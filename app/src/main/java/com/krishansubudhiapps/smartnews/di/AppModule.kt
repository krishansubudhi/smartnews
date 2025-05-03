package com.krishansubudhiapps.smartnews.di

import com.krishansubudhiapps.smartnews.data.repository.NewsRepository
import com.krishansubudhiapps.smartnews.data.repository.TopicPreferencesRepository // Import TopicPreferencesRepository
import com.krishansubudhiapps.smartnews.ui.news.NewsViewModel
import org.koin.android.ext.koin.androidContext // Import androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Provides the NewsRepository instance, injecting the NewsApiService provided by NetworkModule
    single { NewsRepository(get()) }

    // Provides the TopicPreferencesRepository instance, injecting the Android Context
    single { TopicPreferencesRepository(androidContext()) }

    // Provides the NewsViewModel instance, injecting the NewsRepository and TopicPreferencesRepository
    viewModel { NewsViewModel(get(), get()) }
}