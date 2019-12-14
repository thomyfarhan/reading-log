package com.aesthomic.readinglog.read

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aesthomic.readinglog.database.ReadDao
import java.lang.IllegalArgumentException

class ReadViewModelFactory(
    private val database: ReadDao,
    private val application: Application): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReadViewModel::class.java)) {
            return ReadViewModel(database, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}