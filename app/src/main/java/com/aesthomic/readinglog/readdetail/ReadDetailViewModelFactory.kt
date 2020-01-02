package com.aesthomic.readinglog.readdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aesthomic.readinglog.database.BookDao
import com.aesthomic.readinglog.database.ReadDao
import java.lang.IllegalArgumentException

class ReadDetailViewModelFactory(
    private val readKey: Long,
    private val dbRead: ReadDao,
    private val dbBook: BookDao): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReadDetailViewModel::class.java)) {
            return ReadDetailViewModel(readKey, dbRead, dbBook) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }

}