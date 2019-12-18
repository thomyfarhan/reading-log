package com.aesthomic.readinglog.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2: Migration = object: Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `books` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `page` INTEGER NOT NULL, `desc` TEXT NOT NULL, `photo` TEXT NOT NULL)")

        database.execSQL("CREATE TABLE IF NOT EXISTS `read_new` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `start_time_millis` INTEGER NOT NULL, `end_time_millis` INTEGER NOT NULL, `last_page` INTEGER NOT NULL, `book_id` INTEGER, FOREIGN KEY(`book_id`) REFERENCES `books`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")

        database.execSQL("INSERT INTO read_new (id, start_time_millis, end_time_millis, last_page, book_id) SELECT id, start_time_millis, end_time_millis, chapter, null FROM read")

        database.execSQL("DROP TABLE read")

        database.execSQL("ALTER TABLE read_new RENAME TO read")

        database.execSQL("CREATE INDEX IF NOT EXISTS `index_read_book_id` ON `read` (`book_id`)")
    }

}