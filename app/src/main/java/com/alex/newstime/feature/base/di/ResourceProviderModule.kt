package com.alex.newstime.feature.base.di

import com.alex.newstime.feature.base.ResourceProvider
import dagger.Module
import dagger.Provides

@Module
class ResourceProviderModule {

    @Provides
    fun provideResourceProvider() = ResourceProvider
}