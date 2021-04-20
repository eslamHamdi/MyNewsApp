package com.example.mynews.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article( val url: String? = null,

                    val publishDate: String? = null,

                    val author: String? = null,

                   val urlToImage: String? = null,

                  val articleDescription: String? = null,


                   val source: String? = null,

                   val articleTitle: String? = null,

                    val articleContent: String? = null) : Parcelable


