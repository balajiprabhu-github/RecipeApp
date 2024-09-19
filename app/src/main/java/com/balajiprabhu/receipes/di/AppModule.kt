package com.balajiprabhu.receipes.di

import com.balajiprabhu.receipes.navigation.DefaultNavigator
import com.balajiprabhu.search.navigation.SearchFeature
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideNavigationSubGraph(searchFeature: SearchFeature): DefaultNavigator {
        return DefaultNavigator(
            searchFeature = searchFeature
        )
    }
}