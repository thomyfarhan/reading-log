package com.aesthomic.readinglog.database

import androidx.room.ColumnInfo

data class ReadBook(
    @ColumnInfo(name = "id")
    var id: Long = 0L,

    @ColumnInfo(name = "start_time_millis")
    var startTimeMillis: Long = 0L,

    @ColumnInfo(name = "end_time_millis")
    var endTimeMillis: Long = 0L,

    @ColumnInfo(name = "last_page")
    var lastPage: Int = 0,

    @ColumnInfo(name = "title")
    var bookTitle: String? = "",

    @ColumnInfo(name = "page")
    var totalPage: Int? = 0
)
