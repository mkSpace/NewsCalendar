package com.mkspace.newscalendar.data.db

import androidx.room.Dao
import androidx.room.Query
import com.mkspace.newscalendar.data.vo.remotekey.ArticleRemoteKey

@Dao
abstract class ArticleRemoteKeyDao : BaseDao<ArticleRemoteKey>() {

    @Query("SELECT `after` FROM article_remote_keys WHERE query_date = :queryDate")
    abstract fun get(queryDate: String): Int?

    @Query("DELETE FROM article_remote_keys WHERE query_date = :queryDate")
    abstract fun delete(queryDate: String)
}