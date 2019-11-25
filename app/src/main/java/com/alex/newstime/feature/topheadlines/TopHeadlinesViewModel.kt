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
import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class TopHeadlinesViewModel : ViewModel() {

    @Inject
    lateinit var articleRepository: ArticleRepository

    private val articles by lazy { ArrayList<Article>() }

    private val pageSize = 10

    private var currentType = Types.GERMANY

    private var currentSelectedArticle: ArticleModel? = null

    val recyclerLoadingSate by lazy<LiveData<Boolean>> { MutableLiveData() }
    val recyclerMessageState by lazy<LiveData<String>> { MutableLiveData() }
    val recyclerArticlesState by lazy<LiveData<List<BaseModel>>> { MutableLiveData() }
    val recyclerLoadMoreState by lazy<LiveData<Boolean>> { MutableLiveData() }
    val recyclerScrollState by lazy<LiveData<Int>> { SingleLiveEvent() }
    val messageState by lazy<LiveData<String>> { SingleLiveEvent() }
    val detailState by lazy<LiveData<Article>> { SingleLiveEvent() }
    val bottomSheetDialogState by lazy<LiveData<Boolean>> { SingleLiveEvent() }

    // ----------------------------------------------------------------------------

    enum class Types {
        GERMANY,
        WORLD_WIDE
    }

    // ----------------------------------------------------------------------------

    init {
        DaggerTopHeadlinesViewModelComponent.create().inject(this)

        // check out when to use the right dispatcher
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
        viewModelScope.launch(Dispatchers.Default) {
            Single.just(currentType)
                .flatMap {
                    when (it) {
                        Types.GERMANY -> articleRepository.getTopHeadlines(pageSize, 1)
                        Types.WORLD_WIDE -> articleRepository.getEverything(pageSize, 1)
                    }
                }
                .doOnSubscribe {
                    articles.clear()
                    (recyclerLoadingSate as MutableLiveData).postValue(true)
                }
                .doOnSuccess { articles.addAll(it) }
                .doFinally { (recyclerLoadingSate as MutableLiveData).postValue(false) }
                .subscribe({
                    if (articles.isEmpty()) {
                        (recyclerMessageState as MutableLiveData).postValue("Articles not available")
                    } else {
                        val uiModels = ArrayList<BaseModel>().apply {
                            addAll(articles.map { ArticleModel(it.id!!, it.title!!, it.urlToImage) })
                            add(LoadMoreModel(true))
                        }
                        (recyclerArticlesState as MutableLiveData).postValue(uiModels)
                    }
                }, {
                    (recyclerMessageState as MutableLiveData).postValue("Could not load articles")

                    Timber.w(it)
                })
        }
    }

    fun refreshArticles(type: Types? = null) {
        if (type != null) {
            articles.clear()
            currentType = type
        }

        viewModelScope.launch(Dispatchers.Default) {
            Single.just(currentType)
                .flatMap {
                    when (it) {
                        Types.GERMANY -> articleRepository.getTopHeadlines(if (articles.size != 0) articles.size else pageSize, 1)
                        Types.WORLD_WIDE -> articleRepository.getEverything(if (articles.size != 0) articles.size else pageSize, 1)
                    }
                }
                .doOnSubscribe { (recyclerLoadingSate as MutableLiveData).postValue(true) }
                .doOnSuccess { response ->
                    articles.apply {
                        clear()
                        addAll(response)
                    }
                }
                .doFinally {
                    (recyclerLoadingSate as MutableLiveData).postValue(false)
                    if (type != null) (recyclerScrollState as MutableLiveData).postValue(0)
                }
                .subscribe({
                    if (articles.isEmpty()) {
                        (recyclerMessageState as MutableLiveData).postValue("Articles not available")
                    } else {
                        val uiModels = ArrayList<BaseModel>().apply {
                            addAll(articles.map { ArticleModel(it.id!!, it.title!!, it.urlToImage) })
                            add(LoadMoreModel(true))
                        }
                        (recyclerArticlesState as MutableLiveData).postValue(uiModels)
                    }
                }, {
                    (recyclerMessageState as MutableLiveData).postValue("Could not load articles")

                    Timber.w(it)
                })
        }
    }

    fun loadMoreArticles() {
        viewModelScope.launch(Dispatchers.Default) {
            Single.just(currentType)
                .flatMap {
                    when (it) {
                        Types.GERMANY -> articleRepository.getTopHeadlines(pageSize, articles.size / pageSize + 1)
                        Types.WORLD_WIDE -> articleRepository.getEverything(pageSize, articles.size / pageSize + 1)
                    }
                }
                .doOnSubscribe {
                    (recyclerLoadingSate as MutableLiveData).postValue(true)
                    (recyclerLoadMoreState as MutableLiveData).postValue(false)
                }
                .doOnSuccess { articles.addAll(it) }
                .doFinally {
                    (recyclerLoadingSate as MutableLiveData).postValue(false)
                    (recyclerLoadMoreState as MutableLiveData).postValue(true)
                    (recyclerScrollState as SingleLiveEvent).postValue(articles.size - 9)
                }
                .subscribe({
                    val uiModels = ArrayList<BaseModel>().apply {
                        addAll(articles.map { ArticleModel(it.id!!, it.title!!, it.urlToImage) })
                        add(LoadMoreModel(true))
                    }
                    (recyclerArticlesState as MutableLiveData).postValue(uiModels)
                }, {
                    Timber.w(it)
                })
        }
    }

    fun clickOnArticle(article: ArticleModel) {
        val foundArticle = articles.firstOrNull {
            it.id == article.id
        }

        if (foundArticle != null) (detailState as SingleLiveEvent).postValue(foundArticle)
    }

    fun longClickArticle(article: ArticleModel) {
        (bottomSheetDialogState as SingleLiveEvent).postValue(true)

        currentSelectedArticle = article
    }

    fun clickAddToFavorites() {
        (bottomSheetDialogState as SingleLiveEvent).postValue(false)

        val foundArticle = articles.first { it.id == currentSelectedArticle?.id }

        viewModelScope.launch(Dispatchers.Default) {
            articleRepository.setFavorite(foundArticle).subscribe({
                (messageState as SingleLiveEvent).postValue("Saved article")
            }, {
                (messageState as SingleLiveEvent).postValue("Could not save article")

                Timber.e(it)
            })
        }
    }
}