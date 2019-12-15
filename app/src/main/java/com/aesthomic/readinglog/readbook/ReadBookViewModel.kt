package com.aesthomic.readinglog.readbook

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.aesthomic.readinglog.database.ReadDao

class ReadBookViewModel (
    private val readKey: Long,
    private val database: ReadDao,
    application: Application): AndroidViewModel(application) {}