package com.aesthomic.readinglog.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BookDao {

    @Insert
    fun insert(book: Book)

    @Update
    fun update(book: Book)

    @Query("SELECT * FROM books WHERE id = :id")
    fun get(id: Long): Book?

    @Query("DELETE FROM books WHERE id = :id")
    fun delete(id: Long)

    @Query("SELECT * FROM books ORDER BY id DESC")
    fun getAll(): LiveData<List<Book>>
}
