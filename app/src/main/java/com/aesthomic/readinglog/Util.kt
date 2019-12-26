package com.aesthomic.readinglog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Resources
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

private val ONE_MINUTE_MILLI = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)
private val ONE_HOURS_MILLI = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
private val ONE_DAY_MILLI = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)

fun convertLongToDuration(startTime: Long, endTime: Long, res: Resources): String {
    val duration = endTime - startTime
    val seconds = TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) % 60
    val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS) % 60
    val hours = TimeUnit.HOURS.convert(duration, TimeUnit.MILLISECONDS) % 24
    val days = TimeUnit.DAYS.convert(duration, TimeUnit.MILLISECONDS)

    return when {
        duration < ONE_MINUTE_MILLI -> {
            res.getString(R.string.duration_seconds,
                seconds, "second".pluralize(seconds))
        }
        duration < ONE_HOURS_MILLI -> {
            res.getString(R.string.duration,
                minutes, "minute".pluralize(minutes),
                seconds, "second".pluralize(seconds))
        }
        duration < ONE_DAY_MILLI -> {
            res.getString(R.string.duration,
                hours, "hour".pluralize(hours),
                minutes, "minute".pluralize(minutes))
        }
        else -> {
            res.getString(R.string.duration,
                days, "day".pluralize(days),
                hours, "hours".pluralize(hours))
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun convertLongToMonth(time: Long): String {
    return SimpleDateFormat("MMM")
        .format(time).toString()
}

@SuppressLint("SimpleDateFormat")
fun convertLongToDate(time: Long): String {
    return SimpleDateFormat("dd")
        .format(time).toString()
}

@SuppressLint("SimpleDateFormat")
fun convertLongToFormat(time: Long): String {
    return SimpleDateFormat("EEEE', 'dd MMMM yyyy' at 'HH:mm")
        .format(time).toString()
}

fun getTime(): String {
    return SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        .format(Date())
}

fun String.pluralize(value: Long): String {
    return this.pluralize(value, null)
}

fun String.pluralize(value: Long, pluralize: String?): String {
    return if (value > 1) {
        pluralize ?: this + 's'
    } else {
        this
    }
}

fun createImageFile(activity: Activity): File {
    val timeStamp = getTime()
    val pictureName = activity.resources
        .getString(R.string.picture_file_format, timeStamp)

    val storageDir = activity.getExternalFilesDir(
        Environment.DIRECTORY_PICTURES)

    return File.createTempFile(
        pictureName,
        "jpg",
        storageDir)
}
