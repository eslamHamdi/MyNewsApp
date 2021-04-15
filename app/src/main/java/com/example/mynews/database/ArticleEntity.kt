package com.example.mynews.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mynews.dto.Source

@Entity(tableName = "SavedArticles")
data class ArticleEntity(
    @PrimaryKey val url: String? = null,

    @ColumnInfo val publishedAt: String? = null,

    @ColumnInfo val author: String? = null,

    @ColumnInfo val urlToImage: String? = null,

    @ColumnInfo val description: String? = null,

    @ColumnInfo val source: Source? = null,

    @ColumnInfo val title: String? = null,

    @ColumnInfo val content: String? = null)
