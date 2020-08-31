package com.alex.newstime.feature.article.di

import com.alex.newstime.feature.article.ArticleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val articleModule = module {
    viewModel { ArticleViewModel() }
}