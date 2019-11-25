package com.alex.newstime.feature.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alex.core.feature.SingleLiveEvent
import com.alex.newstime.repository.article.Article

class ArticleViewModel : ViewModel() {

    private lateinit var article: Article

    val dataState by lazy<LiveData<ArticleModel>> { MutableLiveData() }
    val linkState by lazy<LiveData<String>> { SingleLiveEvent() }
    val closeState by lazy<LiveData<Any>> { SingleLiveEvent() }

    // ----------------------------------------------------------------------------

    fun init(article: Article) {
        this.article = article

        (dataState as MutableLiveData).postValue(
            ArticleModel(
                article.id!!,
                article.title!!,
                article.urlToImage,
                article.content)
        )
    }

    fun handleClickOnLink() {
        (linkState as SingleLiveEvent).postValue(article.url)
    }

    fun handleClickBack() {
        (closeState as SingleLiveEvent).call()
    }
}