package com.mkspace.newscalendar.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.withTransaction
import com.mkspace.newscalendar.data.NewsDatabase
import com.mkspace.newscalendar.data.PagingDataContainer
import com.mkspace.newscalendar.data.db.ArticleDao
import com.mkspace.newscalendar.data.db.ArticleQueryDateRelationDao
import com.mkspace.newscalendar.data.db.ArticleRemoteKeyDao
import com.mkspace.newscalendar.data.remote.ArticleRemoteDataSource
import com.mkspace.newscalendar.data.repository.mediator.ArticleRemoteMediator
import com.mkspace.newscalendar.data.vo.Article
import com.mkspace.newscalendar.data.vo.ArticleQueryDateRelation
import com.mkspace.newscalendar.utils.DateTimeUtils
import kotlinx.coroutines.flow.Flow
import java.util.regex.Pattern
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val database: NewsDatabase,
    private val articleRemoteDataSource: ArticleRemoteDataSource,
    private val articleDao: ArticleDao,
    private val articleRemoteKeyDao: ArticleRemoteKeyDao,
    private val articleQueryDateRelationDao: ArticleQueryDateRelationDao
) {

    companion object {
        // For Test
        private val IGNORE_TEXTS = arrayOf("운세", "날씨", "여행준비")
        private const val YEAR = "년"
        private const val IGNORE_PATTERN = "[0-9]+$YEAR"
    }

    @ExperimentalPagingApi
    fun getArticles(
        pageSize: Int,
        initialLoadSize: Int,
        queryDate: String
    ): PagingDataContainer<Article> {
        val mediator = ArticleRemoteMediator(
            database = database,
            repository = this,
            articleRemoteKeyDao = articleRemoteKeyDao,
            articleRemote = articleRemoteDataSource,
            queryDate = queryDate
        )

        val pagingData = Pager(
            config = PagingConfig(pageSize = pageSize, initialLoadSize = initialLoadSize),
            remoteMediator = mediator,
            pagingSourceFactory = {
                articleQueryDateRelationDao.getArticlePagingSource(queryDate)
            }
        ).flow
        return PagingDataContainer(pagingData, mediator.isRemoteEmpty)
    }

    fun countArticlesByQueryDate(queryDate: String): Flow<Int> =
        articleQueryDateRelationDao.countArticlesByQueryDate(queryDate)

    fun clear(queryDate: String) {
        // Article이 ArticleQueryDateRelation에서 Foreign key로 등록 되어있으며, onDelete가
        // CASCASE이기 때문에 Relation을 삭제하면 같이 삭제됨
        articleQueryDateRelationDao.delete(queryDate)
    }

    suspend fun insertOrUpdate(queryDate: String, articles: List<Article>) {
        val newArticles = articles
            .filterNot {
                val matcher = Pattern.compile(IGNORE_PATTERN).matcher(it.title)
                if (matcher.find()) {
                    val matchedYear = matcher.group()
                    if (matchedYear.contains("년") && matchedYear.length > 3) {
                        val articleYear = (matchedYear.substring(0, matchedYear.length - 1)
                            .toIntOrNull() ?: 0)
                        articleYear < DateTimeUtils.getCurrentYear()
                    } else {
                        false
                    }
                } else {
                    false
                }
            }
            .filterNot { article ->
                IGNORE_TEXTS.map { article.title.contains(it) }.contains(true)
            }
            .map { it.copy(createdAt = System.currentTimeMillis()) }
        database.withTransaction {
            articleDao.insertOrUpdate(newArticles)
            articleQueryDateRelationDao.insertOrUpdate(newArticles.toArticleQueryRelations(queryDate))
        }
    }

    private fun List<Article>.toArticleQueryRelations(queryDate: String): List<ArticleQueryDateRelation> =
        map { ArticleQueryDateRelation(it.originalLink, queryDate) }
}