package com.alex.newstime.feature.topheadlines

import androidx.lifecycle.MutableLiveData
import com.alex.newstime.feature.BaseViewModel
import com.alex.newstime.repository.news.Article
import com.alex.newstime.repository.news.NewsRepository
import com.alex.newstime.util.SingleLiveEvent
import com.alex.newstime.util.plusAssign

class TopHeadlinesViewModel : BaseViewModel() {

    private lateinit var newsRepository: NewsRepository

    var recyclerLoadingSate = MutableLiveData<Boolean>()
    var recyclerMessageState = MutableLiveData<String>()
    var recyclerArticlesState = MutableLiveData<List<UiArticle>>()
    var detailState = SingleLiveEvent<Article>()

    // ----------------------------------------------------------------------------

    fun setNewsRepository(newsRepository: NewsRepository) {
        this.newsRepository = newsRepository
    }

    // ----------------------------------------------------------------------------

    fun getTopHeadlines() {
        disposables += newsRepository
            .getTopHeadlines()
            .doOnSubscribe {
                recyclerLoadingSate.postValue(true)
            }
            .doFinally {
                recyclerLoadingSate.postValue(false)
            }
            .subscribe({ response ->
                if (response.isEmpty()) {
                    recyclerMessageState.postValue("Articles not available")
                } else {
                    recyclerArticlesState.postValue(response.map { UiArticle(it.title!!, it.urlToImage) })
                }
            }, {
                recyclerMessageState.postValue("Could not load articles")
            })
    }

    fun clickOnArticle(article: UiArticle) {
        //(detailState as SingleLiveEvent).postValue(article)
    }
}