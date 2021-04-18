package com.example.mynews.repository

import com.example.mynews.domain.Article
import com.example.mynews.dto.Result
import kotlinx.coroutines.flow.Flow

interface DataSource {

     val savedArticles:Flow<List<Article>>
    suspend fun getNewsByCountry(countryCode: String): Result<List<Article>>

    suspend fun getNewsByCatagory(countryCode: String, category: String): Result<List<Article>>

    suspend fun NewsSearch(keyWord:String):Result<List<Article>>

    suspend fun saveArticle(item: Article)

    suspend fun deleteSavedArticle(itemID: String)

    suspend fun deleteAllArticles()
}