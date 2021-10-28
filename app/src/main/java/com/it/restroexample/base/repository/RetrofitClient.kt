package com.it.restroexample.base.repository

import com.it.restroexample.base.repository.adapters.ApiResponseAdapterFactory
import com.it.restroexample.util.BaseInterface
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

/**
 * A Singleton class for creating RetroClint obj created by Akash 28/12/2020
 */
object RetrofitClient {

    fun createApiService(
        moshi: Moshi = Moshi.Builder().build()
    ): AkashApi {
        lazy {
            OkHttpClient.Builder().apply {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(loggingInterceptor)
            }.build()
        }

        return Retrofit.Builder()
            .baseUrl(BaseInterface.WS_BASE)
            .addCallAdapterFactory(ApiResponseAdapterFactory())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create()

    }

}

