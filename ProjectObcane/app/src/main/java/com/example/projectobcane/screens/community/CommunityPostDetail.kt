package com.example.projectobcane.screens.community

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.projectobcane.R
import com.example.projectobcane.ui.elements.StatusChip
import com.example.projectobcane.ui.theme.basicMargin
import com.example.projectobcane.ui.theme.detailImageHeight
import com.example.projectobcane.ui.theme.dotSizeActive
import com.example.projectobcane.ui.theme.dotSizeInactive
import com.example.projectobcane.ui.theme.fabIconSize
import com.example.projectobcane.ui.theme.halfMargin
import com.example.projectobcane.ui.theme.iconSizeMedium
import com.example.projectobcane.ui.theme.iconSizeSmall
import com.example.projectobcane.ui.theme.iconXLarge
import com.example.projectobcane.ui.theme.largeCornerRadius
import com.example.projectobcane.ui.theme.mediumCornerRadius
import com.example.projectobcane.ui.theme.quarterMargin
import com.example.projectobcane.ui.theme.smallCornerRadius
import com.example.projectobcane.ui.theme.Purple

@Composable
fun PostDetailScreen(
    state: CommunityScreenUIState,
    viewModel: CommunityScreenViewModel,
    paddingValues: PaddingValues
) {
    val post = state.selectedPost ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = halfMargin, vertical = quarterMargin),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.closePost() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
            }
            Text(
                stringResource(R.string.report_detail),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = basicMargin)
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = basicMargin)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Outlined.Person, null, Modifier.size(iconSizeSmall), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(post.authorName ?: stringResource(R.string.anonymous), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("·", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(post.dateAdded.take(10), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (!post.place.isNullOrBlank()) {
                            Text("·", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(post.place, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Spacer(Modifier.height(halfMargin))
                    Text(post.description, style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(mediumCornerRadius))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(basicMargin)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(quarterMargin),
                            modifier = Modifier
                                .clip(RoundedCornerShape(smallCornerRadius))
                                .clickable { viewModel.toggleUpvote(post) }
                                .padding(6.dp)
                        ) {
                            Icon(
                                if (state.upvotedPostIds.contains(post.id)) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                null,
                                Modifier.size(iconSizeMedium),
                                tint = if (state.upvotedPostIds.contains(post.id)) Purple else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text("${post.upvoteCount}", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(quarterMargin)
                        ) {
                            Icon(Icons.Outlined.ChatBubbleOutline, null, Modifier.size(iconSizeMedium), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${post.commentCount}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(Modifier.weight(1f))
                        StatusChip(status = post.status)
                    }
                    Spacer(Modifier.height(mediumCornerRadius))
                }

                if (post.imageUrls.isNotEmpty()) {
                    val pagerState = rememberPagerState { post.imageUrls.size }
                    Box(
                        modifier = Modifier
                            .padding(horizontal = basicMargin)
                            .padding(bottom = halfMargin)
                    ) {
                        HorizontalPager(state = pagerState) { page ->
                            AsyncImage(
                                model = post.imageUrls[page],
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(detailImageHeight)
                                    .clip(RoundedCornerShape(mediumCornerRadius)),
                                contentScale = ContentScale.Crop
                            )
                        }
                        if (post.imageUrls.size > 1) {
                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = halfMargin),
                                horizontalArrangement = Arrangement.spacedBy(quarterMargin)
                            ) {
                                repeat(post.imageUrls.size) { i ->
                                    Box(
                                        modifier = Modifier
                                            .size(if (pagerState.currentPage == i) dotSizeActive else dotSizeInactive)
                                            .clip(CircleShape)
                                            .background(if (pagerState.currentPage == i) Color.White else Color.White.copy(0.5f))
                                    )
                                }
                            }
                        }
                    }
                } else {
                    AsyncImage(
                        model = "https://picsum.photos/seed/${post.id}/800/400",
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = basicMargin)
                            .padding(bottom = halfMargin)
                            .fillMaxWidth()
                            .height(detailImageHeight)
                            .clip(RoundedCornerShape(mediumCornerRadius)),
                        contentScale = ContentScale.Crop,
                        error = painterResource(android.R.drawable.ic_menu_gallery),
                        fallback = painterResource(android.R.drawable.ic_menu_gallery)
                    )
                }

                if (state.commentsLoading) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(basicMargin),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(iconXLarge))
                    }
                }
            }

            items(state.comments, key = { it.id }) { comment ->
                CommentItem(
                    comment = comment,
                    onReply = { viewModel.startReply(comment) }
                )
            }

            item { Spacer(Modifier.height(80.dp)) }
        }

        Surface(
            shadowElevation = halfMargin,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                if (state.replyingToComment != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Purple.copy(alpha = 0.08f))
                            .padding(horizontal = basicMargin, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            stringResource(R.string.replying_to, state.replyingToComment.authorName),
                            style = MaterialTheme.typography.labelSmall,
                            color = Purple,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { viewModel.cancelReply() },
                            modifier = Modifier.size(iconXLarge)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, Modifier.size(iconSizeSmall), tint = Purple)
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = mediumCornerRadius, vertical = halfMargin),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = state.newCommentText,
                        onValueChange = { viewModel.onCommentTextChange(it) },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                if (state.replyingToComment != null)
                                    stringResource(R.string.reply_to, state.replyingToComment.authorName)
                                else
                                    stringResource(R.string.add_comment)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(largeCornerRadius)
                    )
                    Spacer(Modifier.width(halfMargin))
                    IconButton(
                        onClick = { viewModel.submitComment() },
                        enabled = !state.isPostingComment && state.newCommentText.isNotBlank(),
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (!state.isPostingComment && state.newCommentText.isNotBlank()) Purple else Purple.copy(alpha = 0.4f))
                            .size(fabIconSize)
                    ) {
                        if (state.isPostingComment)
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                        else
                            Icon(Icons.Default.Add, stringResource(R.string.send), tint = Color.White)
                    }
                }
            }
        }
    }
}