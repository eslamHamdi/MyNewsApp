package com.example.mynews.repository

import com.example.mynews.api.Service
import com.example.mynews.database.ArticlesDao
import com.example.mynews.domain.Article
import com.example.mynews.dto.NewsResponse
import com.example.mynews.utils.domainToEntity
import com.example.mynews.utils.entityToDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class NewsRepository(private val dao: ArticlesDao,private val NewService: Service,private var dispatcher:CoroutineDispatcher = Dispatchers.IO) :
    DataSource {

//    val savedArticles: LiveData<List<Article>> = Transformations.map(dao.getSavedArticles()){
//
//       it.entityToDomain()
//    }


    override val savedArticles:Flow<List<Article>> = flow { dao.getSavedArticles().map {
        it.entityToDomain()
    }.distinctUntilChanged() }


    override suspend fun getNewsByCountry(countryCode:String): NewsResponse?
    {
        return withContext(dispatcher){NewService.getHeadLines(country = countryCode).body()}


    }

    override suspend fun getNewsByCatagory(countryCode: String, category:String):NewsResponse?
    {
        return withContext(dispatcher){NewService.getByCatagory(country = countryCode,category = category).body()}



    }

    //suspend fun NewsSearch(key)



    override suspend fun saveArticle(item:Article)
    {
        withContext(dispatcher)
        {

            dao.saveArticle(item.domainToEntity())
        }
    }

    override suspend fun deleteSavedArticle(itemID:String)
    {
        withContext(dispatcher)
        {
            dao.deleteArticle(itemID)
        }

    }

    override suspend fun deleteAllArticles()
    {
        withContext(dispatcher)
        {
            dao.deleteAllArticles()
        }

    }
}