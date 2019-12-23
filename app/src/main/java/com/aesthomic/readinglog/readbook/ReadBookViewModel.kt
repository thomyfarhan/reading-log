package com.aesthomic.readinglog.readbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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

    private val _eventImage = MutableLiveData<Boolean>()
    val eventImage: LiveData<Boolean>
        get() = _eventImage

    val titleText = MutableLiveData<String>()
    val pageText = MutableLiveData<String>()
    val photoUri = MutableLiveData<String>()
    val titlePageMediator = MediatorLiveData<Boolean>()

    init {
        titlePageMediator.apply {
            addSource(titleText) {checkTitlePage()}
            addSource(pageText) {checkTitlePage()}
        }
    }

    private fun checkTitlePage() {
        titlePageMediator.value = titleText.value?.isNotBlank() ?: false
                && pageText.value?.isNotBlank() ?: false
    }

    fun updateRead(time: Long) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val read = dbRead.get(readKey) ?: return@withContext
                read.endTimeMillis = time
                dbRead.update(read)
            }
        }
    }

    fun addBook() {
        uiScope.launch {
            val book = Book(photo = photoUri.value ?: "",
                title = titleText.value ?: "",
                page = pageText.value?.toInt() ?: 0)
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

    fun onEventImage() {
        _eventImage.value = true
    }

    fun onImageDone() {
        _eventImage.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()

        titlePageMediator.apply {
            removeSource(titleText)
            removeSource(pageText)
        }
    }
}