package com.alex.newstime.repository.news

import com.alex.newstime.repository.api.ApiClient
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewsRepository {

    fun getTopHeadlines(): Single<TopHeadlinesResponse> =
        ApiClient
                .getInterface()
                .getTopHeadlines()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
}