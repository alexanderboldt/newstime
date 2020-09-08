package com.alex.newstime.feature.favorits.di

import com.alex.newstime.feature.favorits.FavoritsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoritsModule = module {
    viewModel { FavoritsViewModel(get()) }
}