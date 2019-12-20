package com.aesthomic.readinglog.booklist

import androidx.lifecycle.ViewModel
import com.aesthomic.readinglog.database.BookDao

class BookListViewModel(
    private val dbBook: BookDao): ViewModel() {

    val books = dbBook.getAll()


}
