package com.alex.newstime.repository.api.article

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiModelResponse(
    var totalResults: Int,
    var articles: List<ApiModelArticle>)