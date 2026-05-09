package com.example.projectobcane.screens.chat


enum class ChatRole { User, Assistant }

data class ChatMessage(
    val id: String,
    val role: ChatRole,
    val text: String,           // full final text
    val displayText: String = "",  // what's currently shown (drives the typing effect)
    val isTyping: Boolean = false,
    val faq: List<String> = emptyList()
)