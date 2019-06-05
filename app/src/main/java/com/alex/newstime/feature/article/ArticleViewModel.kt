package com.alex.newstime.feature.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alex.newstime.feature.base.BaseViewModel
import com.alex.newstime.repository.article.Article
import com.alex.newstime.util.SingleLiveEvent

class ArticleViewModel : BaseViewModel() {

    private lateinit var article: Article

    var dataState: LiveData<ArticleModel> = MutableLiveData()
    var linkState: LiveData<String> = SingleLiveEvent()
    var closeState: LiveData<Any> = SingleLiveEvent()

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