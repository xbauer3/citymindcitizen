package com.example.projectobcane.screens.community

import com.example.projectobcane.models.CommunityPostUi

data class CommunityScreenUIState(
    val posts: List<CommunityPostUi> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,

    val showCreateDialog: Boolean = false,
    val createTitle: String = "",
    val createDescription: String = "",
    val createPlace: String = "",
    val createType: String = "IDEA",
    val createSeverity: String = "Střední",
    val createImageUris: List<String> = emptyList(),
    val isCreating: Boolean = false,

    val selectedPost: CommunityPostUi? = null,
    val comments: List<CommentUi> = emptyList(),
    val commentsLoading: Boolean = false,
    val newCommentText: String = "",
    val isPostingComment: Boolean = false,
    val replyingToComment: CommentUi? = null,
    val showMyPosts: Boolean = false,
    val currentUserEmailHash: String = "",

    val upvotedPostIds: Set<Long> = emptySet()
)