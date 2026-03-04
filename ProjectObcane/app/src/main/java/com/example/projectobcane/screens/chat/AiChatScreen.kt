package com.example.projectobcane.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.luminance
import com.example.projectobcane.ui.elements.BaseScreen

private val Purple = Color(0xFF7A3CFF)

@Composable
fun AiChatScreen(
    paddingValues: PaddingValues,
    viewModel: AiChatViewModel = hiltViewModel()
) {
    val ui by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    // app theme (ne systém)
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f

    // Background: dark = jemný violet glow, light = white + soft violet
    val bg = if (!isDark) {
        Brush.verticalGradient(
            listOf(Color.White, Color.White, Color(0xFFEFE7FF).copy(alpha = 0.45f))
        )
    } else {
        Brush.verticalGradient(
            listOf(Color(0xFF0D0B12), Color(0xFF141021), Color(0xFF0B0910))
        )
    }

    LaunchedEffect(ui.items.size, ui.isSending) {
        if (ui.items.isNotEmpty()) listState.animateScrollToItem(ui.items.size - 1)
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
            IconButton(onClick = { viewModel.resetConversation() }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset",
                    tint = Purple
                )
            }
        }

        // ===== CONTENT =====
        if (ui.items.isEmpty() && !ui.isSending) {
            // empty state (center hello)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 22.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HighlightedTitle(
                    text = "Ahoj, Xxxxx",
                    modifier = Modifier.weight(1f)
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
                        ChatRole.User -> UserBubble(m.text, isDark)
                        ChatRole.Assistant -> AiCard(m.text, isDark)
                    }
                }

                if (ui.isSending) {
                    item { ThinkingLabel(isDark) }
                    item { TypingDotsBubble(isDark) }
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
            isDark = isDark
        )
    }
}

@Composable
private fun UserBubble(text: String, isDark: Boolean) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Surface(
            color = Purple,
            shape = RoundedCornerShape(20.dp),
            shadowElevation = if (isDark) 0.dp else 0.dp
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                color = Color.White
            )
        }
    }
}

/**
 * AI karta:
 * - light: bílá karta
 * - dark: tmavá karta s "white highlight" gradientem (stejné jako News cards)
 */
@Composable
private fun AiCard(text: String, isDark: Boolean) {
    val shape = RoundedCornerShape(20.dp)

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        if (!isDark) {
            Surface(
                color = Color.White,
                shape = shape,
                shadowElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 320.dp)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("AI", color = Purple, fontWeight = FontWeight.SemiBold)
                    Text(text = text, color = Color(0xFF2D2D2D))
                }
            }
        } else {
            // dark highlight card
            HighlightCard(
                modifier = Modifier.widthIn(max = 320.dp),
                shape = shape
            ) {
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
private fun TypingDotsBubble(isDark: Boolean) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        if (!isDark) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 4.dp
            ) {
                Text(
                    text = "•••",
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    color = Purple
                )
            }
        } else {
            HighlightCard(
                modifier = Modifier,
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
}

@Composable
private fun BottomInputBar(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    sending: Boolean,
    onSend: () -> Unit,
    isDark: Boolean
) {
    val canSend = value.isNotBlank() && !sending
    val shape = RoundedCornerShape(999.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        if (!isDark) {
            Surface(
                color = Color.White,
                shape = shape,
                shadowElevation = 10.dp
            ) {
                InputRow(
                    value = value,
                    onValueChange = onValueChange,
                    placeholderColor = Purple,
                    textColor = Color(0xFF2D2D2D),
                    onSend = onSend,
                    canSend = canSend
                )
            }
        } else {
            // dark input se stejným highlightem jako News
            HighlightCard(
                modifier = Modifier.fillMaxWidth(),
                shape = shape
            ) {
                InputRow(
                    value = value,
                    onValueChange = onValueChange,
                    placeholderColor = Purple.copy(alpha = 0.75f),
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

        IconButton(onClick = { if (canSend) onSend() }) {
            if (value.isBlank()) {
                Icon(Icons.Default.Mic, null, tint = Purple)
            } else {
                Icon(Icons.Default.Send, null, tint = Purple)
            }
        }
    }
}

/**
 * Card style jako News:
 * tmavý podklad + jemný světlejší gradient highlight (shora zleva)
 */
@Composable
private fun HighlightCard(
    modifier: Modifier,
    shape: RoundedCornerShape,
    content: @Composable ColumnScope.() -> Unit
) {
    val base = Color(0xFF1A1526)

    Box(
        modifier = modifier
            .clip(shape)
            .background(base)
    ) {
        // highlight overlay
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

        Column(
            modifier = Modifier.padding(0.dp),
            content = content
        )
    }
}

@Composable
private fun HighlightedTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f

    val glowColor = if (!isDark) {
        Color(0xFF8E77F5).copy(alpha = 0.45f)
    } else {
        Color.White.copy(alpha = 0.18f)   // ↓ jemnější glow
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = Purple
            ),
            modifier = Modifier
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                glowColor,
                                Color.Transparent
                            ),
                            radius = size.width * 1.7f
                        ),
                        radius = size.width * 1.7f
                    )
                }
        )
    }
}
