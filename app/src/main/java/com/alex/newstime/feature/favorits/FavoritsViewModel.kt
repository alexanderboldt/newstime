package com.alex.newstime.feature.favorits

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alex.core.feature.SingleLiveEvent
import com.alex.newstime.feature.base.BaseViewModel
import com.alex.newstime.feature.favorits.di.DaggerFavoritsViewModelComponent
import com.alex.newstime.feature.favorits.model.ArticleState
import com.alex.newstime.feature.favorits.model.ArticlesState
import com.alex.newstime.repository.article.Article
import com.alex.newstime.repository.article.ArticleRepository
import com.alex.newstime.util.plusAssign
import timber.log.Timber
import javax.inject.Inject

class FavoritsViewModel : BaseViewModel() {

    @Inject lateinit var articleRepository: ArticleRepository

    private val currentArticles by lazy { ArrayList<Article>() }

    private val _recyclerArticlesState = MutableLiveData<ArticlesState>()
    val recyclerArticlesState: LiveData<ArticlesState> = _recyclerArticlesState

    private val _detailState = SingleLiveEvent<Article>()
    val detailState: LiveData<Article> = _detailState

    // ----------------------------------------------------------------------------

    init {
        DaggerFavoritsViewModelComponent.create().inject(this)
    }

    // ----------------------------------------------------------------------------

    fun loadArticles() {
        disposables += articleRepository
                .getFavorites()
                .doOnSubscribe { currentArticles.clear() }
                .subscribe({ articles ->
                    currentArticles.addAll(articles)

                    articles
                        .map { article -> ArticleState(article.id!!, article.title!!, article.urlToImage!!) }
                        .also { _recyclerArticlesState.postValue(it) }
                }, {
                    Timber.w(it)
                })
    }

    fun clickOnArticle(article: ArticleState) {
        currentArticles
            .find { it.id == article.id }
            .also { _detailState.postValue(it) }
    }
}