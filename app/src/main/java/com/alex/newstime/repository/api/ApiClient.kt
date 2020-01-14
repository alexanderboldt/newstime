package com.alex.newstime.repository.api

import com.alex.newstime.BuildConfig
import com.ihsanbal.logging.Level
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import okhttp3.internal.platform.Platform
import com.ihsanbal.logging.LoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private lateinit var api: IApi

    private const val TIMEOUT: Long = 30

    // ----------------------------------------------------------------------------

    fun initialize() {
        api = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()

                val url = request
                        .url()
                        .newBuilder()
                        .addQueryParameter("apikey", BuildConfig.API_KEY)
                        .build()

                val requestBuilder = request.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .url(url)
                        .build()

                chain.proceed(requestBuilder)
            }
            .addInterceptor(LoggingInterceptor.Builder()
                    .loggable(BuildConfig.DEBUG)
                    .setLevel(Level.BODY)
                    .log(Platform.INFO)
                    .build())
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()
            .run {
                Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(this)
                    .build()
                    .create(IApi::class.java)
            }
    }

    fun getInterface() = api
}