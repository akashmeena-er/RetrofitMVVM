package com.it.restroexample.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "user")
data class ArticleResponse(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    val score: Int,
    var title: String,
    val url: String
)