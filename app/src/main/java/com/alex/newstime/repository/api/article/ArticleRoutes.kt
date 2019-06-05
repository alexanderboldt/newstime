package com.alex.newstime.repository.api.article

import com.alex.newstime.repository.api.ApiClient
import com.alex.newstime.repository.article.Article
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.random.Random

object ArticleRoutes {

    fun getTopHeadlines(pageSize: Int = 10, page: Int = 1): Single<List<Article>> {
        return ApiClient
            .getInterface()
            .getTopHeadlines(pageSize, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.articles }
            .map { articles ->
                articles.onEach { article ->
                    article.id = article.title.hashCode().toLong()
                }
            }
    }

    fun getEverything(pageSize: Int = 10, page: Int = 1): Single<List<Article>> {
        return ApiClient
            .getInterface()
            .getEverything(pageSize, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.articles }
            .map { articles ->
                articles.onEach { article ->
                    article.id = article.title.hashCode().toLong()
                }
            }
    }
}