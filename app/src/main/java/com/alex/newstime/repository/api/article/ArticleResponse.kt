package com.alex.newstime.repository.api.article

import com.alex.newstime.repository.article.Article

data class ArticleResponse(
    var totalResults: Int,
    var articles: List<Article>)