package com.example.projectobcane.di.api

import com.example.projectobcane.communication.news.INewsRemoteRepository
import com.example.projectobcane.communication.news.NewsApi
import com.example.projectobcane.communication.news.NewsRemoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsModule {

    @Provides
    @Singleton
    fun provideNewsApi(@NewsRetrofit retrofit: Retrofit): NewsApi {
        return retrofit.create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(api: NewsApi): INewsRemoteRepository {
        return NewsRemoteRepositoryImpl(api)
    }
}