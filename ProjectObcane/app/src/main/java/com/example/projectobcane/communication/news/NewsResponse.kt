package com.example.projectobcane.communication.news

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsResponse(
    @Json(name = "items") val items: List<NewsItemDto>
)