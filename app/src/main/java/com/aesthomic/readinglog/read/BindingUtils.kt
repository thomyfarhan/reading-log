package com.aesthomic.readinglog.read

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.aesthomic.readinglog.util.convertLongToDate
import com.aesthomic.readinglog.util.convertLongToDuration
import com.aesthomic.readinglog.util.convertLongToMonth
import com.aesthomic.readinglog.database.Read

@BindingAdapter("readMonthFormatted")
fun TextView.setReadMonthFormatted(read: Read) {
    text = convertLongToMonth(read.startTimeMillis)
}

@BindingAdapter("readDateFormatted")
fun TextView.setReadDateFormatted(read: Read) {
    text = convertLongToDate(read.startTimeMillis)
}

@BindingAdapter("readDurationFormatted")
fun TextView.setReadDurationFormatted(read: Read) {
    text = convertLongToDuration(
        read.startTimeMillis,
        read.endTimeMillis,
        context.resources
    )
}
