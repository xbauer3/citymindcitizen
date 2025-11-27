package com.example.projectobcane.di.notifications

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManagerCompat {
        return NotificationManagerCompat.from(context)
    }

    @Provides
    @Singleton
    fun provideNotificationBuilderFactory(
        @ApplicationContext context: Context
    ): NotificationBuilderFactory {
        return NotificationBuilderFactory(context)
    }
}