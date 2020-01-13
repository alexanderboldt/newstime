package com.alex.newstime.feature.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alex.core.feature.SingleLiveEvent
import com.alex.newstime.repository.article.Article

class ArticleViewModel : ViewModel() {

    private lateinit var article: Article

    private val _dataState = MutableLiveData<ArticleModel>()
    val dataState: LiveData<ArticleModel> = _dataState

    private val _linkState = MutableLiveData<String>()
    val linkState: LiveData<String> = _linkState

    private val _closeState = SingleLiveEvent<Unit>()
    val closeState: LiveData<Unit> = _closeState

    // ----------------------------------------------------------------------------

    fun init(article: Article) {
        this.article = article

        _dataState.postValue(
            article.run { ArticleModel(id!!, title!!, urlToImage, content) }
        )
    }

    fun handleClickOnLink() {
        _linkState.postValue(article.url)
    }

    fun handleClickBack() {
        _closeState.postValue(Unit)
    }
}