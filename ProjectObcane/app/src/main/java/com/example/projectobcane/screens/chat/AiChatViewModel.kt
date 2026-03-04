package com.example.projectobcane.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.communication.AiChatRequest
import com.example.projectobcane.communication.CommunicationResult
import com.example.projectobcane.communication.IAiChatRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random
import javax.inject.Inject

enum class ChatRole { User, Assistant }

data class ChatMessage(
    val id: String,
    val role: ChatRole,
    val text: String
)

data class AiChatUiState(
    val input: String = "",
    val isSending: Boolean = false,
    val items: List<ChatMessage> = emptyList()
)



@HiltViewModel
class AiChatViewModel @Inject constructor(
    private val repo: IAiChatRemoteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AiChatUiState())
    val state = _state.asStateFlow()

    private var conversationId: Long = newConversationId()
    private val questions = mutableListOf<String>()
    private val answers = mutableListOf<String>()

    fun onInputChange(v: String) {
        _state.value = _state.value.copy(input = v)
    }

    fun resetConversation() {
        conversationId = newConversationId()
        questions.clear()
        answers.clear()
        _state.value = AiChatUiState()
    }

    fun send() {
        val msg = _state.value.input.trim()
        if (msg.isEmpty() || _state.value.isSending) return

        val userMsg = ChatMessage(
            id = "u_${System.currentTimeMillis()}",
            role = ChatRole.User,
            text = msg
        )

        _state.value = _state.value.copy(
            input = "",
            isSending = true,
            items = _state.value.items + userMsg
        )

        viewModelScope.launch {
            val req = AiChatRequest(
                entityId = 1911,
                msg = msg,
                questions = questions.toList(),
                answers = answers.toList(),
                conversationId = conversationId,
                conversationLang = "Czech",
                contextIds = listOf(null),
                additionalDatasets = emptyList(),
                webConfig = emptyMap()
            )


            when (val res = repo.sendMessage(req)) {
                is CommunicationResult.Success -> {
                    val a = res.data.trim().ifEmpty { "Omlouvám se, nedostal jsem odpověď." }
                    val botMsg = ChatMessage(
                        id = "a_${System.currentTimeMillis()}",
                        role = ChatRole.Assistant,
                        text = a
                    )
                    questions.add(msg)
                    answers.add(a)

                    _state.value = _state.value.copy(
                        isSending = false,
                        items = _state.value.items + botMsg
                    )
                }

                is CommunicationResult.Error -> {
                    val botMsg = ChatMessage(
                        id = "a_${System.currentTimeMillis()}",
                        role = ChatRole.Assistant,
                        text = "Omlouvám se, nepodařilo se mi odpovědět."
                    )
                    _state.value = _state.value.copy(
                        isSending = false,
                        items = _state.value.items + botMsg
                    )
                }

                is CommunicationResult.ConnectionError -> TODO()
                is CommunicationResult.Exception -> TODO()
            }
        }
    }

    private fun newConversationId(): Long =
        Random.nextLong(2_000_000_000_000_000_000L, 2_100_000_000_000_000_000L)
}