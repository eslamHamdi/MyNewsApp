package com.example.mynews.repository

import com.example.mynews.api.Service
import com.example.mynews.database.ArticlesDao
import com.example.mynews.domain.Article
import com.example.mynews.dto.Result
import com.example.mynews.utils.domainToEntity
import com.example.mynews.utils.dtoToDomain
import com.example.mynews.utils.entityToDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class NewsRepository(private val dao: ArticlesDao,private val NewService: Service,private var dispatcher:CoroutineDispatcher = Dispatchers.IO) :
    DataSource {


    override fun getSavedArticles(): Flow<List<Article>> {
       return dao.getSavedArticles().map {
           it.entityToDomain()
       }
    }

//    val savedArticles: LiveData<List<Article>> = Transformations.map(dao.getSavedArticles()){
//
//       it.entityToDomain()
//    }


    // val savedArticles:Flow<Article> =


    override suspend fun getNewsByCountry(countryCode: String) = withContext(dispatcher) {

        val response = NewService.getHeadLines(country = countryCode)

        if (response.isSuccessful) {
            val list = response.body()?.articles.dtoToDomain()
            //Log.d(null, "getNewsByCountry: $list" )

            return@withContext Result.Success(data = list!!)
        } else {
            return@withContext Result.Error(response.message(), response.code())
        }

    }


    override suspend fun getNewsByCatagory(
        countryCode: String,
        category: String
    ): Result<List<Article>> {
        var result: Result<List<Article>>

        withContext(dispatcher) {

            val response =
                NewService.getByCatagory(country = countryCode, category = category)

            result = if (response.isSuccessful) {
                val list = response.body()?.articles.dtoToDomain()
                Result.Success(data = list!!)
            } else {
                Result.Error(response.message(), response.code())
            }

        }

        return result


    }

    override suspend fun NewsSearch(keyWord: String): Result<List<Article>> {
        var result: Result<List<Article>>

        withContext(dispatcher) {

            val response = NewService.searchForNews(searchQuery = keyWord)

            result = if (response.isSuccessful) {
                val list = response.body()?.articles.dtoToDomain()
                Result.Success(data = list!!)
            } else {
                Result.Error(response.message(), response.code())
            }

        }

        return result
    }


    override suspend fun saveArticle(item: Article) {
        withContext(dispatcher)
        {

            dao.saveArticle(item.domainToEntity())
        }
    }

    override suspend fun deleteSavedArticle(itemID: String) {
        withContext(dispatcher)
        {
            dao.deleteArticle(itemID)
        }

    }

    override suspend fun deleteAllArticles() {
        withContext(dispatcher)
        {
            dao.deleteAllArticles()
        }

    }


}
