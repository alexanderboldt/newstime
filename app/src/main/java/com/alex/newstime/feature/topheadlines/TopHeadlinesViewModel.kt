package com.alex.newstime.feature.topheadlines

import androidx.lifecycle.*
import com.alex.newstime.R
import com.alex.newstime.feature.base.ResourceProvider
import com.alex.newstime.feature.topheadlines.model.RecyclerViewState
import com.alex.newstime.feature.topheadlines.model.UiModelRecyclerItem
import com.alex.newstime.repository.models.RpModelArticle
import com.alex.newstime.repository.article.ArticleRepository
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class TopHeadlinesViewModel(
    private val articleRepository: ArticleRepository,
    private val resourceProvider: ResourceProvider) : ViewModel() {

    private val articles by lazy { ArrayList<RpModelArticle>() }

    private val PAGE_SIZE = 10

    // ----------------------------------------------------------------------------
    // states

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _recyclerViewState = MutableLiveData<RecyclerViewState>()
    val recyclerViewState: LiveData<RecyclerViewState> = _recyclerViewState

    private val _messageState = LiveEvent<String>()
    val messageState: LiveData<String> = _messageState

    private val _detailState = LiveEvent<RpModelArticle>()
    val detailState: LiveData<RpModelArticle> = _detailState

    // ----------------------------------------------------------------------------

    fun init() {
        loadArticles()
    }

    fun onSwipeRefreshLayout() {
        loadArticles()
    }

    fun loadMoreArticles() {
        viewModelScope.launch(Dispatchers.Main) {

            _loadingState.postValue(true)

            articleRepository
                .getTopHeadlines(PAGE_SIZE, articles.size / PAGE_SIZE + 1)
                .catch {

                    Timber.w(it)
                }.collect { newArticles ->
                    articles.addAll(newArticles)

                    val uiModels = ArrayList<UiModelRecyclerItem>().also {
                        it.addAll(articles.map { UiModelRecyclerItem.UiModelArticle(it.id!!, it.title!!, it.urlToImage) })
                        it.add(UiModelRecyclerItem.UiModelLoadMore())
                    }

                    _recyclerViewState.postValue(RecyclerViewState.ArticlesState(uiModels))
                }

            _loadingState.postValue(false)
        }
    }

    fun clickOnArticle(article: UiModelRecyclerItem.UiModelArticle) {
        articles
            .firstOrNull { it.id == article.id }
            ?.also { _detailState.postValue(it) }
    }

    // ----------------------------------------------------------------------------

    private fun loadArticles() {
        viewModelScope.launch(Dispatchers.Main) {
            _loadingState.postValue(true)

            articleRepository
                .getTopHeadlines(if (articles.size == 0) PAGE_SIZE else articles.size, 1)
                .catch {
                    _recyclerViewState.postValue(RecyclerViewState.MessageState(resourceProvider.getString(R.string.top_headlines_error_load_articles)))

                    Timber.w(it)
                }.collect { newArticles ->
                    articles.apply {
                        clear()
                        addAll(newArticles)
                    }

                    if (articles.isEmpty()) {
                        _recyclerViewState.postValue(RecyclerViewState.MessageState(resourceProvider.getString(R.string.top_headlines_no_articles)))
                        return@collect
                    }

                    ArrayList<UiModelRecyclerItem>()
                        .also {
                            it.addAll(articles.map {
                                UiModelRecyclerItem.UiModelArticle(
                                    it.id!!,
                                    it.title!!,
                                    it.urlToImage
                                )
                            })
                            it.add(UiModelRecyclerItem.UiModelLoadMore())
                        }.also {
                            _recyclerViewState.postValue(RecyclerViewState.ArticlesState(it))
                        }
                }

            _loadingState.postValue(false)
        }
    }
}