package com.example.projectobcane.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.communication.AiChatRequest
import com.example.projectobcane.communication.CommunicationResult
import com.example.projectobcane.communication.IAiChatRemoteRepository
import com.example.projectobcane.screens.settings.LanguageHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random
import javax.inject.Inject


data class AiChatUiState(
    val items: List<ChatItem> = emptyList(),
    val input: String = "",
    val isSending: Boolean = false,
)


@HiltViewModel
class AiChatViewModel @Inject constructor(
    private val repo: IAiChatRemoteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AiChatUiState())
    val state = _state.asStateFlow()

    private val entityId = 1911L
    private var conversationId: Long = Random.nextLong(2_000_000_000_000_000L, 9_000_000_000_000_000L)
    private val questions = mutableListOf<String>()
    private val answers = mutableListOf<String>()

    fun onInputChange(value: String) {
        _state.value = _state.value.copy(input = value)
    }

    fun resetConversation() {
        conversationId = Random.nextLong(2_000_000_000_000_000L, 9_000_000_000_000_000L)
        questions.clear()
        answers.clear()
        _state.value = AiChatUiState()
    }

    fun send() {
        val msg = _state.value.input.trim()
        if (msg.isBlank() || _state.value.isSending) return

        val thinkingId = "thinking-${System.currentTimeMillis()}"

        _state.value = _state.value.copy(
            input = "",
            isSending = true,
            items = _state.value.items +
                ChatItem.User(text = msg) +
                ChatItem.Assistant(id = thinkingId, text = "Přemýšlím nad vaší otázkou…", isThinking = true)
        )

        val request = AiChatRequest(
            entityId = entityId,
            msg = msg,
            questions = questions.toList(),
            answers = answers.toList(),
            conversationId = conversationId,
            conversationLang = if (LanguageHolder.language == "cs") "Czech" else "English",
            contextIds = listOf(null),
            additionalDatasets = emptyList(),
            webConfig = emptyMap()
        )

        viewModelScope.launch {
            val result = repo.sendMessage(request)

            val answerText = when (result) {
                is CommunicationResult.Success -> result.data
                is CommunicationResult.Error -> "Omlouvám se, nepodařilo se mi odpovědět. (${result.error.message ?: "chyba"})"
                is CommunicationResult.ConnectionError -> "Nejste připojeni k internetu."
                is CommunicationResult.Exception -> "Nastala chyba: ${result.exception.message ?: "neznámá"}"
            }

            questions.add(msg)
            answers.add(answerText)

            _state.value = _state.value.copy(
                isSending = false,
                items = _state.value.items.map {
                    if (it is ChatItem.Assistant && it.id == thinkingId) it.copy(text = answerText, isThinking = false)
                    else it
                }
            )
        }
    }
}
