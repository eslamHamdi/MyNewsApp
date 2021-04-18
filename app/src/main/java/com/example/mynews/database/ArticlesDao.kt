package com.example.mynews.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticlesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticle(article:ArticleEntity)

    @Query("DELETE FROM SavedArticles WHERE url =:articleID" )
    suspend fun deleteArticle(articleID: String)

    @Query("SELECT * FROM SavedArticles")
    fun getSavedArticles():Flow<List<ArticleEntity>>

    @Query("DELETE FROM SavedArticles")
    suspend fun deleteAllArticles()
}