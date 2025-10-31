package com.example.projectobcane.di

import com.example.projectobcane.database.EventDao
import com.example.projectobcane.database.EventLocalRepositoryImpl
import com.example.projectobcane.database.IEventLocalRepository
import com.example.projectobcane.database.color.ColorCategoryDao
import com.example.projectobcane.database.color.ColorCategoryRepositoryImpl
import com.example.projectobcane.database.color.IColorCategoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(dao: EventDao): IEventLocalRepository {
        return EventLocalRepositoryImpl(dao)
    }


    @Provides
    @Singleton
    fun provideColorCategoryRepository(dao: ColorCategoryDao): IColorCategoryRepository {
        return ColorCategoryRepositoryImpl(dao)
    }

}