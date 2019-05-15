package com.alex.newstime.repository.api

import com.alex.newstime.repository.news.TopHeadlinesResponse
import io.reactivex.Single
import retrofit2.http.*

interface IApi {

    @GET("v2/top-headlines?country=de")
    fun getTopHeadlines(@Query("pageSize") pageSize: Int, @Query("page") page: Int): Single<TopHeadlinesResponse>

    @GET("v2/everything?domains=nytimes.com")
    fun getEverything(@Query("pageSize") pageSize: Int, @Query("page") page: Int): Single<TopHeadlinesResponse>
}