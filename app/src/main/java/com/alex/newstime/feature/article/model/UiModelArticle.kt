package com.alex.newstime.feature.article.model

data class UiModelArticle(
    var id: Long,
    var title: String,
    var urlToImage: String? = null,
    var content: String? = null)