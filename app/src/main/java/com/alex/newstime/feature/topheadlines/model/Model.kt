package com.alex.newstime.feature.topheadlines.model

sealed class BaseModel

data class ArticleModel(
    var id: Long,
    var title: String,
    var urlToImage: String?) : BaseModel()

data class LoadMoreModel(var enabled: Boolean) : BaseModel()