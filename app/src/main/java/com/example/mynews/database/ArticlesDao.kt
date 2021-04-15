package com.example.mynews.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ArticlesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticle(article:ArticleEntity)

    @Delete
    suspend fun deleteArticle(article: ArticleEntity)

    @Query("SELECT * FROM SavedArticles")
    suspend fun getSavedArticles():LiveData<List<ArticleEntity>>
}