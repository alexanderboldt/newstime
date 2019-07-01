package com.alex.newstime.feature.favorits.di

import com.alex.newstime.feature.favorits.FavoritsViewModel
import com.alex.newstime.repository.article.di.ArticleRepositoryModule
import dagger.Component

@Component(modules = [(ArticleRepositoryModule::class)])
interface FavoritsViewModelComponent {
    fun inject(viewModel: FavoritsViewModel)
}