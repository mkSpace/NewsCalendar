package com.mkspace.newscalendar.network.services

import com.mkspace.newscalendar.data.vo.Article
import com.mkspace.newscalendar.network.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    companion object {
        private const val DEFAULT_SORT = "sim"
    }

    @GET("search/news.json?")
    suspend fun searchNews(
        @Query("query") query: String?,
        @Query("start") after: Int?,
        @Query("display") limit: Int?,
        @Query("sort") sort: String = DEFAULT_SORT
    ): Response<List<Article>>
}