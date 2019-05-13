package com.alex.newstime.feature.topheadlines

data class UiArticle(
    var id: String,
    var title: String,
    var urlToImage: String? = null)