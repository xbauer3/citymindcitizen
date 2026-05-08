package com.example.projectobcane.screens.news

import com.example.projectobcane.models.NewsItemUi

data class NewsScreenUIState(
    val loading: Boolean = true,
    val news: List<NewsItemUi> = emptyList(),
    val error: String? = null
)