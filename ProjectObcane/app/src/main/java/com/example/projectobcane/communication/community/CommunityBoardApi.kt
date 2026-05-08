package com.example.projectobcane.communication.community

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface CommunityBoardApi {

    @GET("community-board/{entityId}")
    suspend fun getPosts(
        @Path("entityId") entityId: Long
    ): Response<List<CommunityBoardPostDto>>

    @POST("community-board/")
    suspend fun createPost(
        @Body request: CreatePostRequest
    ): Response<CommunityBoardPostDto>

    @GET("community-board/comments/{entityId}/{postId}")
    suspend fun getComments(
        @Path("entityId") entityId: Long,
        @Path("postId") postId: Long
    ): Response<List<CommentDto>>

    @POST("community-board/create-comment/{entityId}")
    suspend fun createComment(
        @Path("entityId") entityId: Long,
        @Body request: CreateCommentRequest
    ): Response<CommentDto>

    @POST("community-board/upvote")
    suspend fun createUpvote(
        @Body request: UpvoteRequest
    ): Response<Unit>

    @HTTP(method = "DELETE", path = "community-board/upvote", hasBody = true)
    suspend fun removeUpvote(
        @Body request: UpvoteRequest
    ): Response<Unit>

    @GET("community-board/images/{postId}")
    suspend fun getImages(
        @Path("postId") postId: Long
    ): Response<ResponseBody>
}