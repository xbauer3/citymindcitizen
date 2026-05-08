package com.example.projectobcane.di.api

import com.example.projectobcane.communication.community.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton





@Module
@InstallIn(SingletonComponent::class)
object CommunityBoardModule {

    @Provides
    @Singleton
    fun provideCommunityBoardApi(@ReportsRetrofit retrofit: Retrofit): CommunityBoardApi {
        return retrofit.create(CommunityBoardApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCommunityBoardRepository(api: CommunityBoardApi): ICommunityBoardRemoteRepository {
        return CommunityBoardRemoteRepositoryImpl(api)
    }
}