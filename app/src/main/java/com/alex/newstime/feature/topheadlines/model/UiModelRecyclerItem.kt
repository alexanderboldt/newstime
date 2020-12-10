package com.alex.newstime.feature.topheadlines.model

import androidx.annotation.ColorInt

sealed class UiModelRecyclerItem {
    class UiModelPlaceholder(@ColorInt val color: Int) : UiModelRecyclerItem()

    data class UiModelArticle(
        val source: String,
        val title: String,
        @ColorInt val colorPlaceholder: Int,
        val urlToImage: String?,
        val date: String) : UiModelRecyclerItem()

    class UiModelLoadMore(var isEnabled: Boolean)  : UiModelRecyclerItem()
}