package com.alex.newstime.feature.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alex.newstime.feature.article.model.ArticleState
import com.alex.newstime.repository.article.Article
import com.hadilq.liveevent.LiveEvent

class ArticleViewModel : ViewModel() {

    private lateinit var article: Article

    private val _dataState = MutableLiveData<ArticleState>()
    val dataState: LiveData<ArticleState> = _dataState

    private val _linkState = MutableLiveData<String>()
    val linkState: LiveData<String> = _linkState

    private val _closeState = LiveEvent<Unit>()
    val closeState: LiveData<Unit> = _closeState

    // ----------------------------------------------------------------------------

    fun init(article: Article) {
        this.article = article

        article
            .run { ArticleState(id!!, title!!, urlToImage, content) }
            .also { _dataState.postValue(it) }
    }

    fun handleClickOnLink() {
        _linkState.postValue(article.url)
    }

    fun handleClickBack() {
        _closeState.postValue(Unit)
    }
}