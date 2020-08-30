package com.alex.newstime.repository.api

import com.alex.newstime.repository.api.article.ArticleResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface IApi {

    @GET("v2/top-headlines?country=de")
    fun getTopHeadlines(@Query("pageSize") pageSize: Int, @Query("page") page: Int): Single<ArticleResponse>

    @GET("v2/everything?domains=nytimes.com")
    fun getEverything(@Query("pageSize") pageSize: Int, @Query("page") page: Int): Single<ArticleResponse>
}