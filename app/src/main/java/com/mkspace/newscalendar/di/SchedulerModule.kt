package com.mkspace.newscalendar.di

import com.funin.base.funinbase.rx.schedulers.BaseSchedulerProvider
import com.funin.base.funinbase.rx.schedulers.SchedulerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SchedulerModule {

    @Provides
    fun provideSchedulerProvider(): BaseSchedulerProvider = SchedulerProvider
}