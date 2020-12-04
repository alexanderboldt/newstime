package com.alex.newstime.feature.article.model

data class UiModelArticle(
    val urlToImage: String?,
    val source: String,
    val date: String,
    val title: String,
    val content: String?)