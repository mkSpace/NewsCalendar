package com.mkspace.newscalendar.network

class HttpException(
    val errorCode: String,
    override val message: String? = null
) : RuntimeException(message) {

    constructor(error: Error) : this(error.errorCode, error.errorMessage)
}

val Error.asHttpException: HttpException
    get() = HttpException(this)