package com.example.mynews.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.mynews.database.ArticleEntity
import com.example.mynews.domain.Article
import com.example.mynews.dto.ArticlesItem
import com.example.mynews.dto.Source
import okhttp3.Cache
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.M)
fun isNetworkConnected(context: Context): Boolean
{
    var result = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities)
                ?: return false
        result = when
        {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

    } else
    {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type)
                {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
    }
    return result
}

fun cache(context: Context): Cache
{

    val cacheSize = 10 * 1024 * 1024 // 10 MB
    val httpCacheDirectory = File(context.cacheDir, "http-cache")
    val cache = Cache(httpCacheDirectory, cacheSize.toLong())

    return cache
}


fun List<ArticleEntity>.entityToDomain():List<Article>
{
    return map {

        Article(url = it.url, publishDate = it.publishedAt, author = it.author, urlToImage = it.urlToImage,
                articleDescription = it.description, source = Source(it.source), articleTitle = it.title, articleContent = it.content)

    }
}

    fun List<ArticlesItem?>?.dtoToDomain():List<Article>?
    {
        return this?.map {

            Article(url = it?.url, publishDate = it?.publishedAt, author = it?.author, urlToImage = it?.urlToImage,
                articleDescription = it?.description, source = it?.source, articleTitle = it?.title, articleContent = it?.content)
        }
    }

fun Article.domainToEntity(): ArticleEntity
{
    return ArticleEntity(url = this.url, publishedAt = this.publishDate, author = this.author,

            urlToImage = this.urlToImage, description =this.articleDescription, source = this.source?.name,

            title = this.articleTitle, content = this.articleContent)



}

    fun parseDate(
            inputDateString: String?,
            inputDateFormat: SimpleDateFormat,
            outputDateFormat: SimpleDateFormat
    ): String? {
        var date: Date? = null
        var outputDateString: String? = null
        try {
            date = inputDateFormat.parse(inputDateString!!)
            outputDateString = outputDateFormat.format(date!!)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return outputDateString
    }


