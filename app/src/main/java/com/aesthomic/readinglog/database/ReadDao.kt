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
    @Query("SELECT * FROM read ORDER BY 1 DESC LIMIT 1")
    fun getCurrentRead(): Read?
}