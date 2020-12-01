package com.alex.newstime.feature.topheadlines.model

sealed class RecyclerViewState {
    data class ArticlesState(val items: List<UiModelRecyclerItem>) : RecyclerViewState()
    data class MessageState(val message: String) : RecyclerViewState()
}