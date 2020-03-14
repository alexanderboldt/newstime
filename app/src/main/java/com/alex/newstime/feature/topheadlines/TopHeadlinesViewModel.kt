package com.alex.newstime.feature.topheadlines

import androidx.lifecycle.*
import com.alex.core.bus.RxBus
import com.alex.core.feature.SingleLiveEvent
import com.alex.newstime.R
import com.alex.newstime.bus.ConnectivityEvent
import com.alex.newstime.feature.base.BaseViewModel
import com.alex.newstime.feature.base.ResourceProvider
import com.alex.newstime.feature.topheadlines.model.ArticleModel
import com.alex.newstime.feature.topheadlines.model.BaseModel
import com.alex.newstime.feature.topheadlines.model.LoadMoreModel
import com.alex.newstime.repository.article.Article
import com.alex.newstime.repository.article.ArticleRepository
import com.alex.newstime.util.plusAssign
import timber.log.Timber

class TopHeadlinesViewModel : BaseViewModel() {

    private val articleRepository by lazy { ArticleRepository() }

    private val resourceProvider by lazy { ResourceProvider }

    private val articles by lazy { ArrayList<Article>() }

    private val PAGE_SIZE = 10

    private var currentSelectedArticle: ArticleModel? = null

    private val _recyclerLoadingState = MutableLiveData<Boolean>()
    val recyclerLoadingState: LiveData<Boolean> = _recyclerLoadingState

    private val _recyclerMessageState = MutableLiveData<String>()
    val recyclerMessageState: LiveData<String> = _recyclerMessageState

    private val _recyclerArticlesState = MutableLiveData<List<BaseModel>>()
    val recyclerArticlesState: LiveData<List<BaseModel>> = _recyclerArticlesState

    private val _recyclerLoadMoreState = MutableLiveData<Boolean>()
    val recyclerLoadMoreState: LiveData<Boolean> = _recyclerLoadMoreState

    private val _recyclerScrollState = SingleLiveEvent<Int>()
    val recyclerScrollState: LiveData<Int> = _recyclerScrollState

    private val _messageState = SingleLiveEvent<String>()
    val messageState: LiveData<String> = _messageState

    private val _detailState = SingleLiveEvent<Article>()
    val detailState: LiveData<Article> = _detailState

    private val _bottomSheetDialogState = SingleLiveEvent<Boolean>()
    val bottomSheetDialogState: LiveData<Boolean> = _bottomSheetDialogState

    // ----------------------------------------------------------------------------

    init {
        disposables += RxBus
                .listen(ConnectivityEvent::class.java)
                .skip(1)
                .filter { it.connected }
                .subscribe {
                    init()
                }
    }

    // ----------------------------------------------------------------------------

    fun init() {
        loadArticles()
    }

    fun onSwipeRefreshLayout() {
        loadArticles()
    }

    fun loadMoreArticles() {
        disposables += articleRepository
                .getTopHeadlines(PAGE_SIZE, articles.size / PAGE_SIZE + 1)
                .doOnSubscribe {
                    _recyclerLoadingState.postValue(true)
                    _recyclerLoadMoreState.postValue(false)
                }
                .doFinally {
                    _recyclerLoadingState.postValue(false)
                    _recyclerLoadMoreState.postValue(true)
                    _recyclerScrollState.postValue(articles.size - 9)
                }
                .subscribe({ newArticles ->
                    articles.addAll(newArticles)

                    val uiModels = ArrayList<BaseModel>().also {
                        it.addAll(articles.map { ArticleModel(it.id!!, it.title!!, it.urlToImage ) })
                        it.add(LoadMoreModel(true))
                    }
                    _recyclerArticlesState.postValue(uiModels)
                }, {
                    Timber.w(it)
                })
    }

    fun clickOnArticle(article: ArticleModel) {
        articles
            .firstOrNull { it.id == article.id }
            ?.also { _detailState.postValue(it) }
    }

    fun longClickArticle(article: ArticleModel) {
        _bottomSheetDialogState.postValue(true)

        currentSelectedArticle = article
    }

    fun clickAddToFavorites() {
        _bottomSheetDialogState.postValue(false)

        articles
            .find { it.id == currentSelectedArticle?.id }
            ?.also { article ->
                disposables += articleRepository
                    .setFavorite(article)
                    .subscribe({
                        _messageState.postValue(resourceProvider.getString(R.string.top_headlines_article_saved))
                    }, {
                        _messageState.postValue(resourceProvider.getString(R.string.top_headlines_article_could_not_save))

                        Timber.w(it)
                    })
            }
    }

    // ----------------------------------------------------------------------------

    private fun loadArticles() {
        disposables += articleRepository
                .getTopHeadlines(if (articles.size == 0) PAGE_SIZE else articles.size, 1)
                .doOnSubscribe { _recyclerLoadingState.postValue(true) }
                .doFinally { _recyclerLoadingState.postValue(false) }
                .subscribe({ newArticles ->
                    articles.apply {
                        clear()
                        addAll(newArticles)
                    }

                    if (articles.isEmpty()) {
                        _recyclerMessageState.postValue(resourceProvider.getString(R.string.top_headlines_no_articles))
                        return@subscribe
                    }

                    ArrayList<BaseModel>()
                        .also {
                            it.addAll(articles.map {
                                ArticleModel(
                                    it.id!!,
                                    it.title!!,
                                    it.urlToImage
                                )
                            })
                            it.add(LoadMoreModel(true))
                        }
                        .apply {
                            _recyclerArticlesState.postValue(this)
                        }
                }, {
                    _recyclerMessageState.postValue(resourceProvider.getString(R.string.top_headlines_error_load_articles))

                    Timber.w(it)
                })
    }
}