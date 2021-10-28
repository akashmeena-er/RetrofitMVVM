package com.it.restroexample.base.repository.adapters

import com.it.restroexample.base.repository.*
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonEncodingException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

internal class ApiResponseCall<T>(proxy: Call<T>) : CallDelegate<T, ApiResponse<T>>(proxy) {
    override fun enqueueImpl(callback: Callback<ApiResponse<T>>) =
        proxy.enqueue(object : Callback<T> {

            override fun onResponse(call: Call<T>, response: Response<T>) {
                val apiResponse = create(response)
                callback.onResponse(this@ApiResponseCall, Response.success(apiResponse))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val result = when (t) {
                    is IOException ->
                        ApiNetworkError(if (call.isCanceled) "Canceled" else t.message ?: "unknown error")
                    is SocketTimeoutException ->
                        ApiTimeoutError<T>("Time out")
                    else -> ApiErrorResponse<T>(errorMessage = t.message ?: "unknown error",code = 0)
                }
                callback.onResponse(this@ApiResponseCall, Response.success(result))
            }
        })

    override fun cloneImpl() = ApiResponseCall(proxy.clone())

    private fun <T> create(response: Response<T>): ApiResponse<T> {
        return if (response.isSuccessful) {
            val body = response.body()
            if (body == null || response.code() == 204) {
                ApiEmptyResponse
            } else {
                ApiSuccessResponse(body)
            }
        } else {
            val msg = response.errorBody()?.string()
            val errorMsg = if (msg.isNullOrEmpty()) {
                response.message()
            } else {

                try {
                    val errorObject = JSONObject(msg)
                    errorObject.getString("message")
                } catch (e: JsonEncodingException) {
                    msg
                }
            } ?: "unknown error"
            var code :Int ? = null
            if (response.code() == 409) {
                val json = JSONObject(msg)
                code = json.optInt("code")
            } else {
                code = response.code()
            }
            ApiErrorResponse(errorMessage = errorMsg,code = code!!)

        }
    }
}

@JsonClass(generateAdapter = true)
internal data class ApiError(val type: String = "", val message: String)
