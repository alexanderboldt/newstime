package com.alex.newstime.repository.models

data class RpModelResponse(
    val totalResults: Int,
    val articles: List<RpModelArticle>)