package com.aesthomic.readinglog.readbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aesthomic.readinglog.database.Book
import com.aesthomic.readinglog.database.BookDao
import com.aesthomic.readinglog.database.ReadDao
import kotlinx.coroutines.*

class ReadBookViewModel (
    private val readKey: Long,
    private val dbRead: ReadDao,
    private val dbBook: BookDao): ViewModel() {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _eventSubmit = MutableLiveData<Boolean>()
    val eventSubmit: LiveData<Boolean>
        get() = _eventSubmit

    private val _eventCamera = MutableLiveData<Boolean>()
    val eventCamera: LiveData<Boolean>
        get() = _eventCamera

    private val _eventBookSubmit = MutableLiveData<Boolean>()
    val eventBookSubmit: LiveData<Boolean>
        get() = _eventBookSubmit

    fun updateRead(time: Long) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val read = dbRead.get(readKey) ?: return@withContext
                read.endTimeMillis = time
                dbRead.update(read)
            }
        }
    }

    fun addBook(photo: String, title: String, page: Int) {
        uiScope.launch {
            val book = Book(photo = photo, title = title, page = page)
            insert(book)
        }
    }

    private suspend fun insert(book: Book) {
        withContext(Dispatchers.IO) {
            dbBook.insert(book)
        }
    }

    fun onDataSubmitted() {
        _eventSubmit.value = true
    }

    fun onSubmitDone() {
        _eventSubmit.value = false
    }

    fun onEventCamera() {
        _eventCamera.value = true
    }

    fun onCameraDone() {
        _eventCamera.value = false
    }

    fun onEventBookSubmit() {
        _eventBookSubmit.value = true
    }

    fun onBookSubmitDone() {
        _eventBookSubmit.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}