package com.balajiprabhu.domain.di

import com.balajiprabhu.domain.repository.SearchRepository
import com.balajiprabhu.domain.useCases.DeleteRecipeUseCase
import com.balajiprabhu.domain.useCases.GetAllRecipesUseCase
import com.balajiprabhu.domain.useCases.GetAllSavedRecipesUseCase
import com.balajiprabhu.domain.useCases.InsertRecipeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SearchDomainModule {


    @Provides
    fun provideGetAllRecipesUseCase(searchRepository: SearchRepository) = GetAllRecipesUseCase(searchRepository)

    @Provides
    fun provideInsertRecipeUseCase(searchRepository: SearchRepository) = InsertRecipeUseCase(searchRepository)

    @Provides
    fun provideDeleteRecipeUseCase(searchRepository: SearchRepository) = DeleteRecipeUseCase(searchRepository)

    @Provides
    fun provideGetAllSavedRecipesUseCase(searchRepository: SearchRepository) = GetAllSavedRecipesUseCase(searchRepository)

}