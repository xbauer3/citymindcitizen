package com.example.projectobcane.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.R
import com.example.projectobcane.communication.AiChatRequest
import com.example.projectobcane.communication.CommunicationResult
import com.example.projectobcane.communication.IAiChatRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random
import javax.inject.Inject

enum class ChatRole { User, Assistant }

data class ChatMessage(
    val id: String,
    val role: ChatRole,
    val text: String,           // full final text
    val displayText: String = "",  // what's currently shown (drives the typing effect)
    val isTyping: Boolean = false
)

data class AiChatUiState(
    val input: String = "",
    val isSending: Boolean = false,
    val items: List<ChatMessage> = emptyList(),
    val faq: List<String> = emptyList()
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
            text = msg,
            displayText = msg
        )

        _state.value = _state.value.copy(
            input = "",
            isSending = true,
            items = _state.value.items + userMsg,
            faq = emptyList()
        )

        viewModelScope.launch {
            val req = AiChatRequest(
                entityId = 1932,
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
                    val fullText = res.data.text
                    //val fullText = res.data.trim().ifEmpty { "" }
                    val faq = res.data.faq

                    val botMsgId = "a_${System.currentTimeMillis()}"
                    val botMsg = ChatMessage(
                        id = botMsgId,
                        role = ChatRole.Assistant,
                        text = fullText,
                        displayText = "",
                        isTyping = true
                    )

                    questions.add(msg)
                    answers.add(fullText)


                    _state.value = _state.value.copy(
                        isSending = false,
                        items = _state.value.items + botMsg,
                        faq = emptyList()
                    )

                    // Type out the characters
                    typeOutMessage(botMsgId, fullText, faq)
                }

                is CommunicationResult.Error -> {
                    val errorMsg = ChatMessage(
                        id = "a_${System.currentTimeMillis()}",
                        role = ChatRole.Assistant,
                        text = "Omlouvám se, nepodařilo se mi odpovědět.",
                        displayText = "Omlouvám se, nepodařilo se mi odpovědět."
                    )
                    _state.value = _state.value.copy(
                        isSending = false,
                        items = _state.value.items + errorMsg
                    )
                }

                is CommunicationResult.ConnectionError -> {
                    val errorMsg = ChatMessage(
                        id = "a_${System.currentTimeMillis()}",
                        role = ChatRole.Assistant,
                        text = "Chyba připojení.",
                        displayText = "Chyba připojení."
                    )
                    _state.value = _state.value.copy(
                        isSending = false,
                        items = _state.value.items + errorMsg
                    )
                }

                is CommunicationResult.Exception -> {
                    val errorMsg = ChatMessage(
                        id = "a_${System.currentTimeMillis()}",
                        role = ChatRole.Assistant,
                        text = "Nastala neočekávaná chyba.",
                        displayText = "Nastala neočekávaná chyba."
                    )
                    _state.value = _state.value.copy(
                        isSending = false,
                        items = _state.value.items + errorMsg
                    )
                }
            }
        }
    }

    private suspend fun typeOutMessage(msgId: String, fullText: String, faq: List<String>) {
        // Chunk characters into small groups so it feels fast but smooth
        // Short texts type faster, long texts slightly faster per chunk
        val chunkSize = if (fullText.length > 200) 3 else 2
        val delayMs = 18L

        var charIndex = 0
        while (charIndex < fullText.length) {
            val end = minOf(charIndex + chunkSize, fullText.length)
            val revealed = fullText.substring(0, end)

            val currentItems = _state.value.items

            val updatedItems = currentItems.map { item ->
                if (item.id == msgId && item is ChatMessage && item.role == ChatRole.Assistant) {
                    item.copy(displayText = revealed, isTyping = end < fullText.length)
                } else {
                    item
                }
            }
            _state.value = _state.value.copy(items = updatedItems)

            charIndex = end
            delay(delayMs)
        }
        _state.value = _state.value.copy(faq = faq)
    }

    private fun newConversationId(): Long =
        Random.nextLong(2_000_000_000_000_000_000L, 2_100_000_000_000_000_000L)
}