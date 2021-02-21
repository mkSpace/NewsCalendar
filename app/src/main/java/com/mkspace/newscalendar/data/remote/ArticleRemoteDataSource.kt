package com.mkspace.newscalendar.data.remote

import com.mkspace.newscalendar.data.vo.Article
import com.mkspace.newscalendar.network.Response
import com.mkspace.newscalendar.network.services.NewsService
import javax.inject.Inject

class ArticleRemoteDataSource @Inject constructor(private val service: NewsService) {

    suspend fun searchArticlesByQueryDate(
        queryDate: String,
        after: Int?,
        limit: Int?
    ): Response<List<Article>> = service.searchNews(queryDate, after, limit)
}