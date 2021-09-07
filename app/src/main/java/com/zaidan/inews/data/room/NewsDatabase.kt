package com.zaidan.inews.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zaidan.inews.data.response.ArticlesItem

@Database(
    entities = [ArticlesItem::class],
    version = 1,
    exportSchema = false
)
abstract class NewsDatabase: RoomDatabase() {
    abstract fun newsDao(): NewsDao
}