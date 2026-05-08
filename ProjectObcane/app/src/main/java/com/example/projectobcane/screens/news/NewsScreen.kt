package com.example.projectobcane.screens.news

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.projectobcane.models.NewsItemUi

import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.ui.elements.GlideImage
import com.example.projectobcane.ui.theme.basicMargin
import com.example.projectobcane.ui.theme.halfMargin
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    bottomNav: INavigationRouter,
    rootNav: INavigationRouter,
    paddingValues: PaddingValues
) {

    val viewModel = hiltViewModel<NewsScreenViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    var selectedNews by remember {
        mutableStateOf<NewsItemUi?>(null)
    }

    /*
    //dont need this if i do loadnews in init in viewmodel
    LaunchedEffect(Unit) {
        viewModel.loadNews()
    }*/


    when {

        state.value.loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.value.error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = state.value.error ?: "Unknown error")
            }
        }

        else -> {

            val groupedNews = state.value.news.groupBy { news ->

                val newsDate = try {

                    news.startDate
                        ?.substring(0, 10)
                        ?.let {
                            java.time.LocalDate.parse(it)
                        }

                } catch (e: Exception) {
                    null
                }

                val today = LocalDate.now()

                when {

                    newsDate == null -> "Older"

                    newsDate.isEqual(today) -> "Today"

                    newsDate.isEqual(today.minusDays(1)) -> "Yesterday"

                    newsDate.isAfter(today) &&
                            newsDate.isBefore(today.plusDays(7)) -> "Upcoming Week"

                    newsDate.isAfter(today.minusDays(7)) -> "This Week"

                    else -> "Older"
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                val sectionOrder = listOf(
                    "Today",
                    "Yesterday",
                    "Upcoming Week",
                    "This Week",
                    "Older"
                )

                sectionOrder.forEach { section ->

                    val itemsInSection = groupedNews[section]

                    if (!itemsInSection.isNullOrEmpty()) {

                        item {

                            Text(
                                text = section,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 12.dp
                                )
                            )
                        }

                        items(itemsInSection) { news ->

                            NewsItem(
                                news = news
                            ) {
                                //rootNav.navigateToEventDetail(news.id)
                                selectedNews = news
                            }
                        }
                    }
                }
            }
            selectedNews?.let { news ->

                ModalBottomSheet(
                    onDismissRequest = {
                        selectedNews = null
                    },
                    sheetState = rememberModalBottomSheetState(
                        skipPartiallyExpanded = false
                    )
                ) {

                    NewsDetailSheet(
                        news = news
                    )
                }
            }


        }
    }


}







@Composable
fun NewsItem(
    news: NewsItemUi,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Box {

            GlideImage(
                url = news.imageUrl ?: "https://picsum.photos/400",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {

                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.secondary,
                            RoundedCornerShape(50)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = news.category.ifEmpty { "Novinka" },
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = news.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = news.startDate ?: "Completed",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodySmall
                    )

                }
            }
        }
    }
}

fun formatEventDate(timestamp: Long): String {
    if (timestamp == 0L) return "--.--.----"
    val formatter = DateTimeFormatter.ofPattern("d. M.")
    return Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(formatter)
}

fun formatEventTime(timestamp: Long): String {
    if (timestamp == 0L) return "--:--"
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
        .format(formatter)
}
