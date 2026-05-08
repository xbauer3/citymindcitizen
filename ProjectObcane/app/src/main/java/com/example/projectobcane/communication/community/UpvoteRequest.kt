package com.example.projectobcane.communication.community

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpvoteRequest(
    val postId: Long,
    val upvoterEmailHash: String
)