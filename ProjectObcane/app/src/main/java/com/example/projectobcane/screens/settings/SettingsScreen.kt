package com.example.projectobcane.screens.settings

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.projectobcane.R
import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.ui.elements.BaseScreen
import com.example.projectobcane.ui.theme.Purple
import com.example.projectobcane.ui.theme.basicMargin
import com.example.projectobcane.utils.OnboardingPreferences
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(navigation: INavigationRouter) {
    val viewModel = hiltViewModel<SettingsViewModel>()
    val state by viewModel.settingsScreenUIState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.loadSettings() }

    BaseScreen(
        topBarText = stringResource(R.string.settings),
        showLoading = state.loading,
        onBackClick = { navigation.returnBack() },
        actions = {}
    ) { padding ->
        SettingsScreenContent(
            paddingValues = padding,
            viewModel = viewModel
        )
    }
}

@Composable
fun SettingsScreenContent(
    paddingValues: PaddingValues,
    viewModel: SettingsViewModel
) {
    val state by viewModel.settingsScreenUIState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val activity = context as Activity
    val coroutineScope = rememberCoroutineScope()

    val isDark = state.darkModeEnabled


    val cardColor = if (isDark)
        MaterialTheme.colorScheme.surface.copy(alpha = 1f).let {
            Color(0xFF1E1B2E)
        }
    else
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)



    val pillBackground = if (isDark) Color(0xFF2A2640) else Color.Transparent

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(
                start = basicMargin,
                end = basicMargin,
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
    ) {
        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = stringResource(id = R.string.language),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))

        LanguagePillSwitch(
            supported = state.supportedLanguages,
            selectedLanguage = state.selectedLanguage,
            pillBackground = pillBackground,
            onSelect = { locale ->
                viewModel.updateLanguage(locale) { activity.recreate() }
            }
        )

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = stringResource(R.string.theme),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.dark_mode),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(R.string.dark_mode_description),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = state.darkModeEnabled,
                onCheckedChange = { viewModel.setDarkMode(it) }
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = stringResource(R.string.municipality),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(14.dp)
        ) {
            MunicipalityPicker(
                currentCountry = state.selectedCountry,
                currentCity = state.selectedCity,
                isDark = isDark,
                onSave = { country, city ->
                    viewModel.saveLocation(country, city)
                    Toast.makeText(context, context.getString(R.string.location_saved), Toast.LENGTH_SHORT).show()
                }
            )
        }






        // For debug and showcase only

        Spacer(modifier = Modifier.height(140.dp))


        val versionName = context.packageManager
            .getPackageInfo(context.packageName, 0)
            .versionName

        Text(
            text = "${stringResource(R.string.app_version)} $versionName",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(basicMargin))

        Button(
            onClick = { coroutineScope.launch { OnboardingPreferences.reset(context) } },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = stringResource(R.string.reset_onboarding),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
private fun LanguagePillSwitch(
    supported: List<java.util.Locale>,
    selectedLanguage: String,
    pillBackground: Color,
    onSelect: (java.util.Locale) -> Unit
) {
    val borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(pillBackground)
            .then(
                if (pillBackground == Color.Transparent)
                    Modifier.border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(14.dp)
                    )
                else Modifier
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        supported.forEach { locale ->
            val isSelected = locale.language == selectedLanguage
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onSelect(locale) },
                color = if (isSelected) Purple else Color.Transparent
            ) {
                Text(
                    text = if (locale.language == "cs") "CZ" else "EN",
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MunicipalityPicker(
    currentCountry: String?,
    currentCity: String?,
    isDark: Boolean,
    onSave: (String, String) -> Unit
) {
    data class Country(val name: String, val cities: List<String>)
    val countries = listOf(
        Country("Česká republika", listOf("Brno", "Praha", "Ostrava", "Olomouc", "Zlín", "Plzeň")),
        Country("Slovenská republika", listOf("Bratislava", "Košice", "Prešov", "Žilina", "Nitra", "Banská Bystrica"))
    )

    var pickedCountry by remember(currentCountry) {
        mutableStateOf(countries.firstOrNull { it.name == currentCountry })
    }
    var pickedCity by remember(currentCity) { mutableStateOf(currentCity) }

    val borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
    val surfaceColor = MaterialTheme.colorScheme.surface

    val fieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = surfaceColor,
        focusedContainerColor = surfaceColor,
        disabledContainerColor = surfaceColor,
        unfocusedBorderColor = borderColor,
        focusedBorderColor = Purple,
        disabledBorderColor = borderColor.copy(alpha = 0.2f)
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        var countryExpanded by remember { mutableStateOf(false) }

        Text(
            text = stringResource(R.string.select_country),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))

        ExposedDropdownMenuBox(
            expanded = countryExpanded,
            onExpandedChange = { countryExpanded = !countryExpanded }
        ) {
            OutlinedTextField(
                value = pickedCountry?.name ?: "",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = countryExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors
            )
            ExposedDropdownMenu(
                expanded = countryExpanded,
                onDismissRequest = { countryExpanded = false },
                containerColor = if (isDark) Color(0xFF1E1B2E) else surfaceColor
            ) {
                countries.forEach { country ->
                    DropdownMenuItem(
                        text = { Text(country.name) },
                        onClick = {
                            pickedCountry = country
                            pickedCity = null
                            countryExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        var cityExpanded by remember { mutableStateOf(false) }

        Text(
            text = stringResource(R.string.select_city),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))

        ExposedDropdownMenuBox(
            expanded = cityExpanded,
            onExpandedChange = { if (pickedCountry != null) cityExpanded = !cityExpanded }
        ) {
            OutlinedTextField(
                value = pickedCity ?: "",
                onValueChange = {},
                readOnly = true,
                enabled = pickedCountry != null,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors
            )
            ExposedDropdownMenu(
                expanded = cityExpanded,
                onDismissRequest = { cityExpanded = false },
                containerColor = if (isDark) Color(0xFF1E1B2E) else surfaceColor
            ) {
                pickedCountry?.cities?.forEach { city ->
                    DropdownMenuItem(
                        text = { Text(city) },
                        onClick = {
                            pickedCity = city
                            cityExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = {
                val c = pickedCountry
                val ci = pickedCity
                if (c != null && ci != null) onSave(c.name, ci)
            },
            enabled = pickedCountry != null && pickedCity != null,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = stringResource(R.string.save),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}