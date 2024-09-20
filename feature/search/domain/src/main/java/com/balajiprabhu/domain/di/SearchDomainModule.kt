package com.balajiprabhu.domain.di

import com.balajiprabhu.domain.repository.SearchRepository
import com.balajiprabhu.domain.useCases.GetAllRecipesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SearchDomainModule {


    @Provides
    fun provideGetAllRecipesUseCase(searchRepository: SearchRepository) = GetAllRecipesUseCase(searchRepository)


}