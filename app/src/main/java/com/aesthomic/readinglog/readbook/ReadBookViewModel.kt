package com.aesthomic.readinglog.readbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aesthomic.readinglog.database.Read
import com.aesthomic.readinglog.database.ReadDao
import kotlinx.coroutines.*

class ReadBookViewModel (
    private val readKey: Long,
    private val database: ReadDao): ViewModel() {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _eventSubmit = MutableLiveData<Boolean>()
    val eventSubmit: LiveData<Boolean>
        get() = _eventSubmit

    fun updateRead(bookName: String, page: Int) {
        uiScope.launch {
            val read = getReadById(readKey) ?: return@launch
            read.bookName = bookName
            read.chapter = page
            update(read)
        }
    }

    private suspend fun getReadById(key: Long): Read? {
        return withContext(Dispatchers.IO) {
            database.get(key)
        }
    }

    private suspend fun update(read: Read) {
        withContext(Dispatchers.IO) {
            database.update(read)
        }
    }

    fun onDataSubmitted() {
        _eventSubmit.value = true
    }

    fun onSubmitDone() {
        _eventSubmit.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}