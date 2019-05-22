package com.alex.newstime.repository.article

import com.alex.newstime.repository.api.ApiClient
import com.alex.newstime.repository.database.article.ArticleTable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.random.Random

open class ArticleRepository {

    open fun getTopHeadlines(pageSize: Int = 10, page: Int = 1): Single<List<Article>> {
        return ApiClient
            .getInterface()
            .getTopHeadlines(pageSize, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.articles }
            .map { articles ->
                articles.onEach { article ->
                    article.id = Random.nextLong(10000)
                }
            }
            .doOnSuccess {
                ArticleTable.insert(it).subscribe()
            }
//            .onErrorResumeNext {
//                ArticleTable.getAll().map {
//                    if (it.isEmpty()) {
//                        val article = Article()
//                        article.id = 87242323
//                        article.title = "Test Article"
//                        article.urlToImage = "http://icanbecreative.com/resources/files/articles/deadpool-movie-photoshop-tutorial/deadpool-movie-logo-photoshop-tutorial.jpg"
//
//                        listOf(article, article, article)
//                    } else {
//                        it
//                    }
//                }
//            }
    }

    open fun getEverything(pageSize: Int = 10, page: Int = 1): Single<List<Article>> {
        return ApiClient
            .getInterface()
            .getEverything(pageSize, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.articles }
            .map { articles ->
                articles.onEach { article ->
                    article.id = Random.nextLong(10000)
                }
            }
            .doOnSuccess {
                ArticleTable.insert(it).subscribe()
            }
//            .onErrorResumeNext {
//                ArticleTable.getAll().map {
//                    if (it.isEmpty()) {
//                        val article = Article()
//                        article.id = 87242323
//                        article.title = "Test Article"
//                        article.urlToImage = "http://icanbecreative.com/resources/files/articles/deadpool-movie-photoshop-tutorial/deadpool-movie-logo-photoshop-tutorial.jpg"
//
//                        listOf(article, article, article)
//                    } else {
//                        it
//                    }
//                }
//            }
    }
}