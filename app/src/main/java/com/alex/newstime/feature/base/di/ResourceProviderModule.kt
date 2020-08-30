package com.alex.newstime.feature.base.di

import com.alex.newstime.feature.base.ResourceProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val resourceProviderModule = module {
    single { ResourceProvider(androidContext()) }
}