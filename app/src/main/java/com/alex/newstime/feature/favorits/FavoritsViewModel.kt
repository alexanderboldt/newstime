package com.alex.newstime.feature.favorits

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.newstime.feature.favorits.di.DaggerFavoritsViewModelComponent
import com.alex.newstime.repository.article.Article
import com.alex.newstime.repository.article.ArticleRepository
import com.alex.newstime.util.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class FavoritsViewModel : ViewModel() {

    @Inject
    lateinit var articleRepository: ArticleRepository

    private val currentArticles = ArrayList<Article>()

    var recyclerArticlesState = MutableLiveData<List<ArticleModel>>()
    var detailState = SingleLiveEvent<Article>()

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

                    recyclerArticlesState.postValue(articles.map { article ->
                        ArticleModel(article.id!!, article.title!!, article.urlToImage!!)
                    } as ArrayList)
                }, {
                    Timber.e(it)
                })
        }
    }

    fun clickOnArticle(article: ArticleModel) {
        detailState.postValue(currentArticles.first {
            it.id == article.id
        })
    }
}