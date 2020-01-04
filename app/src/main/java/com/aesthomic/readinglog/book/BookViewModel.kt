package com.aesthomic.readinglog.book

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.aesthomic.readinglog.database.Book
import com.aesthomic.readinglog.database.BookDao
import com.aesthomic.readinglog.util.setVisibilityByString
import kotlinx.coroutines.*

class BookViewModel(private val dbBook: BookDao): ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val books = dbBook.getAllByLastAccessed()

    private val bookKey = MutableLiveData<Long>()
    val book = MediatorLiveData<Book>()

    val descVisibility = Transformations.map(book) {
        setVisibilityByString(it.desc)
    }

    init {
        setBookMediatorSource()
    }

    private fun setBookMediatorSource() {
        book.addSource(bookKey) {
            uiScope.launch {
                book.value = getBookByKey(it)
            }
        }
    }

    fun setBookKey(key: Long) {
        bookKey.value = key
    }

    private suspend fun getBookByKey(key: Long): Book? {
        return withContext(Dispatchers.IO) {
            dbBook.get(key)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        book.removeSource(bookKey)
    }
}
