package com.aesthomic.readinglog.read

import android.app.Application
import androidx.lifecycle.*
import com.aesthomic.readinglog.database.Read
import com.aesthomic.readinglog.database.ReadDao
import kotlinx.coroutines.*

class ReadViewModel(
    private val database: ReadDao,
    application: Application): AndroidViewModel(application) {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val currentRead = MutableLiveData<Read?>()

    private val _navigateToReadBook = MutableLiveData<Read?>()
    val navigateToReadBook: LiveData<Read?>
        get() = _navigateToReadBook

    private val _navigateToDetail = MutableLiveData<Long?>()
    val navigateToDetail: LiveData<Long?>
        get() = _navigateToDetail

    private val _eventDelete = MutableLiveData<Boolean>()
    val eventDelete: LiveData<Boolean>
        get() = _eventDelete

    val readBook = database.getAllReadBook()

    /**
     * Add list and group the list with the data item type
     * by adding conditial whether the read object is odd or even
     */
    val readList = Transformations.map(readBook) { readBooks ->
        readBooks.map {
            if (it.id % 2L == 0L) {
                DataItem.ReadItemEven(it)
            } else {
                DataItem.ReadItemOdd(it)
            }
        }
    }

    val fabState = Transformations.map(currentRead) {
        it == null
    }

    val deleteEnable = Transformations.map(readBook) {
        it.isNotEmpty()
    }

    private val readKey = MutableLiveData<Long>()

    private val _read = MediatorLiveData<Read>()
    val read: LiveData<Read>
        get() = _read

    init {
        initializeCurrentRead()
        _read.addSource(readKey) {initReadById(it)}
    }

    private fun initializeCurrentRead() {
        uiScope.launch {
            currentRead.value = getCurrentFromDatabase()
        }
    }

    private fun initReadById(key: Long) {
        uiScope.launch {
            _read.value = getReadById(key)
        }
    }

    fun insertRead(read: Read) {
        uiScope.launch {
            insert(read)
            currentRead.value = getCurrentFromDatabase()
        }
    }

    fun setReadKey(key: Long) {
        readKey.value = key
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

    private suspend fun getReadById(key: Long): Read? {
        return withContext(Dispatchers.IO) {
            database.get(key)
        }
    }

    fun deleteReadById(key: Long) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.delete(key)
            }
            currentRead.value = getCurrentFromDatabase()
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
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

    fun onReading() {
        fabState.value?.let {
            if(it) onStartReading() else onStopReading()
        }
    }

    private fun onStopReading() {
        uiScope.launch {
            val oldRead = currentRead.value ?: return@launch
            _navigateToReadBook.value = oldRead
        }
    }

    private fun onStartReading() {
        uiScope.launch {
            val read = Read(startTimeMillis = System.currentTimeMillis())
            insert(read)
            currentRead.value = getCurrentFromDatabase()
        }
    }

    fun onReadClicked(key: Long) {
        _navigateToDetail.value = key
    }

    fun onNavigateReadBookDone() {
        _navigateToReadBook.value = null
    }

    fun onNavigateDetailDone() {
        _navigateToDetail.value = null
    }

    fun onEventDelete() {
        _eventDelete.value = true
    }

    fun onDeleteDone() {
        _eventDelete.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        _read.removeSource(readKey)
    }
}