package com.aesthomic.readinglog.book

import androidx.lifecycle.ViewModel
import com.aesthomic.readinglog.database.BookDao

class BookViewModel(private val dbBook: BookDao): ViewModel() {

    val books = dbBook.getAllByLastAccessed()
}
