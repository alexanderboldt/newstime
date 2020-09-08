package com.alex.newstime.feature.favorits

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alex.newstime.feature.base.BaseViewModel
import com.alex.newstime.feature.favorits.model.ArticleState
import com.alex.newstime.feature.favorits.model.ArticlesState
import com.alex.newstime.repository.article.Article
import com.alex.newstime.repository.article.ArticleRepository
import com.alex.newstime.util.plusAssign
import com.hadilq.liveevent.LiveEvent
import timber.log.Timber

class FavoritsViewModel(private val articleRepository: ArticleRepository) : BaseViewModel() {

    private val currentArticles by lazy { ArrayList<Article>() }

    private val _recyclerArticlesState = MutableLiveData<ArticlesState>()
    val recyclerArticlesState: LiveData<ArticlesState> = _recyclerArticlesState

    private val _detailState = LiveEvent<Article>()
    val detailState: LiveData<Article> = _detailState

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