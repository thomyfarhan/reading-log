package com.aesthomic.readinglog.readbook

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aesthomic.readinglog.database.ReadDao
import java.lang.IllegalArgumentException

class ReadBookViewModelFactory(
    private val readKey: Long,
    private val database: ReadDao,
    private val application: Application): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ReadBookViewModel::class.java)) {
            return ReadBookViewModel(readKey, database, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}