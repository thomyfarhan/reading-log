package com.aesthomic.readinglog.database

data class Read(
    var id: Long = 0L,
    var startTimeMillis: Long = 0L,
    var endTimeMillis: Long = 0L,
    var bookName: String = "",
    var chapter: Int = 0
)
