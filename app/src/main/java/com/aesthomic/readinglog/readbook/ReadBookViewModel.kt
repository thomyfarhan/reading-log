package com.aesthomic.readinglog.readbook

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aesthomic.readinglog.database.Book
import com.aesthomic.readinglog.database.BookDao
import com.aesthomic.readinglog.database.ReadDao
import com.aesthomic.readinglog.util.writeBitmapFile
import kotlinx.coroutines.*
import java.io.File

class ReadBookViewModel (
    var readKey: Long,
    private val dbRead: ReadDao,
    private val dbBook: BookDao): ViewModel() {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _selectedBook = MutableLiveData<Book>()
    val selectedBook: LiveData<Book>
        get() = _selectedBook

    private val _eventSubmit = MutableLiveData<Boolean>()
    val eventSubmit: LiveData<Boolean>
        get() = _eventSubmit

    private val _eventImage = MutableLiveData<Boolean>()
    val eventImage: LiveData<Boolean>
        get() = _eventImage

    private val _eventCamera = MutableLiveData<Boolean>()
    val eventCamera: LiveData<Boolean>
        get() = _eventCamera

    private val _eventGallery = MutableLiveData<Boolean>()
    val eventGallery: LiveData<Boolean>
        get() = _eventGallery

    private val _eventBook = MutableLiveData<Boolean>()
    val eventBook: LiveData<Boolean>
        get() = _eventBook

    private val _pictureFile = MutableLiveData<File>()
    val pictureFile: LiveData<File>
        get() = _pictureFile

    var newPicture: File? = null

    val titleText = MutableLiveData<String>()
    val pageText = MutableLiveData<String>()
    val photoUri = MutableLiveData<String>()
    val titlePageMediator = MediatorLiveData<Boolean>()

    val readPageText = MutableLiveData<String>()
    val readMediator = MediatorLiveData<Boolean>()

    init {
        titlePageMediator.apply {
            addSource(titleText) {checkTitlePage()}
            addSource(pageText) {checkTitlePage()}
        }
        setReadMediator()
        initSelectedBook()
    }

    private fun initSelectedBook() {
        uiScope.launch {
            _selectedBook.value = getBookByLastAccessed()
        }
    }

    private fun setReadMediator() {
        readMediator.apply {
            addSource(readPageText) {checkReadText()}
            addSource(selectedBook) {checkReadText()}
        }
    }

    private fun checkTitlePage() {
        titlePageMediator.value = titleText.value?.isNotBlank() ?: false
                && pageText.value?.isNotBlank() ?: false
    }

    private fun checkReadText() {
        readMediator.value = readPageText.value?.isNotBlank() ?: false
                && selectedBook.value != null
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
                page = pageText.value?.toInt() ?: 0,
                lastAccessed = System.currentTimeMillis())
            insert(book)
            _selectedBook.value = getBookByLastAccessed()
        }
    }

    fun inputBitmapFile(source: Bitmap) {
        uiScope.launch {
            newPicture?.let {
                writeBitmapFile(source, it)
                _pictureFile.value = it
            }
        }
    }

    private suspend fun insert(book: Book) {
        withContext(Dispatchers.IO) {
            dbBook.insert(book)
        }
    }

    private suspend fun getBookById(id: Long): Book? {
        return withContext(Dispatchers.IO) {
            dbBook.get(id)
        }
    }

    private suspend fun getBookByLastAccessed(): Book? {
        return withContext(Dispatchers.IO) {
            dbBook.getLastAccessed()
        }
    }

    fun setSelectedBook(key: Long) {
        uiScope.launch {
            _selectedBook.value = getBookById(key)
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

    fun onEventCamera() {
        _eventCamera.value = true
    }

    fun onCameraDone() {
        _eventCamera.value = false
    }

    fun onEventGallery() {
        _eventGallery.value = true
    }

    fun onGalleryDone() {
        _eventGallery.value = false
    }

    fun onEventBook() {
        if (selectedBook.value != null) {
            _eventBook.value = true
        }
    }

    fun onBookDone() {
        _eventBook.value = false
    }

    fun onClearState() {
        viewModelJob.cancel()

        titlePageMediator.apply {
            removeSource(titleText)
            removeSource(pageText)
        }

        readMediator.apply {
            removeSource(readPageText)
            removeSource(selectedBook)
        }
    }


}