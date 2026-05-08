package com.example.projectobcane.models

data class NewsItemUi(
    val id: Long,
    val title: String,
    val text: String,
    val imageUrl: String?,
    val created: Long,
    val startDate: String?,
    val endDate: String?,
    val category: String,
    val faculty: String
)