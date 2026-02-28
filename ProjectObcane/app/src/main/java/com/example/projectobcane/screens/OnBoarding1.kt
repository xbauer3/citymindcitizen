package com.example.projectobcane.screens

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.projectobcane.R
import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.screens.settings.SettingsViewModel
import com.example.projectobcane.utils.OnboardingPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingScreen1(
    navigation: INavigationRouter
) {
    // Redesigned to match Figma (gradient background + language switch + municipality sheet)
    OnBoardingScreen1Content(navigation = navigation)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingScreen1Content(
    navigation: INavigationRouter
) {

    val viewModel = hiltViewModel<SettingsViewModel>()
    val state by viewModel.settingsScreenUIState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as Activity
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showSheet = remember { mutableStateOf(false) }
    val selectedMunicipality = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadSettings()
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF6E56CF),
            Color(0xFF8E77F5),
            Color(0xFFF7F5FF)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(horizontal = 20.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp, bottom = 24.dp)
        ) {

            // Logo row (CityMind)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.infoobce),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = "CityMind",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "AI agent, novinky a hlášení z vaší obce",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Vyberte si obec a budete mít vše přehledně na jednom místě.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.92f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Illustration (placeholder uses existing image)
            Image(
                painter = painterResource(id = R.drawable.infoobce),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(24.dp))
            )

            Spacer(modifier = Modifier.weight(1f))

            // Bottom controls: language toggle + continue
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White.copy(alpha = 0.20f))
                        .padding(4.dp)
                ) {
                    val langs = state.supportedLanguages
                    langs.forEach { locale ->
                        val selected = locale.language == com.example.projectobcane.screens.settings.LanguageHolder.language
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    viewModel.updateLanguage(locale) {
                                        scope.launch {
                                            activity.recreate()
                                        }
                                    }
                                },
                            color = if (selected) Color.White else Color.Transparent
                        ) {
                            Text(
                                text = if (locale.language == "cs") "CZ" else "EN",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                color = if (selected) Color(0xFF3D2E86) else Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Button(
                    onClick = { showSheet.value = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.continue_button),
                        color = Color(0xFF3D2E86),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (showSheet.value) {
            ModalBottomSheet(
                onDismissRequest = { showSheet.value = false },
                sheetState = sheetState
            ) {
                MunicipalitySheet(
                    selected = selectedMunicipality.value,
                    onSelect = { selectedMunicipality.value = it },
                    onBack = { showSheet.value = false },
                    onContinue = {
                        scope.launch {
                            OnboardingPreferences.setCompleted(context)
                            showSheet.value = false
                            navigation.navigateToMainScreen()
                        }
                    }
                )
            }
        }
    }
}


@Composable
private fun MunicipalitySheet(
    selected: String?,
    onSelect: (String) -> Unit,
    onBack: () -> Unit,
    onContinue: () -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Zpět",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onBack() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            )
            Text(
                text = "Vyber obec",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(52.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Podle výběru se přizpůsobí informace a služby.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        val municipalities = listOf(
            "Brno",
            "Praha",
            "Ostrava",
            "Olomouc",
            "Zlín",
            "Plzeň"
        )

        municipalities.forEach { name ->
            val isSelected = selected == name
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onSelect(name) },
                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
            ) {
                Text(
                    text = name,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = onContinue,
            enabled = selected != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 18.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Pokračovat")
        }
    }
}

