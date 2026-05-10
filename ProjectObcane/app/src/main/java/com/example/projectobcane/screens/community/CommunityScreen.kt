package com.example.projectobcane.screens.community

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.projectobcane.R
import com.example.projectobcane.models.CommunityPostUi
import com.example.projectobcane.ui.elements.StatusChip
import com.example.projectobcane.ui.theme.*

@Composable
fun CommunityScreen(paddingValues: PaddingValues) {
    val viewModel = hiltViewModel<CommunityScreenViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.loadPosts() }

    if (state.selectedPost != null) {
        PostDetailScreen(state = state, viewModel = viewModel, paddingValues = paddingValues)
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            state.loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            state.error != null -> Text(
                text = state.error!!,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(doubleMargin),
                color = MaterialTheme.colorScheme.error
            )
            else -> {
                val filteredPosts = if (state.showMyPosts) {
                    state.posts.filter { it.hashedEmail == state.currentUserEmailHash }
                } else {
                    state.posts
                }

                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = basicMargin, vertical = halfMargin),
                        horizontalArrangement = Arrangement.spacedBy(halfMargin)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(chipCornerRadius))
                                .background(if (!state.showMyPosts) Purple else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { viewModel.toggleMyPosts(false) }
                                .padding(horizontal = basicMargin, vertical = halfMargin)
                        ) {
                            Text(
                                stringResource(R.string.all_reports),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if (!state.showMyPosts) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(chipCornerRadius))
                                .background(if (state.showMyPosts) Purple else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { viewModel.toggleMyPosts(true) }
                                .padding(horizontal = basicMargin, vertical = halfMargin)
                        ) {
                            Text(
                                stringResource(R.string.my_reports),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if (state.showMyPosts) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (filteredPosts.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(halfMargin)
                            ) {
                                Text(
                                    if (state.showMyPosts) stringResource(R.string.no_reports_yet) else stringResource(R.string.help_improve_university),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    if (state.showMyPosts) stringResource(R.string.create_first_report) else stringResource(R.string.report_problem_or_suggestion),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        LazyColumn(contentPadding = PaddingValues(horizontal = basicMargin, vertical = quarterMargin)) {
                            items(filteredPosts, key = { it.id }) { post ->
                                PostCard(
                                    post = post,
                                    isUpvoted = state.upvotedPostIds.contains(post.id),
                                    onClick = { viewModel.openPost(post) },
                                    onUpvote = { viewModel.toggleUpvote(post) }
                                )
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { viewModel.showCreateDialog() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(doubleMargin),
            containerColor = Purple,
            contentColor = Color.White,
            shape = RoundedCornerShape(cardCornerRadius)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = basicMargin),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(halfMargin)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text(stringResource(R.string.new_report), fontWeight = FontWeight.SemiBold)
            }
        }
    }

    if (state.showCreateDialog) {
        val view = LocalView.current
        DisposableEffect(Unit) {
            val window = (view.context as android.app.Activity).window
            val controller = WindowInsetsControllerCompat(window, window.decorView)
            controller.isAppearanceLightStatusBars = false
            onDispose {
                controller.isAppearanceLightStatusBars = false
            }
        }
        CreatePostBottomSheet(state = state, viewModel = viewModel)
    }
}

@Composable
private fun PostCard(
    post: CommunityPostUi,
    isUpvoted: Boolean,
    onClick: () -> Unit,
    onUpvote: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = mediumCornerRadius)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(cardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            if (post.imageUrls.isNotEmpty()) {
                val pagerState = rememberPagerState { post.imageUrls.size }
                Box {
                    HorizontalPager(state = pagerState) { page ->
                        AsyncImage(
                            model = post.imageUrls[page],
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(cardImageHeight)
                                .clip(RoundedCornerShape(topStart = cardCornerRadius, topEnd = cardCornerRadius)),
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
                                        .background(if (pagerState.currentPage == i) Color.White else Color.White.copy(alpha = 0.5f))
                                )
                            }
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(basicMargin)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Outlined.Person, null, Modifier.size(iconSizeSmall), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(stringResource(R.string.anonymous), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("·", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
                    Text(post.dateAdded.take(10), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (!post.place.isNullOrBlank()) {
                        Text("·", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
                        Text(post.place, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Spacer(Modifier.height(6.dp))
                Text(post.description, style = MaterialTheme.typography.bodyMedium, maxLines = 4, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(chipPaddingHorizontal))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(mediumCornerRadius)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(quarterMargin),
                            modifier = Modifier
                                .clip(RoundedCornerShape(smallCornerRadius))
                                .clickable { onUpvote() }
                                .padding(horizontal = 6.dp, vertical = chipPaddingVertical)
                        ) {
                            Icon(
                                if (isUpvoted) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                stringResource(R.string.upvote),
                                Modifier.size(basicMargin),
                                tint = if (isUpvoted) Purple else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(post.upvoteCount.toString(), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(quarterMargin)
                        ) {
                            Icon(Icons.Outlined.ChatBubbleOutline, null, Modifier.size(basicMargin), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(post.commentCount.toString(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    StatusChip(status = post.status)
                }
            }
        }
    }
}