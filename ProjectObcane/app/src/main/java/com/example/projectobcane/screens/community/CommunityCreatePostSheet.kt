package com.example.projectobcane.screens.community

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.projectobcane.R
import com.example.projectobcane.ui.theme.avatarSize
import com.example.projectobcane.ui.theme.basicMargin
import com.example.projectobcane.ui.theme.cardImageHeight
import com.example.projectobcane.ui.theme.dotSizeActive
import com.example.projectobcane.ui.theme.dotSizeInactive
import com.example.projectobcane.ui.theme.doubleMargin
import com.example.projectobcane.ui.theme.fabHeight
import com.example.projectobcane.ui.theme.halfMargin
import com.example.projectobcane.ui.theme.iconMediumSmall
import com.example.projectobcane.ui.theme.iconSizeLarge
import com.example.projectobcane.ui.theme.mediumCornerRadius
import com.example.projectobcane.ui.theme.photoPickerHeight
import com.example.projectobcane.ui.theme.quarterMargin
import com.example.projectobcane.ui.theme.smallCornerRadius
import com.example.projectobcane.ui.theme.Purple



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostBottomSheet(
    state: CommunityScreenUIState,
    viewModel: CommunityScreenViewModel
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    val postTypes = listOf(
        "IDEA" to stringResource(R.string.type_idea),
        "OTHER" to stringResource(R.string.type_other),
        "QUESTION" to stringResource(R.string.type_question),
        "POST" to stringResource(R.string.type_post)
    )
    val severities = listOf(
        stringResource(R.string.severity_low),
        stringResource(R.string.severity_medium),
        stringResource(R.string.severity_high)
    )
    val severityKeys = listOf("Lehká", "Střední", "Těžká")

    val photoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        uris.forEach { uri -> viewModel.onPhotoSelected(context, uri.toString()) }
    }

    ModalBottomSheet(
        onDismissRequest = { viewModel.hideCreateDialog() },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 20.dp)
                .padding(bottom = doubleMargin)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(basicMargin)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.hideCreateDialog() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = MaterialTheme.colorScheme.primary)
                }
                Text(
                    stringResource(R.string.new_report),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            if (state.createImageUris.isNotEmpty()) {
                val pagerState = rememberPagerState { state.createImageUris.size }
                Box {
                    HorizontalPager(state = pagerState) { page ->
                        Box {
                            AsyncImage(
                                model = state.createImageUris[page],
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(cardImageHeight)
                                    .clip(RoundedCornerShape(mediumCornerRadius)),
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = { viewModel.removePhoto(state.createImageUris[page]) },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(quarterMargin)
                                    .size(avatarSize)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.5f))
                            ) {
                                Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(basicMargin))
                            }
                        }
                    }
                    if (state.createImageUris.size > 1) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = halfMargin),
                            horizontalArrangement = Arrangement.spacedBy(quarterMargin)
                        ) {
                            repeat(state.createImageUris.size) { i ->
                                Box(
                                    modifier = Modifier
                                        .size(if (pagerState.currentPage == i) dotSizeActive else dotSizeInactive)
                                        .clip(CircleShape)
                                        .background(if (pagerState.currentPage == i) Color.White else Color.White.copy(0.5f))
                                )
                            }
                        }
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(photoPickerHeight)
                    .clip(RoundedCornerShape(mediumCornerRadius))
                    .clickable { photoPickerLauncher.launch("image/*") },
                color = Purple.copy(alpha = 0.08f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = basicMargin),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(mediumCornerRadius)
                ) {
                    Icon(Icons.Default.Add, null, tint = Purple, modifier = Modifier.size(iconSizeLarge))
                    Column {
                        Text(stringResource(R.string.add_photo), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = Purple)
                        Text(stringResource(R.string.add_photo_description), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Text(stringResource(R.string.problem_specification), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            OutlinedTextField(
                value = state.createDescription,
                onValueChange = { viewModel.onCreateDescChange(it) },
                placeholder = { Text(stringResource(R.string.problem_specification_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                shape = RoundedCornerShape(mediumCornerRadius)
            )

            OutlinedTextField(
                value = state.createPlace,
                onValueChange = { viewModel.onCreatePlaceChange(it) },
                placeholder = { Text(stringResource(R.string.location_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(mediumCornerRadius),
                leadingIcon = { Icon(Icons.Outlined.Place, null, tint = Purple, modifier = Modifier.size(iconMediumSmall)) }
            )

            Text(stringResource(R.string.report_type), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(halfMargin)) {
                items(postTypes) { (type, label) ->
                    FilterChip(
                        selected = state.createType == type,
                        onClick = { viewModel.onCreateTypeChange(type) },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Purple,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Text(stringResource(R.string.report_severity), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            severities.forEachIndexed { index, label ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(smallCornerRadius))
                        .clickable { viewModel.onCreateSeverityChange(severityKeys[index]) }
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = state.createSeverity == severityKeys[index],
                        onClick = { viewModel.onCreateSeverityChange(severityKeys[index]) },
                        colors = RadioButtonDefaults.colors(selectedColor = Purple)
                    )
                    Text(label, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Button(
                onClick = { viewModel.submitPost() },
                enabled = !state.isCreating && state.createDescription.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(fabHeight),
                colors = ButtonDefaults.buttonColors(containerColor = Purple),
                shape = RoundedCornerShape(14.dp)
            ) {
                if (state.isCreating)
                    CircularProgressIndicator(Modifier.size(iconMediumSmall), color = Color.White, strokeWidth = 2.dp)
                else
                    Text(stringResource(R.string.send), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}