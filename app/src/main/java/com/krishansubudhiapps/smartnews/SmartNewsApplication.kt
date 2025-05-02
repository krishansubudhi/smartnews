package com.krishansubudhiapps.smartnews

import android.app.Application
import com.krishansubudhiapps.smartnews.di.appModule
import com.krishansubudhiapps.smartnews.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SmartNewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SmartNewsApplication)
            modules(appModule, networkModule)
        }
    }
}