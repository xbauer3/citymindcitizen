package com.example.projectobcane.communication.community

import android.util.Log
import com.example.projectobcane.communication.CommunicationResult
import com.example.projectobcane.communication.CommunicationError
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class CommunityBoardRemoteRepositoryImpl @Inject constructor(
    private val api: CommunityBoardApi
) : ICommunityBoardRemoteRepository {

    override suspend fun getPosts(entityId: Long): CommunicationResult<List<CommunityBoardPostDto>> {
        Log.d("COMMUNITY", "getPosts(entityId=$entityId)")
        return processResponse { api.getPosts(entityId) }.also { logResult("getPosts", it) }
    }

    override suspend fun createPost(request: CreatePostRequest): CommunicationResult<CommunityBoardPostDto> {
        Log.d("COMMUNITY", "createPost(title=${request.title})")
        return processResponse { api.createPost(request) }.also { logResult("createPost", it) }
    }

    override suspend fun getComments(entityId: Long, postId: Long): CommunicationResult<List<CommentDto>> {
        Log.d("COMMUNITY", "getComments(postId=$postId)")
        return processResponse { api.getComments(entityId, postId) }.also { logResult("getComments", it) }
    }

    override suspend fun createComment(entityId: Long, request: CreateCommentRequest): CommunicationResult<CommentDto> {
        Log.d("COMMUNITY", "createComment(postId=${request.postId})")
        return processResponse { api.createComment(entityId, request) }.also { logResult("createComment", it) }
    }

    override suspend fun createUpvote(request: UpvoteRequest): CommunicationResult<Unit> {
        Log.d("COMMUNITY", "createUpvote(postId=${request.postId})")
        return processResponse { api.createUpvote(request) }.also { logResult("createUpvote", it) }
    }

    override suspend fun removeUpvote(request: UpvoteRequest): CommunicationResult<Unit> {
        Log.d("COMMUNITY", "removeUpvote(postId=${request.postId})")
        return processResponse { api.removeUpvote(request) }.also { logResult("removeUpvote", it) }
    }

    override suspend fun getImages(postId: Long): CommunicationResult<List<String>> {
        Log.d("COMMUNITY", "getImages(postId=$postId)")
        return try {
            val response = api.getImages(postId)
            if (response.isSuccessful) {
                val raw = response.body()?.string().orEmpty()
                Log.d("COMMUNITY", "getImages raw response: $raw")
                // API returns a map/object — extract all string values as URLs
                val urls = mutableListOf<String>()
                try {
                    val obj = JSONObject(raw)
                    obj.keys().forEach { key -> urls.add(obj.getString(key)) }
                } catch (_: Exception) {
                    try {
                        val arr = JSONArray(raw)
                        for (i in 0 until arr.length()) urls.add(arr.getString(i))
                    } catch (_: Exception) {}
                }
                Log.d("COMMUNITY", "getImages parsed ${urls.size} urls")
                CommunicationResult.Success(urls)
            } else {
                Log.e("COMMUNITY", "getImages ERROR ${response.code()}")
                CommunicationResult.Success(emptyList()) // no images is not a fatal error
            }
        } catch (t: Throwable) {
            Log.e("COMMUNITY", "getImages EXCEPTION: ${t.message}")
            CommunicationResult.Success(emptyList())
        }
    }

    private fun <T : Any> logResult(call: String, result: CommunicationResult<T>) {
        when (result) {
            is CommunicationResult.Success -> Log.d("COMMUNITY", "$call SUCCESS")
            is CommunicationResult.Error -> Log.e("COMMUNITY", "$call ERROR ${result.error.code}: ${result.error.message}")
            is CommunicationResult.ConnectionError -> Log.e("COMMUNITY", "$call CONNECTION ERROR")
            is CommunicationResult.Exception -> Log.e("COMMUNITY", "$call EXCEPTION: ${result.exception.message}")
        }
    }
}