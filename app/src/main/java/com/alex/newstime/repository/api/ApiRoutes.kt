package com.alex.newstime.repository.api

import com.alex.newstime.repository.api.models.ApiModelResponse
import retrofit2.http.*

interface ApiRoutes {

    @GET("v2/top-headlines?country=de")
    suspend fun getTopHeadlines(@Query("pageSize") pageSize: Int, @Query("page") page: Int): ApiModelResponse

    @GET("v2/everything?domains=nytimes.com")
    suspend fun getEverything(@Query("pageSize") pageSize: Int, @Query("page") page: Int): ApiModelResponse
}