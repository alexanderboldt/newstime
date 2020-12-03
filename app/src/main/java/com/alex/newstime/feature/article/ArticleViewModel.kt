package com.alex.newstime.feature.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alex.newstime.feature.article.model.UiModelArticle
import com.alex.newstime.repository.models.RpModelArticle
import com.hadilq.liveevent.LiveEvent

class ArticleViewModel : ViewModel() {

    private lateinit var article: RpModelArticle

    private val _dataState = MutableLiveData<UiModelArticle>()
    val dataState: LiveData<UiModelArticle> = _dataState

    private val _linkState = MutableLiveData<String>()
    val linkState: LiveData<String> = _linkState

    private val _closeState = LiveEvent<Unit>()
    val closeState: LiveData<Unit> = _closeState

    // ----------------------------------------------------------------------------

    fun init(article: RpModelArticle) {
        this.article = article

        article
            .run { UiModelArticle(title, urlToImage, content) }
            .also { _dataState.postValue(it) }
    }

    fun handleClickOnLink() {
        _linkState.postValue(article.url)
    }

    fun handleClickBack() {
        _closeState.postValue(Unit)
    }
}