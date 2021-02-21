package com.mkspace.newscalendar.data.repository.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.mkspace.newscalendar.data.NewsDatabase
import com.mkspace.newscalendar.data.db.ArticleRemoteKeyDao
import com.mkspace.newscalendar.data.remote.ArticleRemoteDataSource
import com.mkspace.newscalendar.data.repository.ArticleRepository
import com.mkspace.newscalendar.data.vo.Article
import com.mkspace.newscalendar.data.vo.remotekey.ArticleRemoteKey
import com.mkspace.newscalendar.network.HttpException
import com.mkspace.newscalendar.network.asHttpException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import java.io.IOException

@ExperimentalPagingApi
class ArticleRemoteMediator(
    private val database: NewsDatabase,
    private val repository: ArticleRepository,
    private val articleRemoteKeyDao: ArticleRemoteKeyDao,
    private val articleRemote: ArticleRemoteDataSource,
    private val queryDate: String
) : RemoteMediator<Int, Article>() {

    private val isRemoteEmptyChannel = Channel<Boolean>(Channel.CONFLATED)
    val isRemoteEmpty = isRemoteEmptyChannel.receiveAsFlow()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Article>
    ): MediatorResult {
        try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    // Query DB for ForumListRemoteKey for the forum post list.
                    // ForumListRemoteKey is a wrapper object we use to keep track of page keys we
                    // receive from the Forum List API to fetch the next page.
                    val afterKey = database.withTransaction {
                        articleRemoteKeyDao.get(queryDate)
                    }

                    // We must explicitly check if the page key is null when appending, since the
                    // Forum List API informs the end of the list by returning null for page key,
                    // but passing a null key to Forum List API will fetch the initial page.
                    afterKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = articleRemote.searchArticlesByQueryDate(
                queryDate = queryDate,
                limit = when (loadType) {
                    LoadType.REFRESH -> state.config.initialLoadSize
                    else -> state.config.pageSize
                },
                after = loadKey
            )

            if (response.error != null) return MediatorResult.Error(response.error.asHttpException)

            val articles = response.items
            val after = if (response.start != null && response.display != null) {
                response.start + response.display
            } else {
                null
            }

            if (loadType == LoadType.REFRESH) {
                isRemoteEmptyChannel.send(articles.isNullOrEmpty())
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    repository.clear(queryDate)
                    articleRemoteKeyDao.delete(queryDate)
                }

                articleRemoteKeyDao.insertOrUpdate(ArticleRemoteKey(queryDate, after))
                if (!articles.isNullOrEmpty()) {
                    repository.insertOrUpdate(queryDate, articles)
                }
            }

            return MediatorResult.Success(endOfPaginationReached = after == null)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}