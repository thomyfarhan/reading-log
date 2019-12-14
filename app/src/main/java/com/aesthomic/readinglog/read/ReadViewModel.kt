package com.aesthomic.readinglog.read

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.aesthomic.readinglog.database.ReadDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class ReadViewModel(
    private val database: ReadDao,
    application: Application): AndroidViewModel(application) {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}