package com.example.mynews.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ArticleEntity::class],version = 1,exportSchema = false)
abstract class LocalDataBase: RoomDatabase() {

    abstract fun getDao():ArticlesDao
}