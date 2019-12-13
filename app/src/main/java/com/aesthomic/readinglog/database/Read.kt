package com.aesthomic.readinglog.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "read")
data class Read(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "start_time_millis")
    var startTimeMillis: Long = 0L,

    @ColumnInfo(name = "end_time_millis")
    var endTimeMillis: Long = 0L,

    @ColumnInfo(name = "book_name")
    var bookName: String = "",

    @ColumnInfo(name = "chapter")
    var chapter: Int = 0
)
