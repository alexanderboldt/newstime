package com.alex.newstime.feature.topheadlines.di

import com.alex.newstime.feature.base.di.ResourceProviderModule
import com.alex.newstime.feature.topheadlines.TopHeadlinesViewModel
import com.alex.newstime.repository.article.di.ArticleRepositoryModule
import dagger.Component

@Component(modules = [ArticleRepositoryModule::class, ResourceProviderModule::class])
interface TopHeadlinesViewModelComponent {
    fun inject(viewModel: TopHeadlinesViewModel)
}