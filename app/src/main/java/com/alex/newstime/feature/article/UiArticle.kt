package com.alex.newstime.feature.article

data class UiArticle(
    var id: String,
    var title: String,
    var urlToImage: String? = null,
    var content: String? = null)