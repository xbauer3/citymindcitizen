package com.example.projectobcane.communication.community

import com.example.projectobcane.communication.CommunicationResult
import com.example.projectobcane.communication.IBaseRemoteRepository

interface ICommunityBoardRemoteRepository : IBaseRemoteRepository {
    suspend fun getPosts(entityId: Long): CommunicationResult<List<CommunityBoardPostDto>>
    suspend fun createPost(request: CreatePostRequest): CommunicationResult<CommunityBoardPostDto>
    suspend fun getComments(entityId: Long, postId: Long): CommunicationResult<List<CommentDto>>
    suspend fun createComment(entityId: Long, request: CreateCommentRequest): CommunicationResult<CommentDto>
    suspend fun createUpvote(request: UpvoteRequest): CommunicationResult<Unit>
    suspend fun removeUpvote(request: UpvoteRequest): CommunicationResult<Unit>
    suspend fun getImages(postId: Long): CommunicationResult<List<String>>
}