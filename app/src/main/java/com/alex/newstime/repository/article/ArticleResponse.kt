package com.alex.newstime.repository.article

data class ArticleResponse(
    var totalResults: Int,
    var articles: List<Article>)