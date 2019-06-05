package com.alex.newstime.feature.topheadlines

sealed class BaseModel

data class ArticleModel(
    var id: Long,
    var title: String,
    var urlToImage: String?) : BaseModel()

class LoadMoreModel(var enabled: Boolean) : BaseModel()