package com.example.projectobcane.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.graphics.luminance

private val Purple = Color(0xFF7A3CFF)

@Composable
fun AiChatScreen(
    paddingValues: PaddingValues,
    viewModel: AiChatViewModel = hiltViewModel()
) {
    val ui by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f

    // ---------- DARK / LIGHT COLORS ----------
    val backgroundTop = if (isDark) Color(0xFF121018) else Color.White
    val backgroundBottom = if (isDark) Color(0xFF1B1626) else Color(0xFFEFE7FF)

    val aiCardColor = if (isDark) Color(0xFF1E1A29) else Color.White
    val aiTextColor = if (isDark) Color(0xFFEAE6F8) else Color(0xFF2D2D2D)

    val inputBackground = if (isDark) Color(0xFF1E1A29) else Color.White
    val inputTextColor = if (isDark) Color(0xFFEAE6F8) else Color(0xFF2D2D2D)
    val placeholderColor = if (isDark) Purple.copy(alpha = 0.7f) else Purple

    val bg = Brush.verticalGradient(
        listOf(backgroundTop, backgroundTop, backgroundBottom)
    )

    LaunchedEffect(ui.items.size, ui.isSending) {
        if (ui.items.isNotEmpty()) {
            listState.animateScrollToItem(ui.items.size - 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
    ) {

        // ===== HEADER =====
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ahoj, Eliška",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Purple
                ),
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = { viewModel.resetConversation() }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset",
                    tint = Purple
                )
            }
        }

        // ===== CHAT CONTENT =====
        if (ui.items.isEmpty() && !ui.isSending) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 22.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ahoj, Eliška",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Purple
                    ),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(top = 64.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(ui.items, key = { it.id }) { m ->
                    when (m.role) {
                        ChatRole.User -> UserBubble(m.text)
                        ChatRole.Assistant -> AiCard(m.text, aiCardColor, aiTextColor)
                    }
                }

                if (ui.isSending) {
                    item { ThinkingLabel(isDark) }
                    item { TypingDotsBubble(aiCardColor) }
                }
            }
        }

        // ===== INPUT =====
        BottomInputBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            value = ui.input,
            onValueChange = viewModel::onInputChange,
            sending = ui.isSending,
            onSend = viewModel::send,
            backgroundColor = inputBackground,
            textColor = inputTextColor,
            placeholderColor = placeholderColor
        )
    }
}

@Composable
private fun UserBubble(text: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Surface(
            color = Purple,
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                color = Color.White
            )
        }
    }
}

@Composable
private fun AiCard(text: String, background: Color, textColor: Color) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        Surface(
            color = background,
            shape = RoundedCornerShape(20.dp),
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 320.dp)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "AI",
                    color = Purple,
                    fontWeight = FontWeight.SemiBold
                )
                Text(text = text, color = textColor)
            }
        }
    }
}

@Composable
private fun ThinkingLabel(isDark: Boolean) {
    Text(
        text = "Přemýšlím nad vaší otázkou...",
        color = if (isDark) Color(0xFF9C94B6) else Color.Gray,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
private fun TypingDotsBubble(background: Color) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        Surface(
            color = background,
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "•••",
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                color = Purple
            )
        }
    }
}

@Composable
private fun BottomInputBar(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    sending: Boolean,
    onSend: () -> Unit,
    backgroundColor: Color,
    textColor: Color,
    placeholderColor: Color
) {
    val canSend = value.isNotBlank() && !sending

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Surface(
            color = backgroundColor,
            shape = RoundedCornerShape(999.dp),
            shadowElevation = 8.dp
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

                IconButton(onClick = { if (canSend) onSend() }) {
                    if (value.isBlank()) {
                        Icon(Icons.Default.Mic, null, tint = Purple)
                    } else {
                        Icon(Icons.Default.Send, null, tint = Purple)
                    }
                }
            }
        }
    }
}