package com.example.projectobcane.screens.community

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projectobcane.R
import com.example.projectobcane.ui.theme.*

@Composable
fun CommentItem(
    comment: CommentUi,
    onReply: () -> Unit
) {
    val isReply = comment.content.startsWith("@")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isReply)
                    Modifier.padding(start = doubleMargin, end = basicMargin, top = quarterMargin, bottom = quarterMargin)
                else
                    Modifier.padding(horizontal = basicMargin, vertical = quarterMargin)
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
        Text(
            stringResource(R.string.reply),
            style = MaterialTheme.typography.labelSmall,
            color = Purple,
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .clickable { onReply() }
                .padding(horizontal = quarterMargin, vertical = 2.dp)
        )
        HorizontalDivider(modifier = Modifier.padding(top = halfMargin), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
    }
}