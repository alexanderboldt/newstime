package com.alex.newstime.repository.api.article

import com.alex.newstime.repository.api.ApiClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class ArticleRoutes {

    fun getTopHeadlines(pageSize: Int = 10, page: Int = 1): Single<List<ApiModelArticle>> {
        return ApiClient
            .getInterface()
            .getTopHeadlines(pageSize, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.articles }
    }

    fun getEverything(pageSize: Int = 10, page: Int = 1): Single<List<ApiModelArticle>> {
        return ApiClient
            .getInterface()
            .getEverything(pageSize, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.articles }
    }
}