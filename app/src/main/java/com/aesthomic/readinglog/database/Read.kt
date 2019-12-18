package com.aesthomic.readinglog.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "read",
    foreignKeys = [ForeignKey(entity = Book::class,
        parentColumns = ["id"],
        childColumns = ["book_id"],
        onDelete = ForeignKey.CASCADE)])
data class Read(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "start_time_millis")
    var startTimeMillis: Long = 0L,

    @ColumnInfo(name = "end_time_millis")
    var endTimeMillis: Long = startTimeMillis,

    @ColumnInfo(name = "last_page")
    var lastPage: Int = 0,

    @ColumnInfo(name = "book_id", index = true)
    var bookId: Long? = null
)
