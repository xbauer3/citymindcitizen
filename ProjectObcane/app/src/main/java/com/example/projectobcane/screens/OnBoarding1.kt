package com.example.projectobcane.screens

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import com.example.projectobcane.screens.settings.LanguageHolder
import com.example.projectobcane.screens.settings.SettingsViewModel
import com.example.projectobcane.ui.theme.basicMargin
import com.example.projectobcane.utils.CityPreferences
import com.example.projectobcane.utils.OnboardingPreferences
import kotlinx.coroutines.launch

private data class Country(val name: String, val cities: List<String>)

private val SUPPORTED_COUNTRIES = listOf(
    Country(
        name = "Česká republika",
        cities = listOf("Brno", "Praha", "Ostrava", "Olomouc", "Zlín", "Plzeň")
    ),
    Country(
        name = "Slovenská republika",
        cities = listOf("Bratislava", "Košice", "Prešov", "Žilina", "Nitra", "Banská Bystrica")
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingScreen1(navigation: INavigationRouter) {
    val viewModel = hiltViewModel<SettingsViewModel>()
    val state by viewModel.settingsScreenUIState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val activity = context as Activity
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showSheet = remember { mutableStateOf(false) }
    val selectedCountry = remember { mutableStateOf<Country?>(null) }
    val selectedCity = remember { mutableStateOf<String?>(null) }
    // 0 = country step, 1 = city step
    val sheetStep = remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) { viewModel.loadSettings() }

    val gradient = Brush.verticalGradient(
        colorStops = arrayOf(
            0.00f to Color(0xFF7822FF),
            0.45f to Color(0xFF4A1C9E),
            1.00f to Color(0xFF151041)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .statusBarsPadding()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = basicMargin)
                .padding(bottom = 96.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.citymindlogo),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.size(100.dp)
                )
            }

            Text(
                text = stringResource(R.string.ai_agent_description),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = MaterialTheme.typography.headlineMedium.lineHeight
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.select_municipality_info),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.82f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = painterResource(id = R.drawable.onboardingfoto),
                contentDescription = null,
                modifier = Modifier.size(400.dp)
            )
        }


        BottomControls(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = basicMargin, vertical = 16.dp),
            supportedLanguages = state.supportedLanguages,
            selectedLang = LanguageHolder.language,
            onSelectLang = { locale ->
                viewModel.updateLanguage(locale) { scope.launch { activity.recreate() } }
            },
            onContinue = {
                sheetStep.intValue = 0
                selectedCountry.value = null
                selectedCity.value = null
                showSheet.value = true
            }
        )


        if (showSheet.value) {
            ModalBottomSheet(
                onDismissRequest = { showSheet.value = false },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface
            ) {

                AnimatedContent(
                    targetState = sheetStep.intValue,
                    transitionSpec = {
                        val enter = slideInHorizontally(
                            animationSpec = tween(300, easing = FastOutSlowInEasing),
                            initialOffsetX = { if (targetState > initialState) it else -it }
                        )
                        val exit = slideOutHorizontally(
                            animationSpec = tween(300, easing = FastOutSlowInEasing),
                            targetOffsetX = { if (targetState > initialState) -it else it }
                        )
                        enter togetherWith exit using SizeTransform(clip = true)
                    },
                    label = "onboarding_sheet_step"
                ) { step ->
                    when (step) {
                        0 -> CountrySheet(
                            selected = selectedCountry.value,
                            onSelect = { selectedCountry.value = it },
                            onBack = { showSheet.value = false },
                            onContinue = {
                                if (selectedCountry.value != null) {
                                    selectedCity.value = null
                                    sheetStep.intValue = 1
                                }
                            }
                        )
                        1 -> CitySheet(
                            country = selectedCountry.value,
                            selected = selectedCity.value,
                            onSelect = { selectedCity.value = it },
                            onBack = { sheetStep.intValue = 0 },
                            onContinue = {
                                scope.launch {

                                    selectedCountry.value?.let { country ->
                                        selectedCity.value?.let { city ->
                                            CityPreferences.save(context, country.name, city)
                                        }
                                    }
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
    }
}


@Composable
private fun BottomControls(
    modifier: Modifier = Modifier,
    supportedLanguages: List<java.util.Locale>,
    selectedLang: String,
    onSelectLang: (java.util.Locale) -> Unit,
    onContinue: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Language pill
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(alpha = 0.18f))
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            supportedLanguages.forEachIndexed { index, locale ->
                val isSelected = locale.language == selectedLang
                val label = if (locale.language == "cs") "CZ" else "EN"

                if (index > 0) {
                    Text(
                        text = "|",
                        color = Color.White.copy(alpha = 0.40f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSelectLang(locale) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.55f),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Button(
            onClick = onContinue,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp),
            contentPadding = PaddingValues(horizontal = 28.dp, vertical = 14.dp)
        ) {
            Text(
                text = stringResource(R.string.continue_button),
                color = Color(0xFF3A1499),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


@Composable
private fun CountrySheet(
    selected: Country?,
    onSelect: (Country) -> Unit,
    onBack: () -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp)
    ) {
        SheetHeader(
            title = stringResource(R.string.select_country),
            onBack = onBack
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.selection_affects_content),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        SUPPORTED_COUNTRIES.forEach { country ->
            SelectionRow(
                label = country.name,
                isSelected = selected == country,
                onClick = { onSelect(country) }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onContinue,
            enabled = selected != null,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(text = stringResource(R.string.continue_action), fontWeight = FontWeight.Bold)
        }
    }
}


@Composable
private fun CitySheet(
    country: Country?,
    selected: String?,
    onSelect: (String) -> Unit,
    onBack: () -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp)
    ) {
        SheetHeader(
            title = stringResource(R.string.select_city),
            onBack = onBack
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.change_selection_later),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        country?.cities?.forEach { city ->
            SelectionRow(
                label = city,
                isSelected = selected == city,
                onClick = { onSelect(city) }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onContinue,
            enabled = selected != null,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(text = stringResource(R.string.continue_action), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SheetHeader(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = stringResource(R.string.back),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable { onBack() }
                .padding(4.dp)
                .size(24.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(R.string.back),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onBack() }
                .padding(horizontal = 4.dp, vertical = 4.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(60.dp))
    }
}

@Composable
private fun SelectionRow(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surface,
        tonalElevation = if (isSelected) 0.dp else 1.dp
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}