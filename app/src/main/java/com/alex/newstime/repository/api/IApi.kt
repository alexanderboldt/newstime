package com.alex.newstime.repository.api

import com.alex.newstime.repository.news.TopHeadlinesResponse
import io.reactivex.Single
import retrofit2.http.*

interface IApi {

    @GET("v2/top-headlines?country=de&category=business")
    fun getTopHeadlines(): Single<TopHeadlinesResponse>
}