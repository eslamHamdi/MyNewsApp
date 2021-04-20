package com.example.mynews.api

import android.annotation.SuppressLint
import android.content.Context
import com.example.mynews.utils.cache
import com.example.mynews.utils.isNetworkConnected
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object NewsClient {

    private val HEADER_CACHE_CONTROL = "Cache-Control"
   private val HEADER_PRAGMA = "Pragma"
    private val BASE_URL = "https://newsapi.org/v2/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val interceptor = Interceptor { chain ->
        var request: Request = chain.request()

        val url = request.url().newBuilder().addQueryParameter(
            "apiKey",
            "73dade22420a47f8afca5ddfbd5a7b6f"
        ).build()

        request = request.newBuilder().url(url)
            .build()

        chain.proceed(request)
    }

    fun onlineInterceptor(): Interceptor {
        return Interceptor { chain ->
            //Log.d(TAG, "network interceptor: called.")
            val response: Response = chain.proceed(chain.request())
            val cacheControl = CacheControl.Builder()
                .maxAge(5, TimeUnit.SECONDS)
                .build()
            response.newBuilder()
                .removeHeader(HEADER_PRAGMA)
                .removeHeader(HEADER_CACHE_CONTROL)
                .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                .build()
        }
    }

    //@RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("NewApi")
    fun offlineInterceptor(context: Context): Interceptor {
        return Interceptor { chain ->
            //Log.d(TAG, "offline interceptor: called.")
            var request = chain.request()

            // prevent caching when network is on. For that we use the "networkInterceptor"
            if (!isNetworkConnected(context))
            {
                val cacheControl = CacheControl.Builder()
                    .maxStale(7, TimeUnit.DAYS)
                    .build()
                request = request.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .cacheControl(cacheControl)
                    .build()
            }
            chain.proceed(request)
        }
    }

    private fun HTTPCLient(context: Context):OkHttpClient
    {
        return OkHttpClient.Builder()
            .cache(cache(context))
            .addNetworkInterceptor(onlineInterceptor())
            .addInterceptor(offlineInterceptor(context))
            .addInterceptor(interceptor)
            .build()
    }

    fun get(context: Context):Retrofit
    {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(HTTPCLient(context))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}