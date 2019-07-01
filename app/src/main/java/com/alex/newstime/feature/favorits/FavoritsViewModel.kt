package com.alex.newstime.feature.favorits

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alex.newstime.feature.base.BaseViewModel
import com.alex.newstime.repository.article.Article
import com.alex.newstime.repository.article.ArticleRepository
import com.alex.newstime.util.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber

class FavoritsViewModel : BaseViewModel() {

    private lateinit var articleRepository: ArticleRepository

    private val currentArticles = ArrayList<Article>()

    var recyclerArticlesState = MutableLiveData<List<ArticleModel>>()
    var detailState = SingleLiveEvent<Article>()

    // ----------------------------------------------------------------------------

    fun setArticleRepository(articleRepository: ArticleRepository) {
        this.articleRepository = articleRepository
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