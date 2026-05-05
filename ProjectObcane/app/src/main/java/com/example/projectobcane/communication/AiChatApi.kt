package com.example.projectobcane.communication

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Backend endpoint for AI agent.
 *
 */
interface AiChatApi {


    //maybe change it to aurora/get
    @POST("get")
    suspend fun chat(@Body request: AiChatRequest): Response<ResponseBody>
}
