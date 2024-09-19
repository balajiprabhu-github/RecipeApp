package com.balajiprabhu.search.di

import com.balajiprabhu.search.navigation.SearchFeature
import com.balajiprabhu.search.navigation.SearchFeatureImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SearchModule {
    @Provides
    fun provideSearchFeature(): SearchFeature {
        return SearchFeatureImpl()
    }
}