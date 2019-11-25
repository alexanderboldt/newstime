package com.alex.newstime.repository.api.article

data class ApiArticle(
    val title: String,
    val urlToImage: String,
    val content: String,
    val url: String)