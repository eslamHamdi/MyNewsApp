package com.example.mynews.ui.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.mynews.domain.Article
import com.example.mynews.dto.Result
import com.example.mynews.repository.DataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class NewsViewModel(private val repo:DataSource):ViewModel() {



    var loadingState:MutableLiveData<Boolean> = MutableLiveData(false)
    var news:MutableLiveData<List<Article>> = MutableLiveData(null)
    val searchNews: MutableLiveData<List<Article>> = MutableLiveData()
    private val channel = Channel<String>(Channel.BUFFERED)
    val toastFlow = channel.receiveAsFlow()
    var fragmentCreatedToast = true




    fun getNews(code:String)
    {
        loadingState.value = true


        viewModelScope.launch {
            try
            {
                when(val response = repo.getNewsByCountry(code))
                {
                    is Result.Success -> {news.value = (response.data)}
                    is Result.Error -> toastTriggered("error:" + response.message)
                }
                //Log.e(null, "getNews: ${news.value}", )

            }catch (e:Exception)
            {
               if (fragmentCreatedToast)
             {
                    toastTriggered("Connection error!!")
                    fragmentCreatedToast = false
                }

                Log.e(null, "getNews: $e" )
            }
            loadingState.value = false
        }

    }

    fun getbyCategory(code:String,category:String)
    {
        loadingState.value = true

        viewModelScope.launch {
try
{
    val response = repo.getNewsByCatagory(code,category)
    when(response)
    {
        is Result.Success -> {news.postValue(response.data)}
        is Result.Error -> toastTriggered("error:" + response.message)
    }
}catch (e:Exception)
{

        toastTriggered("Connection error!!")
    Log.e(null, "getbyCategory: $e")
}

            loadingState.value = false
        }


    }

    fun addToFavorites(article:Article)
    {
        viewModelScope.launch {
            try {
                repo.saveArticle(article)
                toastTriggered("Article Saved!!")
            }catch (e:Exception)
            {
                toastTriggered("Saving Failed!!")
            }


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

    fun newsSearch(kewWord:String)
    {
        loadingState.value = true
        viewModelScope.launch {

            try
            {
                val response = repo.NewsSearch(kewWord)
                when(response)
                {
                    is Result.Success -> {searchNews.postValue(response.data)}
                    is Result.Error -> toastTriggered("error:" + response.message)
                }
            }catch (e:Exception)
            {
                toastTriggered("Connection error!!")
                Log.e(null, "newsSearch: $e")
            }

            loadingState.value = false
        }
        }



    fun toastTriggered(message:String?)
    {
        viewModelScope.launch {

            if (message != null)
            {
                channel.send(message)
            }

        }
    }


    fun returnSavedArticles():LiveData<List<Article>>
      {
       return repo.getSavedArticles().asLiveData()

   }





}