package com.aesthomic.readinglog.read

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aesthomic.readinglog.database.Read
import com.aesthomic.readinglog.database.ReadDao
import kotlinx.coroutines.*

class ReadViewModel(
    private val database: ReadDao,
    application: Application): AndroidViewModel(application) {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val currentRead = MutableLiveData<Read?>()

    val reads = database.getAllReads()

    init {
        initializeCurrentRead()
    }

    private fun initializeCurrentRead() {
        uiScope.launch {
            currentRead.value = getCurrentFromDatabase()
        }
    }

    private suspend fun getCurrentFromDatabase(): Read? {
        return withContext(Dispatchers.IO) {
            var read = database.getCurrentRead()
            if (read?.startTimeMillis != read?.endTimeMillis) {
                read = null
            }
            read
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    private suspend fun update(read: Read) {
        withContext(Dispatchers.IO) {
            database.update(read)
        }
    }

    private suspend fun insert(read: Read) {
        withContext(Dispatchers.IO) {
            database.insert(read)
        }
    }

    fun onDelete() {
        uiScope.launch {
            clear()
            currentRead.value = null
        }
    }

    fun onStopReading() {
        uiScope.launch {
            val oldRead = currentRead.value ?: return@launch
            oldRead.endTimeMillis = System.currentTimeMillis()
            update(oldRead)
        }
    }

    fun onStartReading() {
        uiScope.launch {
            val read = Read(startTimeMillis = System.currentTimeMillis())
            insert(read)
            currentRead.value = getCurrentFromDatabase()
        }
    }

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}