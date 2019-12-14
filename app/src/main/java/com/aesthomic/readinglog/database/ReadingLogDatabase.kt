package com.aesthomic.readinglog.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Read::class], version = 1)
abstract class ReadingLogDatabase: RoomDatabase() {

}