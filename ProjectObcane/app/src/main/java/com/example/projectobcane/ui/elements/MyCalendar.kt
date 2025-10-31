package com.example.projectobcane.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectobcane.database.Event
import java.time.*
import java.time.format.DateTimeFormatter

import androidx.core.graphics.toColorInt

import com.example.projectobcane.database.EventWithColorCategory
import com.example.projectobcane.navigation.INavigationRouter

import com.example.projectobcane.ui.theme.basicMargin
import com.example.projectobcane.ui.theme.halfMargin
import com.example.projectobcane.ui.theme.heighOfARow
import kotlin.math.ceil
import com.example.projectobcane.R


@Composable
fun EventRow(
    event: EventWithColorCategory,
    onRowClick: () -> Unit,
){



    val backgroundColor = parseHexColorOrWhite(event.colorCategory?.colorHex)



    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable { onRowClick() }
            .padding(
                start = basicMargin,
                end = basicMargin,
                top = halfMargin,
                bottom = halfMargin
            )
            .height(heighOfARow)
        ,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Text(text = event.event.eventName,
            color = Color.Black, // Ensure contrast or customize
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )


    }

}


@Composable
fun CalendarHeader(
    currentMonth: LocalDate,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(basicMargin),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.previous_month))
        }
        Text(
            text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        IconButton(onClick = onNextMonth) {
            Icon(Icons.Default.ArrowForward, contentDescription = stringResource(R.string.next_month))
        }
    }
}

@Composable
fun WeekdayLabels() {
    Row(Modifier
        .fillMaxWidth()
        .padding(horizontal = basicMargin)) {

        listOf(stringResource(R.string.sun),
            stringResource(R.string.mon), stringResource(R.string.tue),
            stringResource(R.string.wed), stringResource(R.string.thu),
            stringResource(R.string.fri), stringResource(R.string.sat)
        ).forEach {
            Text(
                text = it,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CalendarGrid(
    days: List<LocalDate?>,
    selectedDate: LocalDate,
    events: List<EventWithColorCategory>,
    onDateSelected: (LocalDate) -> Unit
) {


    val rows = ceil(days.size / 7.0).toInt()
    val gridHeight = heighOfARow * rows

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier
            .fillMaxWidth()
            .height(gridHeight),
        userScrollEnabled = false
    ) {
        items(days.size) { index ->
            val date = days[index]
            val eventsForDay = events.filter {
                val eventDate = it.event.startDate?.let { ms ->
                    Instant.ofEpochMilli(ms).atZone(ZoneId.systemDefault()).toLocalDate()
                }
                eventDate != null && eventDate == date
            }
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(3.dp)
                    .clip(CircleShape)
                    .background(
                        if (date == selectedDate) MaterialTheme.colorScheme.primary else Color.Transparent
                    )
                    .clickable(enabled = date != null) {
                        date?.let { onDateSelected(it) }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = date?.dayOfMonth?.toString() ?: "",
                    color = if (date == selectedDate) Color.White else Color.Unspecified
                )
                val eventsForDay = events.filter {
                    val eventDate = it.event.startDate?.let { ms ->
                        Instant.ofEpochMilli(ms).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    eventDate == date
                }
                if (date != null && eventsForDay.isNotEmpty()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(25.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            eventsForDay.take(3).forEach { event ->
                                val color = parseHexColorOrWhite(event.colorCategory?.colorHex)
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .border(width = 0.5.dp, color = Color.Black.copy(alpha = 0.2f), shape = CircleShape)
                                        .background(color, CircleShape)
                                )
                            }
                        }
                        /*
                        if (eventsForDay.size > 3) {
                            Text(
                                text = "+${eventsForDay.size - 3}",
                                fontSize = 10.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 1.dp)
                            )
                        }*/
                    }
                }

            }
        }
    }
}







fun parseHexColorOrWhite(hex: String?): Color {
    return try {
        val cleanedHex = hex?.trim()?.removePrefix("#") ?: return Color.White
        val colorLong = cleanedHex.toLong(16)

        when (cleanedHex.length) {
            6 -> Color(colorLong or 0x00000000FF000000) // Add full alpha if missing
            8 -> Color(colorLong)
            else -> Color.White
        }
    } catch (e: Exception) {
        Color.White
    }
}
