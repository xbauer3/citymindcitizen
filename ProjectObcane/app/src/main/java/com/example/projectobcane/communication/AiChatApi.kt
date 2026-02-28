package com.example.projectobcane.communication

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Backend endpoint for AI agent.
 *
 * NOTE: If your backend uses a different path, change it here.
 */
interface AiChatApi {

    @POST("chat")
    suspend fun chat(@Body request: AiChatRequest): Response<ResponseBody>
}
