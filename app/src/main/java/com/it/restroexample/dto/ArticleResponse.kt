package com.it.restroexample.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

/**
 * created by Akash on 24/12/2020
 */
@JsonClass(generateAdapter = true)
class ArticleResponse {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("score")
    @Expose
    var score: Int? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("url")
    @Expose
    var url: String? = null
}