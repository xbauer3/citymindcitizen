package com.example.projectobcane.communication

import okhttp3.ResponseBody
import javax.inject.Inject

class AiChatRemoteRepositoryImpl @Inject constructor(
    private val api: AiChatApi
) : IAiChatRemoteRepository {

    override suspend fun sendMessage(request: AiChatRequest): CommunicationResult<String> {
        return try {
            val response = api.chat(request)
            if (response.isSuccessful) {
                val bodyText = response.body()?.string().orEmpty()
                CommunicationResult.Success(bodyText)
            } else {
                CommunicationResult.Error(
                    CommunicationError(
                        code = response.code(),
                        message = response.errorBody()?.string() ?: "Unknown error"
                    )
                )
            }
        } catch (t: Throwable) {
            CommunicationResult.Error(
                CommunicationError(
                    code = 469,
                    message = t.message ?: "Network error"
                )
            )
        }
    }
}
