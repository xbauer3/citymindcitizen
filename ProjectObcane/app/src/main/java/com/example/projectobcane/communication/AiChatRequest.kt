package com.example.projectobcane.communication

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AiChatRequest(
    val entityId: Long,
    val msg: String,
    val questions: List<String>,
    val answers: List<String>,
    val conversationId: Long,
    val conversationLang: String,
    val contextIds: List<String?> = listOf(null),
    val additionalDatasets: List<String> = emptyList(),
    val webConfig: Map<String, Any?> = emptyMap(),
)
