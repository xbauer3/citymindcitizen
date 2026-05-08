package com.example.projectobcane.communication.community

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommunityBoardPostDto(
    @Json(name = "id") val id: Long,
    @Json(name = "authorName") val authorName: String?,
    @Json(name = "description") val description: String,
    @Json(name = "entityId") val entityId: Long,
    @Json(name = "place") val place: String?,
    @Json(name = "postType") val postType: String,
    @Json(name = "status") val status: String,
    @Json(name = "title") val title: String,
    @Json(name = "upvoteCount") val upvoteCount: Int,
    @Json(name = "commentCount") val commentCount: Int,
    @Json(name = "dateAdded") val dateAdded: String,
    @Json(name = "hashedEmail") val hashedEmail: String?
)