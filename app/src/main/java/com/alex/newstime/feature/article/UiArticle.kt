package com.alex.newstime.feature.article

data class UiArticle(
    var id: Long,
    var title: String,
    var urlToImage: String? = null,
    var content: String? = null)