package com.example.projectobcane.screens.community

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.BuildConfig
import com.example.projectobcane.communication.CommunicationResult
import com.example.projectobcane.communication.community.*
import com.example.projectobcane.models.CommunityPostUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class CommentUi(
    val id: Long,
    val authorName: String,
    val content: String,
    val createdAt: String,
    val localLikes: Int = 0,
    val isLiked: Boolean = false
)

@HiltViewModel
class CommunityScreenViewModel @Inject constructor(
    private val repository: ICommunityBoardRemoteRepository
) : ViewModel() {

    private val entityId = 1932L
    private val currentUserEmail = BuildConfig.AUTH_USERNAME
    private val currentUserEmailHash = sha256(currentUserEmail)
    private val currentUserName = currentUserEmail.substringBefore("@")

    private val _state = MutableStateFlow(
        CommunityScreenUIState(currentUserEmailHash = sha256(BuildConfig.AUTH_USERNAME))
    )
    val state = _state.asStateFlow()

    fun loadPosts() {
        Log.d("COMMUNITY_VM", "loadPosts()")
        val preservedUpvotes = _state.value.upvotedPostIds
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            when (val result = repository.getPosts(entityId)) {
                is CommunicationResult.Success -> {
                    Log.d("COMMUNITY_VM", "got ${result.data.size} posts")
                    val posts = result.data.map { dto ->
                        async {
                            val images = when (val img = repository.getImages(dto.id)) {
                                is CommunicationResult.Success -> img.data
                                else -> emptyList()
                            }
                            dto.toUi(images)
                        }
                    }.map { it.await() }
                    _state.value = _state.value.copy(
                        loading = false,
                        posts = posts,
                        upvotedPostIds = preservedUpvotes
                    )
                }
                is CommunicationResult.Error ->
                    _state.value = _state.value.copy(loading = false, error = "Chyba načítání (${result.error.code})")
                else ->
                    _state.value = _state.value.copy(loading = false, error = "Chyba připojení")
            }
        }
    }

    fun showCreateDialog() { _state.value = _state.value.copy(showCreateDialog = true) }

    fun hideCreateDialog() {
        _state.value = _state.value.copy(
            showCreateDialog = false,
            createTitle = "", createDescription = "", createPlace = "",
            createType = "IDEA", createSeverity = "Střední",
            createImageUris = emptyList()
        )
    }

    fun onCreateTitleChange(v: String) { _state.value = _state.value.copy(createTitle = v) }
    fun onCreateDescChange(v: String) { _state.value = _state.value.copy(createDescription = v) }
    fun onCreatePlaceChange(v: String) { _state.value = _state.value.copy(createPlace = v) }
    fun onCreateTypeChange(v: String) { _state.value = _state.value.copy(createType = v) }
    fun onCreateSeverityChange(v: String) { _state.value = _state.value.copy(createSeverity = v) }

    fun onPhotoSelected(context: Context, uri: String) {
        _state.value = _state.value.copy(createImageUris = _state.value.createImageUris + uri)
    }

    fun removePhoto(uri: String) {
        _state.value = _state.value.copy(createImageUris = _state.value.createImageUris - uri)
    }

    fun submitPost() {
        val s = _state.value
        if (s.createDescription.isBlank()) return
        viewModelScope.launch {
            _state.value = _state.value.copy(isCreating = true)
            val req = CreatePostRequest(
                authorEmail = currentUserEmail,
                authorName = currentUserName,
                description = s.createDescription,
                entityId = entityId,
                place = s.createPlace,
                postType = s.createType,
                title = s.createDescription.take(60)
            )
            when (repository.createPost(req)) {
                is CommunicationResult.Success -> {
                    _state.value = _state.value.copy(
                        isCreating = false, showCreateDialog = false,
                        createTitle = "", createDescription = "", createPlace = "",
                        createType = "IDEA", createSeverity = "Střední",
                        createImageUris = emptyList()
                    )
                    loadPosts()
                }
                else -> _state.value = _state.value.copy(isCreating = false)
            }
        }
    }

    fun openPost(post: CommunityPostUi) {
        _state.value = _state.value.copy(
            selectedPost = post,
            comments = emptyList(),
            replyingToComment = null,
            newCommentText = ""
        )
        loadComments(post.id)
    }

    fun closePost() {
        _state.value = _state.value.copy(
            selectedPost = null,
            comments = emptyList(),
            replyingToComment = null,
            newCommentText = ""
        )
    }

    private fun loadComments(postId: Long) {
        viewModelScope.launch {
            _state.value = _state.value.copy(commentsLoading = true)
            when (val result = repository.getComments(entityId, postId)) {
                is CommunicationResult.Success -> {
                    _state.value = _state.value.copy(
                        commentsLoading = false,
                        comments = result.data.map {
                            CommentUi(it.id, it.authorName, it.content, it.createdAt)
                        }
                    )
                }
                else -> _state.value = _state.value.copy(commentsLoading = false)
            }
        }
    }

    fun onCommentTextChange(v: String) { _state.value = _state.value.copy(newCommentText = v) }

    fun startReply(comment: CommentUi) {
        _state.value = _state.value.copy(
            replyingToComment = comment,
            newCommentText = "@${comment.authorName} "
        )
    }

    fun cancelReply() {
        _state.value = _state.value.copy(replyingToComment = null, newCommentText = "")
    }

    fun submitComment() {
        val s = _state.value
        val postId = s.selectedPost?.id ?: return
        if (s.newCommentText.isBlank()) return
        viewModelScope.launch {
            _state.value = _state.value.copy(isPostingComment = true)
            val req = CreateCommentRequest(
                authorName = currentUserName,
                content = s.newCommentText,
                createdAt = DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
                postId = postId
            )
            when (val result = repository.createComment(entityId, req)) {
                is CommunicationResult.Success -> {
                    _state.value = _state.value.copy(
                        isPostingComment = false,
                        newCommentText = "",
                        replyingToComment = null,
                        comments = _state.value.comments + CommentUi(
                            result.data.id, result.data.authorName,
                            result.data.content, result.data.createdAt
                        )
                    )
                    updatePostInList(postId) { it.copy(commentCount = it.commentCount + 1) }
                }
                else -> _state.value = _state.value.copy(isPostingComment = false)
            }
        }
    }

    fun toggleCommentLike(commentId: Long) {
        _state.value = _state.value.copy(
            comments = _state.value.comments.map { c ->
                if (c.id == commentId) {
                    if (c.isLiked) c.copy(isLiked = false, localLikes = (c.localLikes - 1).coerceAtLeast(0))
                    else c.copy(isLiked = true, localLikes = c.localLikes + 1)
                } else c
            }
        )
    }

    fun toggleUpvote(post: CommunityPostUi) {
        val livePost = _state.value.posts.find { it.id == post.id }
            ?: _state.value.selectedPost?.takeIf { it.id == post.id }
            ?: post
        val alreadyUpvoted = _state.value.upvotedPostIds.contains(livePost.id)
        viewModelScope.launch {
            if (alreadyUpvoted) {
                _state.value = _state.value.copy(upvotedPostIds = _state.value.upvotedPostIds - livePost.id)
                updatePostInList(livePost.id) { it.copy(upvoteCount = (it.upvoteCount - 1).coerceAtLeast(0)) }
                when (repository.removeUpvote(UpvoteRequest(livePost.id, currentUserEmailHash))) {
                    is CommunicationResult.Success -> Log.d("COMMUNITY_VM", "removeUpvote SUCCESS")
                    else -> {
                        _state.value = _state.value.copy(upvotedPostIds = _state.value.upvotedPostIds + livePost.id)
                        updatePostInList(livePost.id) { it.copy(upvoteCount = it.upvoteCount + 1) }
                        Log.e("COMMUNITY_VM", "removeUpvote FAILED, rolled back")
                    }
                }
            } else {
                _state.value = _state.value.copy(upvotedPostIds = _state.value.upvotedPostIds + livePost.id)
                updatePostInList(livePost.id) { it.copy(upvoteCount = it.upvoteCount + 1) }
                when (repository.createUpvote(UpvoteRequest(livePost.id, currentUserEmailHash))) {
                    is CommunicationResult.Success -> Log.d("COMMUNITY_VM", "createUpvote SUCCESS")
                    else -> {
                        _state.value = _state.value.copy(upvotedPostIds = _state.value.upvotedPostIds - livePost.id)
                        updatePostInList(livePost.id) { it.copy(upvoteCount = (it.upvoteCount - 1).coerceAtLeast(0)) }
                        Log.e("COMMUNITY_VM", "createUpvote FAILED, rolled back")
                    }
                }
            }
        }
    }

    fun toggleMyPosts(show: Boolean) {
        _state.value = _state.value.copy(showMyPosts = show)
    }

    private fun updatePostInList(postId: Long, transform: (CommunityPostUi) -> CommunityPostUi) {
        _state.value = _state.value.copy(
            posts = _state.value.posts.map { if (it.id == postId) transform(it) else it },
            selectedPost = _state.value.selectedPost?.let { if (it.id == postId) transform(it) else it }
        )
    }

    private fun CommunityBoardPostDto.toUi(images: List<String>) = CommunityPostUi(
        id = id, title = title, description = description,
        authorName = authorName, dateAdded = dateAdded, status = status,
        postType = postType, upvoteCount = upvoteCount, commentCount = commentCount,
        place = place, hashedEmail = hashedEmail, imageUrls = images
    )

    private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}