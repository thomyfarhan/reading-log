package com.aesthomic.readinglog.database

data class Book(
    var id: Long = 0L,

    var title: String = "",

    var page: Int = 0,

    var desc: String = "",

    var photo: String = ""
)