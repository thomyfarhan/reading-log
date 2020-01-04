package com.aesthomic.readinglog.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.aesthomic.readinglog.R
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
            res.getString(
                R.string.duration_seconds,
                seconds, "second".pluralize(seconds))
        }
        duration < ONE_HOURS_MILLI -> {
            res.getString(
                R.string.duration,
                minutes, "minute".pluralize(minutes),
                seconds, "second".pluralize(seconds))
        }
        duration < ONE_DAY_MILLI -> {
            res.getString(
                R.string.duration,
                hours, "hour".pluralize(hours),
                minutes, "minute".pluralize(minutes))
        }
        else -> {
            res.getString(
                R.string.duration,
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

@BindingAdapter("pluralizePageText")
fun TextView.setPluralizePageText(page: Int?) {
    page?.let {
        text = resources.getString(R.string.page_format, it, "page".pluralize(it.toLong()))
    }
}

@BindingAdapter("srcByUriString")
fun ImageView.setSrcByUriString(uriString: String?) {
    if (!uriString.isNullOrBlank()) {
        setImageURI(Uri.parse(uriString))
    } else {
        setImageResource(R.drawable.ic_photo)
    }
}

fun hideKeyboard(context: Context, view: View) {
    val imm = context.getSystemService(
        Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun setVisibilityByString(string: String): Int {
    return if (string.isBlank()) {
        View.GONE
    } else {
        View.VISIBLE
    }
}