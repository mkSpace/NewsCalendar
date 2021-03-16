package com.mkspace.newscalendar.data

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

data class PagingDataContainer<T : Any>(
    val pagingData: Flow<PagingData<T>>,
    val isRemoteEmpty: Flow<Boolean>
)