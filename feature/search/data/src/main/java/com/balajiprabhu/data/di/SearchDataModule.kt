package com.balajiprabhu.data.di

import com.balajiprabhu.data.remote.SearchService
import com.balajiprabhu.data.repository.SearchRepositoryImpl
import com.balajiprabhu.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val BASE_URL = "https://www.themealdb.com"

@Module
@InstallIn(SingletonComponent::class)
object SearchDataModule {

    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideSearchService(retrofit: Retrofit) : SearchService {
        return retrofit.create(SearchService::class.java)
    }

    @Provides
    fun provideSearchRepository(searchService: SearchService) : SearchRepository = SearchRepositoryImpl(searchService)

}