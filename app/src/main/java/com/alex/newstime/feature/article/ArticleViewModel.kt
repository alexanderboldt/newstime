package com.alex.newstime.feature.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alex.newstime.repository.article.Article
import com.alex.newstime.util.SingleLiveEvent

class ArticleViewModel : ViewModel() {

    private lateinit var article: Article

    var dataState: LiveData<UiArticle> = MutableLiveData()
    var linkState: LiveData<String> = SingleLiveEvent()

    // ----------------------------------------------------------------------------

    fun init(article: Article) {
        this.article = article

        (dataState as MutableLiveData).postValue(
            UiArticle(
                article.id!!,
                article.title!!,
                article.urlToImage,
                article.content)
        )
    }

    fun handleLinkClick() {
        (linkState as SingleLiveEvent).postValue(article.url)
    }
}