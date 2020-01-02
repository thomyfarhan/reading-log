package com.aesthomic.readinglog.readdetail

import androidx.lifecycle.*
import com.aesthomic.readinglog.database.Book
import com.aesthomic.readinglog.database.BookDao
import com.aesthomic.readinglog.util.convertLongToFormat
import com.aesthomic.readinglog.database.Read
import com.aesthomic.readinglog.database.ReadDao
import kotlinx.coroutines.*

class ReadDetailViewModel (
    private val readKey: Long,
    private val dbRead: ReadDao,
    private val dbBook: BookDao): ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _read = MutableLiveData<Read?>()
    val read: LiveData<Read?>
        get() = _read

    val book = MediatorLiveData<Book>()

    private val _navigateToRead = MutableLiveData<Boolean>()
    val navigateToRead: LiveData<Boolean>
        get() = _navigateToRead

    val startTime = Transformations.map(read) {
        it?.let {
            convertLongToFormat(it.startTimeMillis)
        }
    }

    val endTime = Transformations.map(read) {
        it?.let {
            convertLongToFormat(it.endTimeMillis)
        }
    }

    init {
        initRead()
        initBook()
    }

    private fun initRead() {
        uiScope.launch {
            _read.value = getReadById(readKey)
        }
    }

    private fun initBook() {
        book.addSource(read) {
            uiScope.launch {
                book.value = getBookById(it?.bookId ?: 0L)
            }
        }
    }

    fun deleteRead() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dbRead.delete(readKey)
            }
        }
    }

    private suspend fun getReadById(key: Long): Read? {
        return withContext(Dispatchers.IO) {
            dbRead.get(key)
        }
    }

    private suspend fun getBookById(key: Long): Book? {
        return withContext(Dispatchers.IO) {
            dbBook.get(key)
        }
    }

    fun eventNavigateToRead() {
        _navigateToRead.value = true
    }

    fun doneNavigateToRead() {
        _navigateToRead.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        book.removeSource(read)
    }
}
