package com.example.projectobcane.di

import com.example.projectobcane.database.EventDao
import com.example.projectobcane.database.EventDatabase
import com.example.projectobcane.database.color.ColorCategoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideDao(database: EventDatabase): EventDao {
        return database.eventDao()
    }

    @Provides
    @Singleton
    fun provideColorCategoryDao(database: EventDatabase): ColorCategoryDao {
        return database.colorCategoryDao()
    }

}