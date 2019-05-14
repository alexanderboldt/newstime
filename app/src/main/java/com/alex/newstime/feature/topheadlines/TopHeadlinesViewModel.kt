package com.alex.newstime.feature.topheadlines

import androidx.lifecycle.MutableLiveData
import com.alex.newstime.feature.BaseViewModel
import com.alex.newstime.repository.news.Article
import com.alex.newstime.repository.news.NewsRepository
import com.alex.newstime.util.SingleLiveEvent
import com.alex.newstime.util.plusAssign

class TopHeadlinesViewModel : BaseViewModel() {

    private lateinit var newsRepository: NewsRepository

    private val articles = ArrayList<Article>()

    private val pageSize = 10

    var recyclerLoadingSate = MutableLiveData<Boolean>()
    var recyclerMessageState = MutableLiveData<String>()
    var recyclerArticlesState = MutableLiveData<List<BaseModel>>()
    var recyclerLoadMoreState = MutableLiveData<Boolean>()
    var detailState = SingleLiveEvent<Article>()

    // ----------------------------------------------------------------------------

    fun setNewsRepository(newsRepository: NewsRepository) {
        this.newsRepository = newsRepository
    }

    // ----------------------------------------------------------------------------

    fun getTopHeadlines() {
        disposables += newsRepository
            .getTopHeadlines(if (articles.size != 0) articles.size else pageSize, 1)
            .doOnSubscribe { recyclerLoadingSate.postValue(true) }
            .doFinally { recyclerLoadingSate.postValue(false) }
            .subscribe({ response ->
                articles.clear()
                articles.addAll(response)

                if (articles.isEmpty()) {
                    recyclerMessageState.postValue("Articles not available")
                } else {
                    val uiModels = ArrayList<BaseModel>()

                    uiModels.addAll(articles.map {
                        ArticleModel(it.id!!, it.title!!, it.urlToImage) as BaseModel
                    })
                    uiModels.add(LoadMoreModel(true))

                    recyclerArticlesState.postValue(uiModels)
                }
            }, {
                recyclerMessageState.postValue("Could not load articles")
            })
    }

    fun clickLoadMore() {
        disposables += newsRepository
            .getTopHeadlines(pageSize, articles.size / pageSize + 1)
            .doOnSubscribe {
                recyclerLoadingSate.postValue(true)
                recyclerLoadMoreState.postValue(false)
            }
            .doFinally {
                recyclerLoadingSate.postValue(false)
                recyclerLoadMoreState.postValue(true)
            }
            .subscribe({ response ->
                articles.addAll(response)

                val uiModels = ArrayList<BaseModel>()

                uiModels.addAll(articles.map {
                    ArticleModel(it.id!!, it.title!!, it.urlToImage) as BaseModel
                })
                uiModels.add(LoadMoreModel(true))

                recyclerArticlesState.postValue(uiModels)
            }, {
            })
    }

    fun clickOnArticle(article: ArticleModel) {
        detailState.postValue(articles.first {
            it.id == article.id
        })
    }
}