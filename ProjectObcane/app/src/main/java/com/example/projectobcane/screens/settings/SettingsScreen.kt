package com.example.projectobcane.screens.settings

import android.app.Activity
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.projectobcane.R
import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.ui.elements.BaseScreen
import com.example.projectobcane.ui.theme.basicMargin
import com.example.projectobcane.utils.OnboardingPreferences
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    navigation: INavigationRouter
) {
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

    Column(
        modifier = Modifier
            .padding(
                start = basicMargin,
                end = basicMargin,
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
            .fillMaxSize()
    ) {
        val versionName = context.packageManager
            .getPackageInfo(context.packageName, 0)
            .versionName

        Text(text = " ${stringResource(R.string.app_version)} $versionName")
        Spacer(modifier = Modifier.height(basicMargin))

        // ---------- Language switch (same style as onboarding) ----------
        Text(text = stringResource(id = R.string.language))
        Spacer(modifier = Modifier.height(8.dp))

        LanguagePillSwitch(
            supported = state.supportedLanguages,
            selectedLanguage = state.selectedLanguage,
            onSelect = { locale ->
                viewModel.updateLanguage(locale) {
                    activity.recreate()
                }
            }
        )

        Spacer(modifier = Modifier.height(22.dp))

        // ---------- Dark mode switch ----------
        Text(text = "Téma")
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = stringResource(R.string.dark_mode), style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = stringResource(R.string.p_epnout_na_tmav_vzhled_aplikace),
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

        Button(
            onClick = {
                coroutineScope.launch { OnboardingPreferences.reset(context) }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.reset_onboarding))
        }
    }
}

@Composable
private fun LanguagePillSwitch(
    supported: List<java.util.Locale>,
    selectedLanguage: String,
    onSelect: (java.util.Locale) -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        supported.forEach { locale ->
            val isSelected = locale.language == selectedLanguage
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onSelect(locale) },
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
            ) {
                Text(
                    text = if (locale.language == "cs") "CZ" else "EN",
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}