package com.example.projectobcane.communication.community

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreatePostRequest(
    val authorEmail: String,
    val authorName: String,
    val description: String,
    val entityId: Long,
    val place: String,
    val postType: String,
    val title: String
)