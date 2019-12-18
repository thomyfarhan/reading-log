package com.aesthomic.readinglog.readdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.aesthomic.readinglog.convertLongToFormat
import com.aesthomic.readinglog.database.Read
import com.aesthomic.readinglog.database.ReadDao
import kotlinx.coroutines.*

class ReadDetailViewModel (
    private val readKey: Long,
    private val database: ReadDao): ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _read = MutableLiveData<Read?>()
    val read: LiveData<Read?>
        get() = _read

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
    }

    private fun initRead() {
        uiScope.launch {
            _read.value = getReadById(readKey)
        }
    }

    private suspend fun getReadById(key: Long): Read? {
        return withContext(Dispatchers.IO) {
            database.get(key)
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
    }
}
