package com.alex.newstime.feature.topheadlines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.core.bus.RxBus
import com.alex.core.feature.SingleLiveEvent
import com.alex.newstime.bus.ConnectivityEvent
import com.alex.newstime.feature.topheadlines.di.DaggerTopHeadlinesViewModelComponent
import com.alex.newstime.repository.article.Article
import com.alex.newstime.repository.article.ArticleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class TopHeadlinesViewModel : ViewModel() {

    @Inject
    lateinit var articleRepository: ArticleRepository

    private val articles by lazy { ArrayList<Article>() }

    private val pageSize = 10

    private var currentSelectedArticle: ArticleModel? = null

    private val _recyclerLoadingSate = MutableLiveData<Boolean>()
    val recyclerLoadingSate: LiveData<Boolean> = _recyclerLoadingSate

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
        DaggerTopHeadlinesViewModelComponent.create().inject(this)

        viewModelScope.launch {
            RxBus
                .listen(ConnectivityEvent::class.java)
                .skip(1)
                .filter { it.connected }
                .subscribe {
                    init()
                }
        }
    }
    // ----------------------------------------------------------------------------

    fun init() {
        viewModelScope.launch(Dispatchers.Default) {
            articleRepository.getTopHeadlines(pageSize, 1)
                .doOnSubscribe {
                    articles.clear()
                    _recyclerLoadingSate.postValue(true)
                }
                .doOnSuccess { articles.addAll(it) }
                .doFinally { _recyclerLoadingSate.postValue(false) }
                .subscribe({
                    if (articles.isEmpty()) {
                        _recyclerMessageState.postValue("Articles not available")
                        return@subscribe
                    }

                    val uiModels = ArrayList<BaseModel>().apply {
                        addAll(articles.map { ArticleModel(it.id!!, it.title!!, it.urlToImage) })
                        add(LoadMoreModel(true))
                    }
                    _recyclerArticlesState.postValue(uiModels)
                }, {
                    _recyclerMessageState.postValue("Could not load articles")

                    Timber.w(it)
                })
        }
    }

    fun onSwipeRefreshLayout() {
        viewModelScope.launch(Dispatchers.Default) {
            articleRepository.getTopHeadlines(if (articles.size != 0) articles.size else pageSize, 1)
                .doOnSubscribe { _recyclerLoadingSate.postValue(true) }
                .doOnSuccess { response ->
                    articles.apply {
                        clear()
                        addAll(response)
                    }
                }
                .doFinally {
                    _recyclerLoadingSate.postValue(false)
                }
                .subscribe({
                    if (articles.isEmpty()) {
                        _recyclerMessageState.postValue("Articles not available")
                    } else {
                        val uiModels = ArrayList<BaseModel>().apply {
                            addAll(articles.map { ArticleModel(it.id!!, it.title!!, it.urlToImage) })
                            add(LoadMoreModel(true))
                        }
                        _recyclerArticlesState.postValue(uiModels)
                    }
                }, {
                    _recyclerMessageState.postValue("Could not load articles")

                    Timber.w(it)
                })
        }
    }

    fun loadMoreArticles() {
        viewModelScope.launch(Dispatchers.Default) {
            articleRepository.getTopHeadlines(pageSize, articles.size / pageSize + 1)
                .doOnSubscribe {
                    _recyclerLoadingSate.postValue(true)
                    _recyclerLoadMoreState.postValue(false)
                }
                .doOnSuccess { articles.addAll(it) }
                .doFinally {
                    _recyclerLoadingSate.postValue(false)
                    _recyclerLoadMoreState.postValue(true)
                    _recyclerScrollState.postValue(articles.size - 9)
                }
                .subscribe({
                    val uiModels = ArrayList<BaseModel>().apply {
                        addAll(articles.map { ArticleModel(it.id!!, it.title!!, it.urlToImage) })
                        add(LoadMoreModel(true))
                    }
                    _recyclerArticlesState.postValue(uiModels)
                }, {
                    Timber.w(it)
                })
        }
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

        viewModelScope.launch(Dispatchers.Default) {

            val foundArticle = articles.first { it.id == currentSelectedArticle?.id }

            articleRepository.setFavorite(foundArticle).subscribe({
                _messageState.postValue("Saved article")
            }, {
                _messageState.postValue("Could not save article")

                Timber.e(it)
            })
        }
    }
}