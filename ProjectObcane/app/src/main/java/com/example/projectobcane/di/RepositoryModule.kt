package com.example.projectobcane.di


import com.example.projectobcane.database.events.EventDao
import com.example.projectobcane.database.events.EventLocalRepositoryImpl
import com.example.projectobcane.database.events.IEventLocalRepository
import com.example.projectobcane.database.reports.IReportLocalRepository
import com.example.projectobcane.database.reports.ReportDao
import com.example.projectobcane.database.reports.ReportRepository
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
    fun provideRepository(dao: ReportDao): IReportLocalRepository {
        return ReportRepository(dao)
    }

    @Provides
    @Singleton
    fun provideEventRepository(dao: EventDao): IEventLocalRepository {
        return EventLocalRepositoryImpl(dao)
    }


}