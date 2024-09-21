package com.balajiprabhu.receipes.di

import android.content.Context
import com.balajiprabhu.data.local.RecipeDao
import com.balajiprabhu.receipes.navigation.DefaultNavigator
import com.balajiprabhu.receipes.local.ApplicationDataBase
import com.balajiprabhu.search.navigation.SearchFeature
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    fun provideApplicationDataBase(
        @ApplicationContext context: Context
    ): ApplicationDataBase {
        return ApplicationDataBase.getInstance(context)
    }

    @Provides
    fun provideRecipeDao(applicationDataBase: ApplicationDataBase): RecipeDao {
        return applicationDataBase.getRecipeDao()
    }
}