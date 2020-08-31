package com.alex.newstime.feature.article.model

data class ArticleState(
    var id: Long,
    var title: String,
    var urlToImage: String? = null,
    var content: String? = null)