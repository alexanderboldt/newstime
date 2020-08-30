package com.alex.newstime.repository.api.article

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiArticle(
    val title: String,
    val urlToImage: String,
    val content: String,
    val url: String)