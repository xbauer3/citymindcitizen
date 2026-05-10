package com.example.projectobcane.screens.news

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.projectobcane.models.NewsItemUi
import com.example.projectobcane.navigation.INavigationRouter

import com.example.projectobcane.ui.theme.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowInsetsControllerCompat
import coil.compose.AsyncImage
import com.example.projectobcane.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    bottomNav: INavigationRouter,
    rootNav: INavigationRouter,
    paddingValues: PaddingValues
) {
    val viewModel = hiltViewModel<NewsScreenViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    var selectedNews by remember { mutableStateOf<NewsItemUi?>(null) }

    LaunchedEffect(Unit) { viewModel.reloadIfLanguageChanged() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            state.value.loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            state.value.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.value.error ?: "Unknown error")
                }
            }
            else -> {
                val groupedNews = state.value.news.groupBy { news ->
                    val newsDate = try {
                        news.startDate?.substring(0, 10)?.let { LocalDate.parse(it) }
                    } catch (e: Exception) { null }
                    val today = LocalDate.now()
                    when {
                        newsDate == null -> "Older"
                        newsDate.isEqual(today) -> "Today"
                        newsDate.isEqual(today.minusDays(1)) -> "Yesterday"
                        newsDate.isAfter(today) && newsDate.isBefore(today.plusDays(7)) -> "Upcoming Week"
                        newsDate.isAfter(today.minusDays(7)) -> "This Week"
                        else -> "Older"
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(paddingValues)
                ) {
                    val sectionOrder = listOf("Today", "Yesterday", "Upcoming Week", "This Week", "Older")
                    sectionOrder.forEach { section ->
                        val itemsInSection = groupedNews[section]
                        if (!itemsInSection.isNullOrEmpty()) {
                            item {
                                val translatedSection = when (section) {
                                    "Today" -> stringResource(R.string.today)
                                    "Yesterday" -> stringResource(R.string.yesterday)
                                    "Upcoming Week" -> stringResource(R.string.upcoming_week)
                                    "This Week" -> stringResource(R.string.this_week)
                                    "Older" -> stringResource(R.string.older)
                                    else -> section
                                }
                                Text(
                                    text = translatedSection,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = basicMargin, vertical = mediumCornerRadius)
                                )
                            }
                            items(itemsInSection) { news ->
                                NewsItem(news = news) { selectedNews = news }
                            }
                        }
                    }
                }

                selectedNews?.let { news ->
                    val view = LocalView.current
                    DisposableEffect(Unit) {
                        val window = (view.context as android.app.Activity).window
                        val controller = WindowInsetsControllerCompat(window, window.decorView)
                        controller.isAppearanceLightStatusBars = false
                        onDispose {
                            controller.isAppearanceLightStatusBars = false
                        }
                    }

                    ModalBottomSheet(
                        onDismissRequest = { selectedNews = null },
                        contentWindowInsets = { WindowInsets.statusBars },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                        containerColor = MaterialTheme.colorScheme.surface,
                    ) {
                        NewsDetailSheet(news = news)
                    }
                }
            }
        }
    }
}

@Composable
fun NewsItem(news: NewsItemUi, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = mediumCornerRadius, vertical = halfMargin)
            .clickable { onClick() },
        shape = RoundedCornerShape(basicMargin),
        elevation = CardDefaults.cardElevation(defaultElevation = quarterMargin),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box {
            AsyncImage(
                model = news.imageUrl ?: "https://picsum.photos/400",
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(newsCardHeight),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(newsCardHeight)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                            )
                        )
                    )
            )
            Column(modifier = Modifier.align(Alignment.BottomStart).padding(basicMargin)) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(chipCornerRadius))
                        .padding(horizontal = chipPaddingHorizontal, vertical = chipPaddingVertical)
                ) {
                    Text(
                        text = news.category.ifEmpty { "Novinka" },
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Spacer(modifier = Modifier.height(halfMargin))
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = ColorWhite,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(halfMargin))
                Text(
                    text = news.startDate ?: "Completed",
                    color = ColorWhite,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}