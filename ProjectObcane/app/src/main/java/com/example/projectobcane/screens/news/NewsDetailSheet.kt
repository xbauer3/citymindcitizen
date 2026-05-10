package com.example.projectobcane.screens.news

import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.projectobcane.models.NewsItemUi
import com.example.projectobcane.ui.theme.*
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun NewsDetailSheet(news: NewsItemUi) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .navigationBarsPadding()
    ) {
        item {
            Spacer(modifier = Modifier.height(halfMargin))

            AsyncImage(
                model = news.imageUrl ?: "",
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(newsImageHeight),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(basicMargin)) {

                Spacer(modifier = Modifier.height(halfMargin))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (news.category.isNotEmpty()) {
                        MetaChip(
                            label = news.category,
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    if (news.faculty.isNotEmpty()) {
                        MetaChip(
                            icon = {
                                Icon(
                                    Icons.Outlined.School,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp)
                                )
                            },
                            label = news.faculty,
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(basicMargin))

                Text(
                    text = news.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(halfMargin))

                val formattedStart = formatNewsDate(news.startDate)
                val formattedEnd = formatNewsDate(news.endDate)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (formattedStart != null) {
                        MetaChip(
                            icon = {
                                Icon(
                                    Icons.Outlined.CalendarMonth,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp)
                                )
                            },
                            label = if (formattedEnd != null && formattedEnd != formattedStart)
                                "$formattedStart – $formattedEnd"
                            else
                                formattedStart,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(basicMargin))

                OutlinedButton(
                    onClick = {
                        openCalendar(
                            context = context,
                            title = news.title,
                            description = news.text
                                .replace("<br>", " ")
                                .replace("<br/>", " "),
                            startDateStr = news.startDate,
                            endDateStr = news.endDate
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(mediumCornerRadius),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 1.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Přidat do kalendáře",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(modifier = Modifier.height(basicMargin))

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                Spacer(modifier = Modifier.height(basicMargin))

                Text(
                    text = news.text
                        .replace("<br>", "\n")
                        .replace("<br/>", "\n"),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(detailBottomSpacer))
            }
        }
    }
}

@Composable
private fun MetaChip(
    label: String,
    containerColor: androidx.compose.ui.graphics.Color,
    contentColor: androidx.compose.ui.graphics.Color,
    icon: (@Composable () -> Unit)? = null
) {
    Surface(
        shape = RoundedCornerShape(chipCornerRadius),
        color = containerColor
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                icon?.invoke()
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = contentColor,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

private fun formatNewsDate(raw: String?): String? {
    if (raw == null) return null
    return try {
        val date = LocalDate.parse(raw.substring(0, 10))
        date.format(DateTimeFormatter.ofPattern("d. M. yyyy", Locale("cs")))
    } catch (e: Exception) {
        raw
    }
}

private fun openCalendar(
    context: android.content.Context,
    title: String,
    description: String,
    startDateStr: String?,
    endDateStr: String?
) {
    val zoneId = ZoneId.systemDefault()

    val startMillis: Long = try {
        LocalDate.parse(startDateStr!!.substring(0, 10))
            .atStartOfDay(zoneId).toInstant().toEpochMilli()
    } catch (e: Exception) {
        System.currentTimeMillis()
    }

    val endMillis: Long = try {
        LocalDate.parse(endDateStr!!.substring(0, 10))
            .atTime(23, 59).atZone(zoneId).toInstant().toEpochMilli()
    } catch (e: Exception) {
        startMillis + 3_600_000L
    }

    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.Events.TITLE, title)
        putExtra(CalendarContract.Events.DESCRIPTION, description)
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
        putExtra(CalendarContract.Events.ALL_DAY, true)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        val fallback = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, title)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try { context.startActivity(fallback) } catch (_: Exception) {}
    }
}