package com.alex.newstime.util.mapping

import com.alex.newstime.repository.api.models.ApiModelArticle
import com.alex.newstime.repository.models.RpModelArticle

object ArticleMapper {

    // from api to repository

    fun fromApiToRepository(input: List<ApiModelArticle>): List<RpModelArticle> {
        return input.map { fromApiToRepository(it) }
    }

    fun fromApiToRepository(input: ApiModelArticle): RpModelArticle {
        return RpModelArticle(input.title, input.urlToImage, input.content, input.url, input.publishedAt, input.source.name)
    }
}