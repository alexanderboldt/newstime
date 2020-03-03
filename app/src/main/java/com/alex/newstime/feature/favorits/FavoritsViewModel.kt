package com.alex.newstime.feature.favorits

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.core.feature.SingleLiveEvent
import com.alex.newstime.feature.favorits.di.DaggerFavoritsViewModelComponent
import com.alex.newstime.feature.favorits.model.ArticleState
import com.alex.newstime.feature.favorits.model.ArticlesState
import com.alex.newstime.repository.article.Article
import com.alex.newstime.repository.article.ArticleRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class FavoritsViewModel : ViewModel() {

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

    fun loadArticles() {
        viewModelScope.launch {
            articleRepository
                .getFavorites()
                .doOnSubscribe {
                    currentArticles.clear()
                }
                .subscribe({ articles ->
                    currentArticles.addAll(articles)

                    _recyclerArticlesState.postValue(articles.map { article ->
                        ArticleState(article.id!!, article.title!!, article.urlToImage!!)
                    } as ArrayList)
                }, {
                    Timber.w(it)
                })
        }
    }

    fun clickOnArticle(article: ArticleState) {
        _detailState.postValue(currentArticles.first {
            it.id == article.id
        })
    }
}