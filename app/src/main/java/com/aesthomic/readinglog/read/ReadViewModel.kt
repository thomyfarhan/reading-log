package com.aesthomic.readinglog.read

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.aesthomic.readinglog.database.Read
import com.aesthomic.readinglog.database.ReadDao
import kotlinx.coroutines.*

class ReadViewModel(
    private val database: ReadDao,
    application: Application): AndroidViewModel(application) {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val reads = database.getAllReads()

    private suspend fun insert(read: Read) {
        withContext(Dispatchers.IO) {
            database.insert(read)
        }
    }

    fun onStartReading() {
        uiScope.launch {
            val read = Read(startTimeMillis = System.currentTimeMillis())
            insert(read)
        }
    }

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}