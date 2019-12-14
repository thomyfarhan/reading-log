package com.aesthomic.readinglog.read

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.aesthomic.readinglog.database.ReadDao

class ReadViewModel(
    private val database: ReadDao,
    application: Application): AndroidViewModel(application) {}