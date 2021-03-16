package com.mkspace.newscalendar.ui.main

import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.funin.base.funinbase.base.BaseViewModel
import com.funin.base.funinbase.rx.schedulers.BaseSchedulerProvider
import com.mkspace.newscalendar.data.PagingDataContainer
import com.mkspace.newscalendar.data.repository.ArticleRepository
import com.mkspace.newscalendar.data.vo.Article
import com.mkspace.newscalendar.utils.DateTimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ArticleViewModel @Inject constructor(
    schedulerProvider: BaseSchedulerProvider,
    private val articleRepository: ArticleRepository
) : BaseViewModel(schedulerProvider) {

    companion object {
        private const val PAGE_SIZE = 60
        private const val INITIAL_LOAD_SIZE = 60
    }

    @ExperimentalCoroutinesApi
    private val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    init {
        offerInitialQueryDate()
    }

    private val pagingDataContainerChannel =
        Channel<PagingDataContainer<Article>>(Channel.CONFLATED)

    @FlowPreview
    @ExperimentalPagingApi
    @ExperimentalCoroutinesApi
    val items: Flow<PagingData<Article>> = queryChannel.asFlow()
        .flatMapLatest { getArticlesByQuery(it) }
        .cachedIn(viewModelScope)
        .flowOn(Dispatchers.IO)

    @ExperimentalPagingApi
    private fun getArticlesByQuery(query: String): Flow<PagingData<Article>> {
        val pagingDataContainer = articleRepository.getArticles(
            pageSize = PAGE_SIZE,
            initialLoadSize = INITIAL_LOAD_SIZE,
            queryDate = query
        )
        pagingDataContainerChannel.offer(pagingDataContainer)
        return pagingDataContainer.pagingData.cachedIn(viewModelScope)
    }

    private fun offerInitialQueryDate() {
        queryChannel.offer(DateTimeUtils.getTomorrow())
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    val isLocalEmpty: Flow<Boolean> = queryChannel.asFlow()
        .flatMapLatest { articleRepository.countArticlesByQueryDate(it) }
        .mapLatest { count -> count <= 0 }
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)

    @ExperimentalCoroutinesApi
    val isRemoteEmpty: Flow<Boolean> = pagingDataContainerChannel.receiveAsFlow()
        .flatMapLatest { it.isRemoteEmpty }
        .distinctUntilChanged()

    fun setQuery(query: String) {
        queryChannel.offer(query)
    }
}