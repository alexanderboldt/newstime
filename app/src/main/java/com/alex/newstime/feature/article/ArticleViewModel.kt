package com.alex.newstime.feature.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alex.newstime.repository.news.Article

class ArticleViewModel : ViewModel() {

    var dataState: LiveData<Article> = MutableLiveData()

    // ----------------------------------------------------------------------------

    fun init(article: Article) {
        (dataState as MutableLiveData).postValue(article)
    }
}