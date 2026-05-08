package com.example.projectobcane.screens.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projectobcane.models.NewsItemUi
import com.example.projectobcane.ui.elements.GlideImage
import com.example.projectobcane.ui.theme.basicMargin
import com.example.projectobcane.ui.theme.chipCornerRadius
import com.example.projectobcane.ui.theme.detailBottomSpacer
import com.example.projectobcane.ui.theme.halfMargin
import com.example.projectobcane.ui.theme.mediumCornerRadius
import com.example.projectobcane.ui.theme.newsImageHeight

@Composable
fun NewsDetailSheet(
    news: NewsItemUi
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .navigationBarsPadding()
    ) {

        item {

            Spacer(modifier = Modifier.height(halfMargin))

            GlideImage(
                url = news.imageUrl ?: "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(newsImageHeight)
            )

            Column(
                modifier = Modifier.padding(halfMargin)
            ) {

                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(chipCornerRadius)
                        )
                        .padding(
                            horizontal = basicMargin,
                            vertical = halfMargin
                        )
                ) {

                    Text(
                        text = news.category,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(basicMargin))

                Text(
                    text = news.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(halfMargin))

                Text(
                    text = news.faculty,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(basicMargin))

                Text(
                    text = news.text
                        .replace("<br>", "\n"),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(detailBottomSpacer))
            }
        }
    }
}