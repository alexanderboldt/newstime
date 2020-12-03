package com.alex.newstime.repository.api.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiModelArticle(
    val title: String,
    val urlToImage: String?,
    val content: String?,
    val url: String,
    val publishedAt: String,
    val source: ApiModelSource)