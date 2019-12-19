package com.aesthomic.readinglog.readbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aesthomic.readinglog.database.BookDao
import com.aesthomic.readinglog.database.ReadDao
import java.lang.IllegalArgumentException

class ReadBookViewModelFactory(
    private val readKey: Long,
    private val dbRead: ReadDao,
    private val dbBook: BookDao): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ReadBookViewModel::class.java)) {
            return ReadBookViewModel(readKey, dbRead, dbBook) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}