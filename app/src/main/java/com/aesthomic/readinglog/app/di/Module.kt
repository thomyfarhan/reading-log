package com.aesthomic.readinglog.app.di

import android.app.Application
import com.aesthomic.readinglog.database.BookDao
import com.aesthomic.readinglog.database.ReadDao
import com.aesthomic.readinglog.database.ReadingLogDatabase
import com.aesthomic.readinglog.readbook.ReadBookViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (key: Long) -> ReadBookViewModel(key, get(), get()) }
}

val databaseModule = module {

    fun provideDatabase(app: Application): ReadingLogDatabase {
        return ReadingLogDatabase.getInstance(app)
    }

    fun provideReadDao(database: ReadingLogDatabase): ReadDao {
        return database.readDao
    }

    fun provideBookDao(database: ReadingLogDatabase): BookDao {
        return database.bookDao
    }

    single { provideDatabase(androidApplication()) }
    single { provideReadDao(get()) }
    single { provideBookDao(get()) }
}
