package com.example.projectobcane.communication

data class AiChatResponse(
    val text: String,
    val faq: List<String>
)