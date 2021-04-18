package com.example.mynews.ui.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.mynews.domain.Article
import com.example.mynews.dto.NewsResponse
import com.example.mynews.repository.DataSource
import com.example.mynews.utils.dtoToDomain
import kotlinx.coroutines.launch

class NewsViewModel(private val repo:DataSource):ViewModel() {


    val savedNews:LiveData<List<Article>> = repo.savedArticles.asLiveData(viewModelScope.coroutineContext)
    var LoadingState:MutableLiveData<Boolean> = MutableLiveData(false)
    var news:MutableLiveData<List<Article>> = MutableLiveData(null)


    fun getNews(code:String)
    {
        LoadingState.value = true

        var response:NewsResponse? = null

        viewModelScope.launch {

            try {
                response = repo.getNewsByCountry(code)
                val list = response?.articles.dtoToDomain()
                news.postValue(list)
            }catch (e:Exception)
            {
                Log.e(null, "getNews: ${response?.message} + ${response?.code} ", )
            }

        }
        LoadingState.value = false
    }

    fun getbyCategory(code:String,category:String)
    {
        LoadingState.value = true

        var response:NewsResponse? = null

        viewModelScope.launch {

            try {
                response = repo.getNewsByCatagory(code,category)
                val list = response?.articles.dtoToDomain()
                news.postValue(list)
            }catch (e:Exception)
            {
                Log.e(null, "getNews: ${response?.message} + ${response?.code} ", )
            }

            LoadingState.value = false

        }

    }

    fun addToFavorites(article:Article)
    {
        viewModelScope.launch {
            repo.saveArticle(article)

        }

    }

    fun deleteArticle(url:String)
    {
        viewModelScope.launch {

            repo.deleteSavedArticle(url)
        }
    }

    fun deleteAll()
    {
        viewModelScope.launch {
            repo.deleteAllArticles()
        }
    }






}