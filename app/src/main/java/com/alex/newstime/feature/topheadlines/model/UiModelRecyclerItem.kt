package com.alex.newstime.feature.topheadlines.model

sealed class UiModelRecyclerItem {
    data class UiModelArticle(
        val source: String,
        val title: String,
        val urlToImage: String?,
        val date: String) : UiModelRecyclerItem()

    class UiModelLoadMore(var isEnabled: Boolean)  : UiModelRecyclerItem()
}