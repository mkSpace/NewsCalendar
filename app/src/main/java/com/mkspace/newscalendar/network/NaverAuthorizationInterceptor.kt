package com.mkspace.newscalendar.network

import com.mkspace.newscalendar.data.Constants.NAVER_CLIENT_ID
import com.mkspace.newscalendar.data.Constants.NAVER_CLIENT_SECRET
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NaverAuthorizationInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(
        chain.request()
            .newBuilder()
            .apply {
                header("X-Naver-Client-Id", NAVER_CLIENT_ID)
                header("X-Naver-Client-Secret", NAVER_CLIENT_SECRET)
            }
            .build()
    )
}