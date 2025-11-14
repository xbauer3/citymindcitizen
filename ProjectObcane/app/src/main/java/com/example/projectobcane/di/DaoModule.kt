package com.example.projectobcane.di

import com.example.projectobcane.database.ProjectDatabase
import com.example.projectobcane.database.reports.ReportDao
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
    fun provideDao(database: ProjectDatabase): ReportDao {
        return database.reportDao()
    }

}