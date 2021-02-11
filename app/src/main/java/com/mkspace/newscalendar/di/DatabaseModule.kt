package com.mkspace.newscalendar.di

import android.content.Context
import com.mkspace.newscalendar.data.ArticleDao
import com.mkspace.newscalendar.data.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun getDatabase(@ApplicationContext context: Context): NewsDatabase =
        NewsDatabase.getInstance(context)

    @Provides
    fun getArticleDao(@ApplicationContext context: Context): ArticleDao =
        NewsDatabase.getInstance(context).articleDao()
}