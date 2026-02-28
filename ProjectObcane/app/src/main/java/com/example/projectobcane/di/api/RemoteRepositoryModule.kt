package com.example.projectobcane.di.api

import com.example.projectobcane.communication.AiChatApi
import com.example.projectobcane.communication.AiChatRemoteRepositoryImpl
import com.example.projectobcane.communication.IAiChatRemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteRepositoryModule {

    @Provides
    @Singleton
    fun provideAiChatRemoteRepository(api: AiChatApi): IAiChatRemoteRepository {
        return AiChatRemoteRepositoryImpl(api)
    }
}
