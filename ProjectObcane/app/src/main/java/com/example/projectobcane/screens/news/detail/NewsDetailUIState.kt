package com.example.projectobcane.screens.news.detail

import com.example.projectobcane.models.NewsItemUi

data class NewsDetailUIState(
    val loading: Boolean = true,
    val news: NewsItemUi? = null
)