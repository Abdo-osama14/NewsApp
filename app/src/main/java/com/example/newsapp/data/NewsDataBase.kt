package com.example.newsapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.domain.modles.Article

@Database(entities = [Article::class], version = 1)
@TypeConverters(RoomDBConverter::class)

abstract class NewsDataBase : RoomDatabase() {
    abstract fun getNewsDao():NewsDao
}