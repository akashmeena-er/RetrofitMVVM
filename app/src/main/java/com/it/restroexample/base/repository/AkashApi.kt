package com.it.restroexample.base.repository
import com.it.restroexample.dto.ArticleResponse
import com.it.restroexample.util.BaseInterface.Companion.WS_TOP
import retrofit2.Call
import retrofit2.http.*
/**
 * created by Akash on 24/12/2020
 */
interface AkashApi {

    @GET(WS_TOP)
    suspend fun topNews(): ApiResponse<List<Int>>

    @GET("/v0/item/{articleid}.json?print=pretty")
    suspend fun detail(@Path("articleid") id: Int): ApiResponse<ArticleResponse>?

}


