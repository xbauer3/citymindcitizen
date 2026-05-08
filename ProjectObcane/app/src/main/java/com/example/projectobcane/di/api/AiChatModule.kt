package com.example.projectobcane.di.api

import com.example.projectobcane.communication.AiChatApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AiChatModule {

    @Provides
    @Singleton
    fun provideAiChatApi(@CityMindRetrofit retrofit: Retrofit): AiChatApi {
        return retrofit.create(AiChatApi::class.java)
    }
}
