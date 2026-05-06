package com.example.projectobcane.screens.chat

import java.util.UUID

sealed interface ChatItem {
    val id: String

    data class User(
        override val id: String = UUID.randomUUID().toString(),
        val text: String
    ) : ChatItem

    data class Assistant(
        override val id: String = UUID.randomUUID().toString(),
        val text: String,
        val isThinking: Boolean = false
    ) : ChatItem

    data class Faq(
        override val id: String = UUID.randomUUID().toString(),
        val questions: List<String>
    ) : ChatItem
}
