package com.alex.newstime.feature.topheadlines

import androidx.lifecycle.*
import com.alex.core.bus.RxBus
import com.alex.core.feature.SingleLiveEvent
import com.alex.newstime.bus.ConnectivityEvent
import com.alex.newstime.feature.topheadlines.di.DaggerTopHeadlinesViewModelComponent
import com.alex.newstime.repository.article.Article
import com.alex.newstime.repository.article.ArticleRepository
import com.alex.newstime.util.plusAssign
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class TopHeadlinesViewModel : ViewModel() {

    @Inject
    lateinit var articleRepository: ArticleRepository

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

    private val disposables = CompositeDisposable()

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
        loadArticles()
    }

    fun onSwipeRefreshLayout() {
        loadArticles()
    }

    fun loadMoreArticles() {
        disposables += articleRepository.getTopHeadlines(PAGE_SIZE, articles.size / PAGE_SIZE + 1)
            .doOnSubscribe {
                _recyclerLoadingState.postValue(true)
                _recyclerLoadMoreState.postValue(false)
            }
            .doFinally {
                _recyclerLoadingState.postValue(false)
                _recyclerLoadMoreState.postValue(true)
                _recyclerScrollState.postValue(articles.size - 9)
            }
            .subscribe({ articles ->
                this.articles.addAll(articles)

                val uiModels = ArrayList<BaseModel>().also {
                    it.addAll(this.articles.map { ArticleModel(it.id!!, it.title!!, it.urlToImage) })
                    it.add(LoadMoreModel(true))
                }
                _recyclerArticlesState.postValue(uiModels)
            }, {
                _messageState.postValue("Could not load more articles")

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

        val foundArticle = articles.first { it.id == currentSelectedArticle?.id }

        disposables += articleRepository.setFavorite(foundArticle).subscribe({
            _messageState.postValue("Saved article")
        }, {
            _messageState.postValue("Could not save article")

            Timber.w(it)
        })
    }

    // ----------------------------------------------------------------------------

    private fun loadArticles() {
        disposables += articleRepository
            .getTopHeadlines(if (articles.size == 0) PAGE_SIZE else articles.size, 1)
            .doOnSubscribe { _recyclerLoadingState.postValue(true) }
            .doFinally { _recyclerLoadingState.postValue(false) }
            .subscribe({ articles ->
                this.articles.apply {
                    clear()
                    addAll(articles)
                }

                if (this.articles.isEmpty()) {
                    _recyclerMessageState.postValue("Articles not available")
                    return@subscribe
                }

                val uiModels = ArrayList<BaseModel>().also {
                    it.addAll(this.articles.map { ArticleModel(it.id!!, it.title!!, it.urlToImage) })
                    it.add(LoadMoreModel(true))
                }
                _recyclerArticlesState.postValue(uiModels)
            }, {
                _recyclerMessageState.postValue("Could not load articles")

                Timber.w(it)
            })
    }
}