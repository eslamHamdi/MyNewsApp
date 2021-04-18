package com.example.mynews.ui.viewmodels

import androidx.lifecycle.*
import com.example.mynews.domain.Article
import com.example.mynews.dto.Result
import com.example.mynews.repository.DataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NewsViewModel(private val repo:DataSource):ViewModel() {


    val savedNews:LiveData<List<Article>> = repo.savedArticles.asLiveData(viewModelScope.coroutineContext)
    var LoadingState:MutableLiveData<Boolean> = MutableLiveData(false)
    var news:MutableLiveData<List<Article>> = MutableLiveData(null)
    private val channel = Channel<String>(Channel.BUFFERED)
    val toastFlow = channel.receiveAsFlow()


    fun getNews(code:String)
    {
        LoadingState.value = true

        viewModelScope.launch {

              val response = repo.getNewsByCountry(code)
            when(response)
            {
                is Result.Success -> {news.postValue(response.data)}
                is Result.Error -> toastTriggered(response.message)
            }
            LoadingState.value = false
        }

    }

    fun getbyCategory(code:String,category:String)
    {
        LoadingState.value = true

        viewModelScope.launch {

            val response = repo.getNewsByCatagory(code,category)
            when(response)
            {
                is Result.Success -> {news.postValue(response.data)}
                is Result.Error -> toastTriggered(response.message)
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

    fun toastTriggered(message:String?)
    {
        viewModelScope.launch {

            if (message != null)
            {
                channel.send(message)
            }else
            {
                channel.send("error No Connection")
            }

        }
    }





}