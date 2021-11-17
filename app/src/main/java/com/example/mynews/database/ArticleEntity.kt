package com.example.mynews.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SavedArticles")
data class ArticleEntity(
    @NonNull
    @PrimaryKey var url: String = "null",

    @ColumnInfo var publishedAt: String? = null,

    @ColumnInfo var author: String? = null,

    @ColumnInfo var urlToImage: String? = null,

    @ColumnInfo var description: String? = null,

    @ColumnInfo var source: String? = null,

    @ColumnInfo var title: String? = null,

    @ColumnInfo var content: String? = null)
