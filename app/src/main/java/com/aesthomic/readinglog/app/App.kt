package com.aesthomic.readinglog.app

import android.app.Application
import com.aesthomic.readinglog.app.di.databaseModule
import com.aesthomic.readinglog.app.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)

            val listModules = listOf(
                viewModelModule,
                databaseModule
            )
            modules(listModules)
        }
    }


}
