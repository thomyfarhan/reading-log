package com.aesthomic.readinglog.readdetail

import androidx.lifecycle.ViewModel
import com.aesthomic.readinglog.database.ReadDao

class ReadDetailViewModel (
    private val readKey: Long,
    private val database: ReadDao): ViewModel() {}