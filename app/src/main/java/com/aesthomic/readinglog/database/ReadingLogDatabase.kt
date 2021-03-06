package com.aesthomic.readinglog.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Read::class, Book::class], version = 3, exportSchema = true)
abstract class ReadingLogDatabase: RoomDatabase() {

    abstract val readDao: ReadDao
    abstract val bookDao: BookDao

    companion object {

        @Volatile
        private var INSTANCE: ReadingLogDatabase? = null

        fun getInstance(context: Context): ReadingLogDatabase {

            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        ReadingLogDatabase::class.java,
                        "reading_log_database")
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}