package com.example.projectobcane.communication

import android.util.Log
import org.json.JSONObject
import javax.inject.Inject

class AiChatRemoteRepositoryImpl @Inject constructor(
    private val api: AiChatApi
) : IAiChatRemoteRepository {

    override suspend fun sendMessage(request: AiChatRequest): CommunicationResult<AiChatResponse> {
        return try {
            val response = api.chat(request)

            Log.d("AI_DEBUG", "Code: ${response.code()}")
            Log.d("AI_DEBUG", "Headers: ${response.headers()}")

            if (response.isSuccessful) {
                val rawBody = response.body()?.string().orEmpty()
                val parsed = parseSseResponse(rawBody)
                Log.d("AI_DEBUG", "Parsed response: $parsed")
                CommunicationResult.Success(parsed)
            } else {
                CommunicationResult.Error(
                    CommunicationError(
                        code = response.code(),
                        message = response.errorBody()?.string() ?: "Unknown error"
                    )
                )
            }
        } catch (t: Throwable) {
            Log.e("AI_DEBUG", "Exception: ${t.message}", t)
            CommunicationResult.Error(
                CommunicationError(
                    code = 469,
                    message = t.message ?: "Network error"
                )
            )
        }

}

    /**
     * Parses a Server-Sent Events body into a plain string.
     * Lines look like:  data: {"response": "Aho", "id": 1}
     *
     */
    private fun parseSseResponse(raw: String): AiChatResponse {
        val textBuilder = StringBuilder()
        val faqList = mutableListOf<String>()

        raw.lines().forEach { line ->
            val trimmed = line.trim()

            if (!trimmed.startsWith("data:")) return@forEach

            val json = trimmed.removePrefix("data:").trim()

            try {
                val obj = JSONObject(json)

                // TEXT STREAM
                if (obj.has("response")) {
                    textBuilder.append(obj.getString("response"))
                }

                // FAQ
                if (obj.has("faq")) {
                    val arr = obj.getJSONArray("faq")
                    for (i in 0 until arr.length()) {
                        faqList.add(arr.getString(i))
                    }
                }

            } catch (_: Exception) {
                // ignore
            }
        }

        return AiChatResponse(
            text = textBuilder.toString().trim()
                .ifEmpty { "Omlouvám se, nedostal jsem odpověď." },
            faq = faqList
        )
    }
}