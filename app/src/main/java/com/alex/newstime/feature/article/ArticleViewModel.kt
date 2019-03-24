package com.alex.newstime.feature.article

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.alex.newstime.repository.news.Article

class ArticleViewModel : ViewModel() {

    var dataState: LiveData<Article> = MutableLiveData()

    // ----------------------------------------------------------------------------

    fun init(article: Article) {
        (dataState as MutableLiveData).postValue(article)
    }
}