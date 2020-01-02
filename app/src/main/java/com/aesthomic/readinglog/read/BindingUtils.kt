package com.aesthomic.readinglog.read

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.aesthomic.readinglog.util.convertLongToDate
import com.aesthomic.readinglog.util.convertLongToDuration
import com.aesthomic.readinglog.util.convertLongToMonth
import com.aesthomic.readinglog.database.Read
import com.aesthomic.readinglog.database.ReadBook

@BindingAdapter("readMonthFormatted")
fun TextView.setReadMonthFormatted(readBook: ReadBook) {
    text = convertLongToMonth(readBook.startTimeMillis)
}

@BindingAdapter("readDateFormatted")
fun TextView.setReadDateFormatted(readBook: ReadBook) {
    text = convertLongToDate(readBook.startTimeMillis)
}

@BindingAdapter("readDurationFormatted")
fun TextView.setReadDurationFormatted(readBook: ReadBook) {
    text = convertLongToDuration(
        readBook.startTimeMillis,
        readBook.endTimeMillis,
        context.resources
    )
}
