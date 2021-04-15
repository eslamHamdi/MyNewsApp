package com.example.mynews.database

import androidx.room.Database

@Database(entities = [ArticleEntity::class],version = 1,exportSchema = false)
abstract class LocalDataBase {

    abstract fun getDao():ArticlesDao
}