package com.example.projectobcane.screens.chat

data class AiChatUiState(
    val input: String = "",
    val isSending: Boolean = false,
    val items: List<ChatMessage> = emptyList(),
)