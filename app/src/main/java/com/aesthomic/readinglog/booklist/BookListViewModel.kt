package com.aesthomic.readinglog.booklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aesthomic.readinglog.database.BookDao

class BookListViewModel(
    private val dbBook: BookDao): ViewModel() {

    val books = dbBook.getAllByLastAccessed()

    private val _navigateToReadBook = MutableLiveData<Long>()
    val navigateToReadBook: LiveData<Long>
        get() = _navigateToReadBook

    fun onBookClicked(key: Long) {
        _navigateToReadBook.value = key
    }

    fun navigateReadBookDone() {
        _navigateToReadBook.value = null
    }
}
