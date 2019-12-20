package com.aesthomic.readinglog.booklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aesthomic.readinglog.database.BookDao
import java.lang.IllegalArgumentException

class BookListViewModelFactory(
    private val dbBook: BookDao): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BookListViewModel::class.java)) {
            return BookListViewModel(dbBook) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}