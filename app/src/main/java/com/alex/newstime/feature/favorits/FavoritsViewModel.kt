package com.alex.newstime.feature.favorits

import androidx.lifecycle.MutableLiveData
import com.alex.newstime.feature.base.BaseViewModel
import com.alex.newstime.repository.article.Article
import com.alex.newstime.repository.article.ArticleRepository
import com.alex.newstime.util.SingleLiveEvent
import com.alex.newstime.util.plusAssign

class FavoritsViewModel : BaseViewModel() {

    private lateinit var articleRepository: ArticleRepository

    private val articles = ArrayList<Article>()

    var recyclerArticlesState = MutableLiveData<List<ArticleModel>>()
    var detailState = SingleLiveEvent<Article>()

    // ----------------------------------------------------------------------------

    fun setArticleRepository(articleRepository: ArticleRepository) {
        this.articleRepository = articleRepository
    }

    fun loadArticles() {
        disposables += articleRepository
            .getFavorites()
            .doOnSubscribe {
                articles.clear()
            }
            .subscribe({ articles ->
                this.articles.addAll(articles)

                recyclerArticlesState.postValue(articles.map { article ->
                    ArticleModel(article.id!!, article.title!!, article.urlToImage!!)
                } as ArrayList)
            }, {
                println(it)
            })
    }

    fun clickOnArticle(article: ArticleModel) {
        detailState.postValue(articles.first {
            it.id == article.id
        })
    }
}