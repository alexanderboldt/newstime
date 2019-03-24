package com.alex.newstime.repository.news

data class TopHeadlinesResponse(
    var totalResults: Int,
    var articles: List<Article>)