package com.alex.newstime.feature.topheadlines

import androidx.lifecycle.MutableLiveData
import com.alex.newstime.feature.base.BaseViewModel
import com.alex.newstime.repository.news.Article
import com.alex.newstime.repository.news.NewsRepository
import com.alex.newstime.util.SingleLiveEvent
import com.alex.newstime.util.plusAssign
import io.reactivex.Single

class TopHeadlinesViewModel : BaseViewModel() {

    private lateinit var newsRepository: NewsRepository

    private val articles = ArrayList<Article>()

    private val pageSize = 10

    private var currentType = Types.GERMANY

    var recyclerLoadingSate = MutableLiveData<Boolean>()
    var recyclerMessageState = MutableLiveData<String>()
    var recyclerArticlesState = MutableLiveData<List<BaseModel>>()
    var recyclerLoadMoreState = MutableLiveData<Boolean>()
    var recyclerScrollState = SingleLiveEvent<Int>()
    var detailState = SingleLiveEvent<Article>()

    // ----------------------------------------------------------------------------

    enum class Types {
        GERMANY,
        WORLD_WIDE
    }

    // ----------------------------------------------------------------------------

    fun setNewsRepository(newsRepository: NewsRepository) {
        this.newsRepository = newsRepository
    }

    // ----------------------------------------------------------------------------

    fun loadInitArticles() {
        articles.clear()

        disposables += Single.just(currentType)
            .flatMap {
                when (it) {
                    Types.GERMANY -> newsRepository.getTopHeadlines(pageSize, 1)
                    Types.WORLD_WIDE -> newsRepository.getEverything(pageSize, 1)
                }
            }
            .doOnSubscribe { recyclerLoadingSate.postValue(true) }
            .doFinally {
                recyclerLoadingSate.postValue(false)
            }
            .subscribe({ response ->
                articles.addAll(response)

                if (articles.isEmpty()) {
                    recyclerMessageState.postValue("Articles not available")
                } else {
                    val uiModels = ArrayList<BaseModel>()

                    uiModels.apply {
                        addAll(articles.map { ArticleModel(it.id!!, it.title!!, it.urlToImage) as BaseModel })
                        add(LoadMoreModel(true))
                    }
                    recyclerArticlesState.postValue(uiModels)
                }
            }, {
                recyclerMessageState.postValue("Could not load articles")
            })
    }

    fun refreshArticles(type: Types? = null) {
        if (type != null) {
            articles.clear()
            currentType = type
        }

        disposables += Single.just(currentType)
            .flatMap {
                when (it) {
                    Types.GERMANY -> newsRepository.getTopHeadlines(if (articles.size != 0) articles.size else pageSize, 1)
                    Types.WORLD_WIDE -> newsRepository.getEverything(if (articles.size != 0) articles.size else pageSize, 1)
                }
            }
            .doOnSubscribe { recyclerLoadingSate.postValue(true) }
            .doFinally {
                recyclerLoadingSate.postValue(false)
                if (type != null) recyclerScrollState.postValue(0)
            }
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

    fun loadMoreArticles() {
        disposables += Single.just(currentType)
            .flatMap {
                when (it) {
                    Types.GERMANY -> newsRepository.getTopHeadlines(pageSize, articles.size / pageSize + 1)
                    Types.WORLD_WIDE -> newsRepository.getEverything(pageSize, articles.size / pageSize + 1)
                }
            }
            .doOnSubscribe {
                recyclerLoadingSate.postValue(true)
                recyclerLoadMoreState.postValue(false)
            }
            .doFinally {
                recyclerLoadingSate.postValue(false)
                recyclerLoadMoreState.postValue(true)
                recyclerScrollState.postValue(articles.size - 9)
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