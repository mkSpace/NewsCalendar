package com.mkspace.newscalendar.di

import android.content.Context
import com.mkspace.newscalendar.data.NewsDatabase
import com.mkspace.newscalendar.data.db.ArticleDao
import com.mkspace.newscalendar.data.db.ArticleQueryDateRelationDao
import com.mkspace.newscalendar.data.db.ArticleRemoteKeyDao
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

    @Provides
    fun getArticleQueryDateRelationDao(
        @ApplicationContext context: Context
    ): ArticleQueryDateRelationDao = NewsDatabase.getInstance(context).articleQueryDateRelationDao()

    @Provides
    fun getArticleRemoteKeyDao(@ApplicationContext context: Context): ArticleRemoteKeyDao =
        NewsDatabase.getInstance(context).articleRemoteKeyDao()
}