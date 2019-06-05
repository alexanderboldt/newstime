package com.alex.newstime.feature.article

data class ArticleModel(
    var id: Long,
    var title: String,
    var urlToImage: String? = null,
    var content: String? = null)