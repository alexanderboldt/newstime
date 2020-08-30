package com.alex.newstime.feature.topheadlines.di

import com.alex.newstime.feature.topheadlines.TopHeadlinesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val topHeadlinesModule = module {
    viewModel { TopHeadlinesViewModel(get(), get()) }
}