package com.example.projectobcane.screens.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.projectobcane.R
import com.example.projectobcane.database.reports.Report
import com.example.projectobcane.navigation.INavigationRouter
    import com.example.projectobcane.ui.elements.BaseScreen
import com.example.projectobcane.ui.theme.GreenDark
import com.example.projectobcane.ui.theme.GreenLight
import com.example.projectobcane.ui.theme.RedDark
import com.example.projectobcane.ui.theme.RedLight
import com.example.projectobcane.ui.theme.YellowDark
import com.example.projectobcane.ui.theme.YellowLight
import com.example.projectobcane.ui.theme.basicMargin
import com.example.projectobcane.ui.theme.halfMargin
import java.time.LocalDate
import java.time.ZoneId


@Composable
fun ReportsScreen(
    bottomNav: INavigationRouter,
    rootNav: INavigationRouter,
    paddingValues: PaddingValues
) {

    val viewModel = hiltViewModel<ReportsScreenViewModel>()
    val state = viewModel.reportScreenUIState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadReports()
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        state.value.reports.forEach {
            item {
                Box(modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .fillMaxWidth()
                    .padding(bottom = 2.dp)

                ) {

                    ReportItem(
                        report = it
                    ) {
                        rootNav.navigateToReportDetail(it.id)
                    }
                }

            }
        }

    }


}



@Composable
fun ReportItem(report: Report, onClick: () -> Unit) {

    val (darkColor, lightColor) = when (report.status.lowercase()) {
        "complete", "done" -> GreenDark to GreenLight
        "inprogress", "in progress" -> YellowDark to YellowLight
        "new" -> RedDark to RedLight
        else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = halfMargin)
            .clickable { onClick() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(darkColor)
                    .padding(basicMargin)
            ) {
                Text(
                    text = report.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(lightColor)
                    .padding(basicMargin)
            ) {
                Column {
                    Text(report.description, maxLines = 1)
                    Text(stringResource(R.string.status_w_value, report.status))
                }
            }
        }
    }
}
