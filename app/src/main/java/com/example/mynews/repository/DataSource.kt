package com.example.mynews.repository

import com.example.mynews.domain.Article
import com.example.mynews.dto.NewsResponse
import kotlinx.coroutines.flow.Flow

interface DataSource {

     val savedArticles:Flow<List<Article>>
    suspend fun getNewsByCountry(countryCode: String): NewsResponse?

    suspend fun getNewsByCatagory(countryCode: String, category: String): NewsResponse?

    suspend fun saveArticle(item: Article)

    suspend fun deleteSavedArticle(itemID: String)

    suspend fun deleteAllArticles()
}