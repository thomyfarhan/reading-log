package com.aesthomic.readinglog.readdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aesthomic.readinglog.database.ReadDao
import java.lang.IllegalArgumentException

class ReadDetailViewModelFactory(
    private val readKey: Long,
    private val database: ReadDao): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReadDetailViewModel::class.java)) {
            return ReadDetailViewModel(readKey, database) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }

}