package com.example.projectobcane.communication

interface IAiChatRemoteRepository : IBaseRemoteRepository {
    suspend fun sendMessage(request: AiChatRequest): CommunicationResult<String>
}
