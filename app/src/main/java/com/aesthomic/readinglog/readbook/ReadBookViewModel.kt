package com.aesthomic.readinglog.readbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aesthomic.readinglog.database.ReadDao
import kotlinx.coroutines.*

class ReadBookViewModel (
    private val readKey: Long,
    private val dbRead: ReadDao): ViewModel() {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _eventSubmit = MutableLiveData<Boolean>()
    val eventSubmit: LiveData<Boolean>
        get() = _eventSubmit

    fun updateRead(time: Long) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val read = dbRead.get(readKey) ?: return@withContext
                read.endTimeMillis = time
                dbRead.update(read)
            }
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