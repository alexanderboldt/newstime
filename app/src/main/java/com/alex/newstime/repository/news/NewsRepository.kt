package com.alex.newstime.repository.news

import com.alex.newstime.repository.api.ApiClient
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewsRepository {

    fun getTopHeadlines(): Single<List<Article>> {
        return ApiClient
            .getInterface()
            .getTopHeadlines()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.articles }
            .onErrorReturn {
                val article = Article()
                article.title = "Test Article"
                article.urlToImage = "http://icanbecreative.com/resources/files/articles/deadpool-movie-photoshop-tutorial/deadpool-movie-logo-photoshop-tutorial.jpg"

                listOf(article, article, article)
            }
    }
}