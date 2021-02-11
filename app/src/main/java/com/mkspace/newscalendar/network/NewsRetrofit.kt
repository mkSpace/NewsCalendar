package com.mkspace.newscalendar.network

import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NewsRetrofit {

    const val KAKAO_API_END_POINT = "https://openapi.naver.com/v1/"

    fun <T> create(
        service: Class<T>,
        client: OkHttpClient,
        httpUrl: String = KAKAO_API_END_POINT
    ): T = Retrofit.Builder()
        .baseUrl(httpUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build()
        .create(service)

    inline fun <reified T : Any> create(
        client: OkHttpClient,
        httpUrl: String = KAKAO_API_END_POINT
    ): T {
        require(httpUrl.isNotBlank()) { "Parameter httpUrl cannot be blank." }
        return create(service = T::class.java, httpUrl = httpUrl, client = client)
    }
}