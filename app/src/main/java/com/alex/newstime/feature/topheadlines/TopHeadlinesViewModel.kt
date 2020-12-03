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
import org.threeten.bp.Instant
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import kotlin.math.max

class TopHeadlinesViewModel(
    private val articleRepository: ArticleRepository,
    private val resourceProvider: ResourceProvider) : ViewModel() {

    private val articles = mutableListOf<RpModelArticle>()

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

    fun clickOnArticle(article: UiModelRecyclerItem.UiModelArticle) {
        articles
            .firstOrNull { it.title == article.title }
            ?.also { _detailState.postValue(it) }
    }

    fun clickOnLoadMore() {
        viewModelScope.launch(Dispatchers.Main) {
            _loadingState.postValue(true)

            _recyclerViewState.postValue(RecyclerViewState.ArticlesState(getUiItems(false)))

            articleRepository
                .getTopHeadlines(PAGE_SIZE, articles.size / PAGE_SIZE + 1)
                .catch {
                    _recyclerViewState.postValue(RecyclerViewState.ArticlesState(getUiItems(true)))

                    Timber.w(it)
                }.collect { newArticles ->
                    articles.addAll(newArticles)

                    _recyclerViewState.postValue(RecyclerViewState.ArticlesState(getUiItems(true)))
                }

            _loadingState.postValue(false)
        }
    }

    // ----------------------------------------------------------------------------

    private fun loadArticles() {
        viewModelScope.launch(Dispatchers.Main) {
            _loadingState.postValue(true)

            _recyclerViewState.postValue(RecyclerViewState.ArticlesState(getUiItems(false)))

            articleRepository
                .getTopHeadlines(max(articles.size, PAGE_SIZE), 1)
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

                    _recyclerViewState.postValue(RecyclerViewState.ArticlesState(getUiItems(true)))
                }

            _loadingState.postValue(false)
        }
    }

    // ----------------------------------------------------------------------------

    private fun formatPublishDate(publishedAt: String): String {
        return Instant
            .parse(publishedAt)
            .atOffset(ZoneOffset.UTC)
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }

    private fun getUiItems(isLoadMoreEnabled: Boolean): List<UiModelRecyclerItem> {
        return mutableListOf<UiModelRecyclerItem>().apply {
            addAll(articles.map {
                UiModelRecyclerItem.UiModelArticle(
                    it.source,
                    it.title,
                    it.urlToImage,
                    formatPublishDate(it.publishedAt)
                )
            })
            add(UiModelRecyclerItem.UiModelLoadMore(isLoadMoreEnabled))
        }
    }
}