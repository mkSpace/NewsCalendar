package com.mkspace.newscalendar.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.mkspace.newscalendar.data.vo.Article
import com.mkspace.newscalendar.data.vo.ArticleQueryDateRelation
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ArticleQueryDateRelationDao : BaseDao<ArticleQueryDateRelation>() {

    @Query("SELECT * FROM articles INNER JOIN article_query_relations ON article_original_link = relation_article_original_link WHERE relation_query_date = :queryDate ORDER BY article_created_at")
    abstract fun getArticlePagingSource(queryDate: String): PagingSource<Int, Article>

    @Query("SELECT COUNT(*) FROM article_query_relations WHERE relation_query_date = :queryDate ")
    abstract fun countArticlesByQueryDate(queryDate: String): Flow<Int>

    @Query("DELETE FROM article_query_relations WHERE relation_query_date = :queryDate")
    abstract fun delete(queryDate: String)
}