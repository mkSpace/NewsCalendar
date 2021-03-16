package com.mkspace.newscalendar.network

data class Response<T>(
    val items: T? = null,
    val start: Int? = null,
    val display: Int? = null,
    val total: Int? = null,
    val error: Error? = null
)

data class Error(
    val errorMessage: String,
    val errorCode: String
)