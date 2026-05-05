package com.example.projectobcane.screens.chat

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

private val Purple = Color(0xFF7A3CFF)

@Composable
fun AiChatScreen(
    paddingValues: PaddingValues,
    viewModel: AiChatViewModel = hiltViewModel()
) {
    val ui by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f

    val totalItemCount = ui.items.size + if (ui.isSending) 2 else 0
    LaunchedEffect(totalItemCount) {
        if (totalItemCount > 0) listState.animateScrollToItem(totalItemCount - 1)
    }

    val bg = if (!isDark) {
        Brush.verticalGradient(
            listOf(Color.White, Color.White, Color(0xFFEFE7FF).copy(alpha = 0.45f))
        )
    } else {
        Brush.verticalGradient(
            listOf(Color(0xFF0D0B12), Color(0xFF141021), Color(0xFF0B0910))
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
    ) {
        // ── Thin header row with reset button ────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.resetConversation() }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset conversation",
                    tint = Purple,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // ── Content: empty state or chat list ────────────────────────────
        Box(modifier = Modifier.weight(1f)) {
            if (ui.items.isEmpty() && !ui.isSending) {
                HighlightedTitle(
                    text = "Ahoj, Jak vám mohu pomoci",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(ui.items, key = { it.id }) { m ->
                        when (m.role) {
                            ChatRole.User -> UserBubble(m.displayText, isDark)
                            ChatRole.Assistant -> AiCard(m.displayText, isDark)
                        }
                    }
                    if (ui.isSending) {
                        item(key = "thinking_label") { ThinkingLabel(isDark) }
                        item(key = "thinking_dots") { AnimatedDotsBubble(isDark) }
                    }
                }
            }
        }

        // ── Input bar pinned at bottom ────────────────────────────────────
        BottomInputBar(
            value = ui.input,
            onValueChange = viewModel::onInputChange,
            sending = ui.isSending,
            onSend = viewModel::send,
            isDark = isDark
        )
    }
}

// ─── Bubbles ─────────────────────────────────────────────────────────────────

@Composable
private fun UserBubble(text: String, isDark: Boolean) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Surface(color = Purple, shape = RoundedCornerShape(20.dp)) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                color = Color.White
            )
        }
    }
}

@Composable
private fun AiCard(text: String, isDark: Boolean) {
    val shape = RoundedCornerShape(20.dp)
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        if (!isDark) {
            Surface(color = Color.White, shape = shape, shadowElevation = 6.dp) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 300.dp)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("AI", color = Purple, fontWeight = FontWeight.SemiBold)
                    Text(text = text, color = Color(0xFF2D2D2D))
                }
            }
        } else {
            HighlightCard(modifier = Modifier.widthIn(max = 300.dp), shape = shape) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("AI", color = Purple, fontWeight = FontWeight.SemiBold)
                    Text(text = text, color = Color(0xFFF1ECFF))
                }
            }
        }
    }
}

@Composable
private fun ThinkingLabel(isDark: Boolean) {
    Text(
        text = "Přemýšlím nad vaší otázkou...",
        color = if (isDark) Color(0xFFB7AECF) else Color.Gray,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
private fun AnimatedDotsBubble(isDark: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "dots")

    @Composable
    fun dotAlpha(delayMs: Int): Float {
        val alpha by infiniteTransition.animateFloat(
            initialValue = 0.2f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 900
                    0.2f at 0
                    1f at 300
                    0.2f at 600
                    0.2f at 900
                },
                repeatMode = RepeatMode.Restart,
                initialStartOffset = StartOffset(delayMs)
            ),
            label = "dot_$delayMs"
        )
        return alpha
    }

    val alpha1 = dotAlpha(0)
    val alpha2 = dotAlpha(200)
    val alpha3 = dotAlpha(400)

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        if (!isDark) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 4.dp
            ) {
                DotsRow(alpha1, alpha2, alpha3)
            }
        } else {
            HighlightCard(modifier = Modifier, shape = RoundedCornerShape(16.dp)) {
                DotsRow(alpha1, alpha2, alpha3)
            }
        }
    }
}

@Composable
private fun DotsRow(alpha1: Float, alpha2: Float, alpha3: Float) {
    Row(
        modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(alpha1, alpha2, alpha3).forEach { alpha ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Purple.copy(alpha = alpha))
            )
        }
    }
}

// ─── Input bar ───────────────────────────────────────────────────────────────

@Composable
private fun BottomInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    sending: Boolean,
    onSend: () -> Unit,
    isDark: Boolean
) {
    val canSend = value.isNotBlank() && !sending
    val shape = RoundedCornerShape(999.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        if (!isDark) {
            Surface(color = Color.White, shape = shape, shadowElevation = 10.dp) {
                InputRow(
                    value = value,
                    onValueChange = onValueChange,
                    placeholderColor = Purple.copy(alpha = 0.5f),
                    textColor = Color(0xFF2D2D2D),
                    onSend = onSend,
                    canSend = canSend
                )
            }
        } else {
            HighlightCard(modifier = Modifier.fillMaxWidth(), shape = shape) {
                InputRow(
                    value = value,
                    onValueChange = onValueChange,
                    placeholderColor = Purple.copy(alpha = 0.5f),
                    textColor = Color(0xFFF1ECFF),
                    onSend = onSend,
                    canSend = canSend
                )
            }
        }
    }
}

@Composable
private fun InputRow(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderColor: Color,
    textColor: Color,
    onSend: () -> Unit,
    canSend: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Zeptej se mě...", color = placeholderColor) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Purple,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor
            ),
            maxLines = 4
        )

        // Send icon: purple when can send, faded purple when disabled — no background ever
        IconButton(
            onClick = { if (canSend) onSend() },
            enabled = canSend
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
                tint = if (canSend) Purple else Purple.copy(alpha = 0.3f),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

// ─── Shared components ────────────────────────────────────────────────────────

@Composable
private fun HighlightCard(
    modifier: Modifier,
    shape: RoundedCornerShape,
    content: @Composable ColumnScope.() -> Unit
) {
    val base = Color(0xFF1A1526)
    Box(modifier = modifier.clip(shape).background(base)) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.10f),
                            Color.White.copy(alpha = 0.04f),
                            Color.Transparent
                        )
                    )
                )
        )
        Column(modifier = Modifier.padding(0.dp), content = content)
    }
}

@Composable
private fun HighlightedTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val glowColor = if (!isDark) Color(0xFF8E77F5).copy(alpha = 0.45f)
    else Color.White.copy(alpha = 0.18f)

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(360.dp)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(glowColor, Color.Transparent)
                        ),
                        radius = size.minDimension / 2
                    )
                }
        )
        Text(
            text = text,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = Purple
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}