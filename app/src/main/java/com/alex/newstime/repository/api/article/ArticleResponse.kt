package com.alex.newstime.repository.api.article

data class ArticleResponse(
    var totalResults: Int,
    var articles: List<ApiArticle>)