package com.funin.base.base

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.zip

@ExperimentalCoroutinesApi
fun <T, U> Flow<T>.zipWith(flow: Flow<U>): Flow<Pair<T, U>> = zip(flow) { t, u -> t to u }

@ExperimentalCoroutinesApi
fun <T, U> Flow<T>.combine(flow: Flow<U>): Flow<Pair<T, U>> = combine(flow) { t, u -> t to u }

@ExperimentalCoroutinesApi
fun <T, U, V> Flow<T>.combine(flow1: Flow<U>, flow2: Flow<V>): Flow<Triple<T, U, V>> =
    combine(flow1)
        .combine(flow2) { pair, v ->
            val (t, u) = pair
            Triple(t, u, v)
        }
