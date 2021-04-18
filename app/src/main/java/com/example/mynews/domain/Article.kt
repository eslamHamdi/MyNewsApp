package com.example.mynews.domain

import com.example.mynews.dto.Source

data class Article( val url: String? = null,

                    val publishDate: String? = null,

                    val author: String? = null,

                   val urlToImage: String? = null,

                  val articleDescription: String? = null,

                   val source: Source? = null,

                   val articleTitle: String? = null,

                    val articleContent: String? = null)
