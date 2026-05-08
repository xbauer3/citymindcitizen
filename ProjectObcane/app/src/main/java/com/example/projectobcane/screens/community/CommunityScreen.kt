package com.example.projectobcane.screens.community

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.projectobcane.R
import com.example.projectobcane.models.CommunityPostUi
import com.example.projectobcane.ui.theme.*

private val Purple = Color(0xFF7822FF)

@Composable
fun CommunityScreen(paddingValues: PaddingValues) {
    val viewModel = hiltViewModel<CommunityScreenViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.loadPosts() }

    if (state.selectedPost != null) {
        PostDetailScreen(state = state, viewModel = viewModel, paddingValues = paddingValues)
        return
    }

    Box(modifier = Modifier.fillMaxSize().padding(paddingValues).background(MaterialTheme.colorScheme.surface)) {
        when {
            state.loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            state.error != null -> Text(
                text = state.error!!,
                modifier = Modifier.align(Alignment.Center).padding(doubleMargin),
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
                        modifier = Modifier.fillMaxWidth().padding(horizontal = basicMargin, vertical = halfMargin),
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
            modifier = Modifier.align(Alignment.BottomEnd).padding(doubleMargin),
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
        modifier = Modifier.fillMaxWidth().padding(bottom = mediumCornerRadius).clickable(onClick = onClick),
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
                            modifier = Modifier.fillMaxWidth().height(cardImageHeight).clip(RoundedCornerShape(topStart = cardCornerRadius, topEnd = cardCornerRadius)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    if (post.imageUrls.size > 1) {
                        Row(
                            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = halfMargin),
                            horizontalArrangement = Arrangement.spacedBy(quarterMargin)
                        ) {
                            repeat(post.imageUrls.size) { i ->
                                Box(modifier = Modifier.size(if (pagerState.currentPage == i) dotSizeActive else dotSizeInactive).clip(CircleShape).background(if (pagerState.currentPage == i) Color.White else Color.White.copy(alpha = 0.5f)))
                            }
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(basicMargin)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
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
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(mediumCornerRadius)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(quarterMargin),
                            modifier = Modifier.clip(RoundedCornerShape(smallCornerRadius)).clickable { onUpvote() }.padding(horizontal = 6.dp, vertical = chipPaddingVertical)
                        ) {
                            Icon(if (isUpvoted) Icons.Filled.Star else Icons.Outlined.Star, stringResource(R.string.upvote), Modifier.size(basicMargin), tint = if (isUpvoted) Color(0xFFFFC107) else MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(post.upvoteCount.toString(), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(quarterMargin)) {
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

@Composable
private fun StatusChip(status: String) {
    val (bg, textColor) = when (status) {
        "NEW" -> Color(0xFFEDE7FF) to Purple
        "IN_PROGRESS" -> Color(0xFFFFF3E0) to Color(0xFFE65100)
        "SOLVED" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
        else -> Color(0xFFF5F5F5) to Color(0xFF757575)
    }
    val label = when (status) {
        "NEW" -> stringResource(R.string.status_new)
        "IN_PROGRESS" -> stringResource(R.string.status_in_progress)
        "SOLVED" -> stringResource(R.string.status_solved)
        else -> stringResource(R.string.status_hidden)
    }
    Surface(shape = RoundedCornerShape(chipCornerRadius), color = bg) {
        Text(label, modifier = Modifier.padding(horizontal = chipPaddingHorizontal, vertical = chipPaddingVertical), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold, color = textColor)
    }
}

@Composable
private fun PostDetailScreen(
    state: CommunityScreenUIState,
    viewModel: CommunityScreenViewModel,
    paddingValues: PaddingValues
) {
    val post = state.selectedPost ?: return

    Column(modifier = Modifier.fillMaxSize().padding(paddingValues).background(MaterialTheme.colorScheme.surface)) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = halfMargin, vertical = quarterMargin), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { viewModel.closePost() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
            }
            Text(stringResource(R.string.report_detail), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        }

        LazyColumn(modifier = Modifier.weight(1f), contentPadding = PaddingValues(bottom = basicMargin)) {
            item {
                Column(modifier = Modifier.padding(horizontal = basicMargin)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
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
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(basicMargin)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(quarterMargin),
                            modifier = Modifier.clip(RoundedCornerShape(smallCornerRadius)).clickable { viewModel.toggleUpvote(post) }.padding(6.dp)
                        ) {
                            Icon(if (state.upvotedPostIds.contains(post.id)) Icons.Filled.Star else Icons.Outlined.Star, null, Modifier.size(iconSizeMedium), tint = if (state.upvotedPostIds.contains(post.id)) Color(0xFFFFC107) else MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${post.upvoteCount}", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(quarterMargin)) {
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
                    Box(modifier = Modifier.padding(horizontal = basicMargin).padding(bottom = halfMargin)) {
                        HorizontalPager(state = pagerState) { page ->
                            AsyncImage(model = post.imageUrls[page], contentDescription = null, modifier = Modifier.fillMaxWidth().height(detailImageHeight).clip(RoundedCornerShape(mediumCornerRadius)), contentScale = ContentScale.Crop)
                        }
                        if (post.imageUrls.size > 1) {
                            Row(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = halfMargin), horizontalArrangement = Arrangement.spacedBy(quarterMargin)) {
                                repeat(post.imageUrls.size) { i ->
                                    Box(modifier = Modifier.size(if (pagerState.currentPage == i) dotSizeActive else dotSizeInactive).clip(CircleShape).background(if (pagerState.currentPage == i) Color.White else Color.White.copy(0.5f)))
                                }
                            }
                        }
                    }
                } else {
                    Box(modifier = Modifier.padding(horizontal = basicMargin).padding(bottom = halfMargin).fillMaxWidth().height(detailImageHeight).clip(RoundedCornerShape(mediumCornerRadius)).background(MaterialTheme.colorScheme.surfaceVariant)) {
                        Row(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = halfMargin), horizontalArrangement = Arrangement.spacedBy(quarterMargin)) {
                            repeat(3) { i ->
                                Box(modifier = Modifier.size(if (i == 0) dotSizeActive else dotSizeInactive).clip(CircleShape).background(if (i == 0) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurfaceVariant.copy(0.3f)))
                            }
                        }
                    }
                }

                if (state.commentsLoading) {
                    Box(Modifier.fillMaxWidth().padding(basicMargin), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.size(iconXLarge))
                    }
                }
            }

            items(state.comments, key = { it.id }) { comment ->
                CommentItem(comment = comment, onLike = { viewModel.toggleCommentLike(comment.id) }, onReply = { viewModel.startReply(comment) })
            }

            item { Spacer(Modifier.height(80.dp)) }
        }

        Surface(shadowElevation = halfMargin) {
            Column(modifier = Modifier.fillMaxWidth().navigationBarsPadding()) {
                if (state.replyingToComment != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth().background(Purple.copy(alpha = 0.08f)).padding(horizontal = basicMargin, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(R.string.replying_to, state.replyingToComment.authorName), style = MaterialTheme.typography.labelSmall, color = Purple, modifier = Modifier.weight(1f))
                        IconButton(onClick = { viewModel.cancelReply() }, modifier = Modifier.size(iconXLarge)) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, Modifier.size(iconSizeSmall), tint = Purple)
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = mediumCornerRadius, vertical = halfMargin), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = state.newCommentText,
                        onValueChange = { viewModel.onCommentTextChange(it) },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text(if (state.replyingToComment != null) stringResource(R.string.reply_to, state.replyingToComment.authorName) else stringResource(R.string.add_comment)) },
                        singleLine = true,
                        shape = RoundedCornerShape(largeCornerRadius)
                    )
                    Spacer(Modifier.width(halfMargin))
                    IconButton(
                        onClick = { viewModel.submitComment() },
                        enabled = !state.isPostingComment && state.newCommentText.isNotBlank(),
                        modifier = Modifier.clip(CircleShape).background(if (!state.isPostingComment && state.newCommentText.isNotBlank()) Purple else Purple.copy(alpha = 0.4f)).size(fabIconSize)
                    ) {
                        if (state.isPostingComment) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                        else Icon(Icons.Default.Add, stringResource(R.string.send), tint = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun CommentItem(
    comment: CommentUi,
    onLike: () -> Unit,
    onReply: () -> Unit
) {
    val isReply = comment.content.startsWith("@")
    Column(
        modifier = Modifier.fillMaxWidth().then(
            if (isReply) Modifier.padding(start = doubleMargin, end = basicMargin, top = quarterMargin, bottom = quarterMargin)
            else Modifier.padding(horizontal = basicMargin, vertical = quarterMargin)
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(Icons.Outlined.Person, null, Modifier.size(iconSizeSmall), tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(comment.authorName, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
            Text("·", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(comment.createdAt.take(10), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.height(quarterMargin))
        if (isReply) {
            val spaceIdx = comment.content.indexOf(' ')
            val mention = if (spaceIdx > 0) comment.content.substring(0, spaceIdx) else comment.content
            val rest = if (spaceIdx > 0) comment.content.substring(spaceIdx) else ""
            androidx.compose.foundation.text.BasicText(
                text = androidx.compose.ui.text.buildAnnotatedString {
                    pushStyle(androidx.compose.ui.text.SpanStyle(color = Purple, fontWeight = FontWeight.SemiBold))
                    append(mention)
                    pop()
                    pushStyle(androidx.compose.ui.text.SpanStyle(color = MaterialTheme.colorScheme.onSurface))
                    append(rest)
                    pop()
                },
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            Text(comment.content, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(basicMargin)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(quarterMargin),
                modifier = Modifier.clip(RoundedCornerShape(6.dp)).clickable { onLike() }.padding(horizontal = quarterMargin, vertical = 2.dp)
            ) {
                Icon(if (comment.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder, null, Modifier.size(iconSizeSmall), tint = if (comment.isLiked) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant)
                Text("${comment.localLikes}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(stringResource(R.string.reply), style = MaterialTheme.typography.labelSmall, color = Purple, modifier = Modifier.clip(RoundedCornerShape(6.dp)).clickable { onReply() }.padding(horizontal = quarterMargin, vertical = 2.dp))
        }
        HorizontalDivider(modifier = Modifier.padding(top = halfMargin), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreatePostBottomSheet(
    state: CommunityScreenUIState,
    viewModel: CommunityScreenViewModel
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    val postTypes = listOf(
        "IDEA" to stringResource(R.string.type_idea),
        "OTHER" to stringResource(R.string.type_other),
        "QUESTION" to stringResource(R.string.type_question),
        "POST" to stringResource(R.string.type_post)
    )
    val severities = listOf(
        stringResource(R.string.severity_low),
        stringResource(R.string.severity_medium),
        stringResource(R.string.severity_high)
    )
    val severityKeys = listOf("Lehká", "Střední", "Těžká")

    val photoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        uris.forEach { uri -> viewModel.onPhotoSelected(context, uri.toString()) }
    }

    ModalBottomSheet(onDismissRequest = { viewModel.hideCreateDialog() }, sheetState = sheetState) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = doubleMargin).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(basicMargin)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.hideCreateDialog() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = MaterialTheme.colorScheme.primary)
                }
                Text(stringResource(R.string.new_report), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            }

            if (state.createImageUris.isNotEmpty()) {
                val pagerState = rememberPagerState { state.createImageUris.size }
                Box {
                    HorizontalPager(state = pagerState) { page ->
                        Box {
                            AsyncImage(model = state.createImageUris[page], contentDescription = null, modifier = Modifier.fillMaxWidth().height(cardImageHeight).clip(RoundedCornerShape(mediumCornerRadius)), contentScale = ContentScale.Crop)
                            IconButton(onClick = { viewModel.removePhoto(state.createImageUris[page]) }, modifier = Modifier.align(Alignment.TopEnd).padding(quarterMargin).size(avatarSize).clip(CircleShape).background(Color.Black.copy(alpha = 0.5f))) {
                                Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(basicMargin))
                            }
                        }
                    }
                    if (state.createImageUris.size > 1) {
                        Row(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = halfMargin), horizontalArrangement = Arrangement.spacedBy(quarterMargin)) {
                            repeat(state.createImageUris.size) { i ->
                                Box(modifier = Modifier.size(if (pagerState.currentPage == i) dotSizeActive else dotSizeInactive).clip(CircleShape).background(if (pagerState.currentPage == i) Color.White else Color.White.copy(0.5f)))
                            }
                        }
                    }
                }
            }

            Surface(modifier = Modifier.fillMaxWidth().height(photoPickerHeight).clip(RoundedCornerShape(mediumCornerRadius)).clickable { photoPickerLauncher.launch("image/*") }, color = Purple.copy(alpha = 0.08f)) {
                Row(modifier = Modifier.fillMaxSize().padding(horizontal = basicMargin), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(mediumCornerRadius)) {
                    Icon(Icons.Default.Add, null, tint = Purple, modifier = Modifier.size(iconSizeLarge))
                    Column {
                        Text(stringResource(R.string.add_photo), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = Purple)
                        Text(stringResource(R.string.add_photo_description), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Text(stringResource(R.string.problem_specification), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            OutlinedTextField(value = state.createDescription, onValueChange = { viewModel.onCreateDescChange(it) }, placeholder = { Text(stringResource(R.string.problem_specification_placeholder)) }, modifier = Modifier.fillMaxWidth(), minLines = 4, shape = RoundedCornerShape(mediumCornerRadius))

            OutlinedTextField(value = state.createPlace, onValueChange = { viewModel.onCreatePlaceChange(it) }, placeholder = { Text(stringResource(R.string.location_placeholder)) }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(mediumCornerRadius), leadingIcon = { Icon(Icons.Outlined.Place, null, tint = Purple, modifier = Modifier.size(iconMediumSmall)) })

            Text(stringResource(R.string.report_type), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(halfMargin)) {
                items(postTypes) { (type, label) ->
                    FilterChip(selected = state.createType == type, onClick = { viewModel.onCreateTypeChange(type) }, label = { Text(label) }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Purple, selectedLabelColor = Color.White))
                }
            }

            Text(stringResource(R.string.report_severity), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            severities.forEachIndexed { index, label ->
                Row(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(smallCornerRadius)).clickable { viewModel.onCreateSeverityChange(severityKeys[index]) }.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = state.createSeverity == severityKeys[index], onClick = { viewModel.onCreateSeverityChange(severityKeys[index]) }, colors = RadioButtonDefaults.colors(selectedColor = Purple))
                    Text(label, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Button(onClick = { viewModel.submitPost() }, enabled = !state.isCreating && state.createDescription.isNotBlank(), modifier = Modifier.fillMaxWidth().height(fabHeight), colors = ButtonDefaults.buttonColors(containerColor = Purple), shape = RoundedCornerShape(14.dp)) {
                if (state.isCreating) CircularProgressIndicator(Modifier.size(iconMediumSmall), color = Color.White, strokeWidth = 2.dp)
                else Text(stringResource(R.string.send), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}