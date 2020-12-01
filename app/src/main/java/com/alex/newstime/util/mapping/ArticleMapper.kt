package com.alex.newstime.util.mapping

import com.alex.newstime.repository.api.models.ApiModelArticle
import com.alex.newstime.repository.models.RpModelArticle

object ArticleMapper {

    // from api to repository

    fun fromApiToRepository(input: List<ApiModelArticle>): List<RpModelArticle> {
        return input.map { fromApiToRepository(it) }
    }

    fun fromApiToRepository(input: ApiModelArticle): RpModelArticle {
        return RpModelArticle().apply {
            id = input.title.hashCode().toLong()
            title = input.title
            urlToImage = input.urlToImage
            content = input.content
            url = input.url
        }
    }
}