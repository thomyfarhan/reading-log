package com.aesthomic.readinglog.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "page")
    var page: Int = 0,

    @ColumnInfo(name = "desc")
    var desc: String = "",

    @ColumnInfo(name = "photo")
    var photo: String = ""
)
