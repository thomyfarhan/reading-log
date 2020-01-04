package com.aesthomic.readinglog.book

import androidx.lifecycle.*
import com.aesthomic.readinglog.database.Book
import com.aesthomic.readinglog.database.BookDao
import com.aesthomic.readinglog.util.setVisibilityByString
import kotlinx.coroutines.*

class BookViewModel(private val dbBook: BookDao): ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val books = dbBook.getAllByLastAccessed()

    private val bookKey = MutableLiveData<Long>()
    val book = MediatorLiveData<Book>()

    val descVisibility = Transformations.map(book) {
        setVisibilityByString(it?.desc)
    }

    val bookTitleField = MutableLiveData<String>()
    val bookPageField = MutableLiveData<String>()
    val bookDescField = MutableLiveData<String>()

    val updateInability = MediatorLiveData<Boolean>()

    private val _eventNavigateBook = MutableLiveData<Boolean>()
    val eventNavigateBook: LiveData<Boolean>
        get() = _eventNavigateBook

    init {
        setBookMediatorSource()
        setUpdateMediatorSource()
    }

    private fun setBookMediatorSource() {
        book.addSource(bookKey) {
            uiScope.launch {
                book.value = getBookByKey(it)
            }
        }
    }

    private fun setUpdateMediatorSource() {
        updateInability.addSource(bookTitleField) {checkUpdateField()}
        updateInability.addSource(bookPageField) {checkUpdateField()}
    }

    fun checkUpdateField() {
        updateInability.value = !(bookTitleField.value.isNullOrBlank()
                || bookPageField.value.isNullOrBlank())
    }

    fun setBookKey(key: Long) {
        bookKey.value = key
    }

    fun onUpdateBook() {
        uiScope.launch {
            val newBook = book.value ?: return@launch
            with(newBook) {
                title = bookTitleField.value ?: ""
                page = bookPageField.value?.toInt() ?: 0
                desc = bookDescField.value ?: ""
            }
            updateBook(newBook)
        }
        _eventNavigateBook.value = true
    }

    private suspend fun getBookByKey(key: Long): Book? {
        return withContext(Dispatchers.IO) {
            dbBook.get(key)
        }
    }

    private suspend fun updateBook(book: Book) {
        withContext(Dispatchers.IO) {
            dbBook.update(book)
        }
    }

    fun onNavigateBookDone() {
        _eventNavigateBook.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        book.removeSource(bookKey)

        updateInability.apply {
            removeSource(bookTitleField)
            removeSource(bookPageField)
        }
    }
}
