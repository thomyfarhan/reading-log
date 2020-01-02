package com.aesthomic.readinglog.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Collection of functions
 * for accessing Read or read table in Room Database
 */
@Dao
interface ReadDao {

    /**
     * Inserting data to the database
     */
    @Insert
    fun insert(read: Read)

    /**
     * Update the data with the same id if existed
     *
     * @param read: object that will replace the old data
     */
    @Update
    fun update(read: Read)

    /**
     * Delete all data or rows from read table
     */
    @Query("DELETE FROM read")
    fun clear()

    /**
     * Get read object based on the requested id
     */
    @Query("SELECT * FROM read WHERE id = :id")
    fun get(id: Long): Read?

    /**
     * Return all rows of read table
     * and store them on live data form
     */
    @Query("SELECT * FROM read ORDER BY id DESC")
    fun getAllReads(): LiveData<MutableList<Read>>

    /**
     * Return the current read object
     */
    @Query("SELECT * FROM read ORDER BY id DESC LIMIT 1")
    fun getCurrentRead(): Read?

    @Query("SELECT read.id, read.start_time_millis, read.end_time_millis, read.last_page, books.title, books.page FROM read LEFT OUTER JOIN books ON read.book_id = books.id ORDER BY read.id DESC")
    fun getAllReadBook(): LiveData<List<ReadBook>>
}