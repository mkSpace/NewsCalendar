package com.mkspace.newscalendar.di

import com.mkspace.newscalendar.network.NewsRetrofit
import com.mkspace.newscalendar.network.services.NewsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    fun provideNewsService(client: OkHttpClient): NewsService = NewsRetrofit.create(client)
}