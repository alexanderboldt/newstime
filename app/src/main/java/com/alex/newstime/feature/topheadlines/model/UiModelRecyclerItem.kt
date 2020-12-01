package com.alex.newstime.feature.topheadlines.model

sealed class UiModelRecyclerItem {
    data class UiModelArticle(
        val id: Long,
        val title: String,
        val urlToImage: String?) : UiModelRecyclerItem()

    class UiModelLoadMore  : UiModelRecyclerItem()
}