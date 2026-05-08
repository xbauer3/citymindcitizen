package com.example.projectobcane.communication.community

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateCommentRequest(
    val authorName: String,
    val content: String,
    val createdAt: String,
    val postId: Long
)