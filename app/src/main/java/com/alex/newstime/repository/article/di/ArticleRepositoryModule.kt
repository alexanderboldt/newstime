package com.alex.newstime.repository.article.di

import com.alex.newstime.repository.article.ArticleRepository
import dagger.Module
import dagger.Provides

@Module
class ArticleRepositoryModule {

    @Provides
    fun provideArticleRepository() = ArticleRepository()
}