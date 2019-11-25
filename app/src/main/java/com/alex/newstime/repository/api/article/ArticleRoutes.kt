package com.alex.newstime.repository.api.article

import com.alex.newstime.repository.api.ApiClient
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ArticleRoutes {

    fun getTopHeadlines(pageSize: Int = 10, page: Int = 1): Single<List<ApiArticle>> {
        return ApiClient
            .getInterface()
            .getTopHeadlines(pageSize, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.articles }
    }

    fun getEverything(pageSize: Int = 10, page: Int = 1): Single<List<ApiArticle>> {
        return ApiClient
            .getInterface()
            .getEverything(pageSize, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.articles }
    }
}