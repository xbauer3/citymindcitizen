package com.example.projectobcane.models

data class CommunityPostUi(
    val id: Long,
    val title: String,
    val description: String,
    val authorName: String?,
    val dateAdded: String,
    val status: String,
    val postType: String,
    val upvoteCount: Int,
    val commentCount: Int,
    val place: String?,
    val hashedEmail: String? = null,
    val imageUrls: List<String> = emptyList()
)