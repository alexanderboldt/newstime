package com.alex.newstime.repository.article.di

import com.alex.newstime.repository.article.ArticleRepository
import org.koin.dsl.module

val articleRepositoryModule = module {
    factory { ArticleRepository() }
}