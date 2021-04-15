package com.example.mynews.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import okhttp3.Cache
import java.io.File

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