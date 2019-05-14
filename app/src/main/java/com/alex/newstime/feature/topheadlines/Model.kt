package com.alex.newstime.feature.topheadlines

sealed class BaseModel

data class ArticleModel(
    var id: String? = null,
    var title: String? = null,
    var urlToImage: String? = null,
    var isLoadMore: Boolean = false) : BaseModel()

class LoadMoreModel : BaseModel()