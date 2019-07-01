package com.alex.newstime.feature.topheadlines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alex.newstime.bus.ConnectivityEvent
import com.alex.newstime.bus.RxBus
import com.alex.newstime.feature.base.BaseViewModel
import com.alex.newstime.feature.topheadlines.di.DaggerTopHeadlinesViewModelComponent
import com.alex.newstime.repository.article.Article
import com.alex.newstime.repository.article.ArticleRepository
import com.alex.newstime.util.SingleLiveEvent
import io.reactivex.Single
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class TopHeadlinesViewModel : BaseViewModel() {

    @Inject
    lateinit var articleRepository: ArticleRepository

    private val articles = ArrayList<Article>()

    private val pageSize = 10

    private var currentType = Types.GERMANY

    var recyclerLoadingSate = MutableLiveData<Boolean>()
    var recyclerMessageState = MutableLiveData<String>()
    var recyclerArticlesState = MutableLiveData<List<BaseModel>>()
    var recyclerLoadMoreState = MutableLiveData<Boolean>()
    var recyclerScrollState = SingleLiveEvent<Int>()
    var messageState = SingleLiveEvent<String>()
    var detailState = SingleLiveEvent<Article>()

    // ----------------------------------------------------------------------------

    enum class Types {
        GERMANY,
        WORLD_WIDE
    }

    // ----------------------------------------------------------------------------

    init {
        DaggerTopHeadlinesViewModelComponent.builder().build().inject(this)

        viewModelScope.launch {
            RxBus
                .listen(ConnectivityEvent::class.java)
                .skip(1)
                .filter { it.connected }
                .subscribe {
                    loadInitArticles()
                }
        }
    }
    // ----------------------------------------------------------------------------

    fun loadInitArticles() {
        articles.clear()

        viewModelScope.launch {
            Single.just(currentType)
                .flatMap {
                    when (it) {
                        Types.GERMANY -> articleRepository.getTopHeadlines(pageSize, 1)
                        Types.WORLD_WIDE -> articleRepository.getEverything(pageSize, 1)
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

                    Timber.e(it)
                })
        }
    }

    fun refreshArticles(type: Types? = null) {
        if (type != null) {
            articles.clear()
            currentType = type
        }

        viewModelScope.launch {
            Single.just(currentType)
                .flatMap {
                    when (it) {
                        Types.GERMANY -> articleRepository.getTopHeadlines(if (articles.size != 0) articles.size else pageSize, 1)
                        Types.WORLD_WIDE -> articleRepository.getEverything(if (articles.size != 0) articles.size else pageSize, 1)
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

                    Timber.e(it)
                })
        }
    }

    fun loadMoreArticles() {
        viewModelScope.launch {
            Single.just(currentType)
                .flatMap {
                    when (it) {
                        Types.GERMANY -> articleRepository.getTopHeadlines(pageSize, articles.size / pageSize + 1)
                        Types.WORLD_WIDE -> articleRepository.getEverything(pageSize, articles.size / pageSize + 1)
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
                    Timber.e(it)
                })
        }
    }

    fun clickOnArticle(article: ArticleModel) {
        detailState.postValue(articles.first {
            it.id == article.id
        })
    }

    fun clickOnStar(article: ArticleModel) {
        val foundArticle = articles.first { it.id == article.id }

        viewModelScope.launch {
            articleRepository.setFavorite(foundArticle).subscribe({
                messageState.postValue("Saved article")
            }, {
                messageState.postValue("Could not save article")

                Timber.e(it)
            })
        }
    }
}