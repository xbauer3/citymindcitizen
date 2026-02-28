package com.example.projectobcane.screens

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
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
import com.example.projectobcane.utils.OnboardingPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingScreen1(
    navigation: INavigationRouter
) {
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

    LaunchedEffect(Unit) { viewModel.loadSettings() }

    // ✅ reaguje na app theme (MaterialTheme), ne na systém
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f

    // ✅ Gradient Light/Dark
    val gradient = if (!isDark) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF6E56CF),
                Color(0xFF8E77F5),
                Color(0xFFF7F5FF)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF171027),
                Color(0xFF24153E),
                Color(0xFF0F0B16)
            )
        )
    }

    val titleColor = if (!isDark) Color.White else Color(0xFFF3EEFF)
    val bodyColor = if (!isDark) Color.White.copy(alpha = 0.92f) else Color(0xFFD7CCE9)
    val cardOverlay = if (!isDark) Color.White.copy(alpha = 0.20f) else Color.White.copy(alpha = 0.10f)
    val pillSelectedBg = if (!isDark) Color.White else Color(0xFFF3EEFF)
    val pillSelectedText = if (!isDark) Color(0xFF3D2E86) else Color(0xFF24153E)
    val pillUnselectedText = if (!isDark) Color.White else Color(0xFFE7DFFF)
    val continueBg = if (!isDark) Color.White else Color(0xFFF3EEFF)
    val continueText = if (!isDark) Color(0xFF3D2E86) else Color(0xFF24153E)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(horizontal = 20.dp)
            .statusBarsPadding()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 18.dp)
        ) {
            // Logo row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.infoobce),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = "CityMind",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = titleColor
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "AI agent, novinky a hlášení z vaší obce",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Vyberte si obec a budete mít vše přehledně na jednom místě.",
                style = MaterialTheme.typography.bodyLarge,
                color = bodyColor
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Illustration (placeholder)
            Image(
                painter = painterResource(id = R.drawable.infoobce),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = if (!isDark) 0.06f else 0.04f))
            )

            Spacer(modifier = Modifier.weight(1f))

            // ✅ Bottom controls v "safe" zóně (nezakryje se navigačními ikonami)
            BottomControls(
                supportedLanguages = state.supportedLanguages,
                selectedLang = LanguageHolder.language,
                pillBackground = cardOverlay,
                selectedBackground = pillSelectedBg,
                selectedTextColor = pillSelectedText,
                unselectedTextColor = pillUnselectedText,
                continueBackground = continueBg,
                continueTextColor = continueText,
                onSelectLang = { locale ->
                    viewModel.updateLanguage(locale) { scope.launch { activity.recreate() } }
                },
                onContinue = { showSheet.value = true }
            )
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
private fun BottomControls(
    supportedLanguages: List<java.util.Locale>,
    selectedLang: String,
    pillBackground: Color,
    selectedBackground: Color,
    selectedTextColor: Color,
    unselectedTextColor: Color,
    continueBackground: Color,
    continueTextColor: Color,
    onSelectLang: (java.util.Locale) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            // ✅ hlavní fix překrytí: safe padding pro nav bary
            .navigationBarsPadding()
            .padding(bottom = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Language pills
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(pillBackground)
                    .padding(4.dp)
            ) {
                supportedLanguages.forEach { locale ->
                    val isSelected = locale.language == selectedLang
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSelectLang(locale) },
                        color = if (isSelected) selectedBackground else Color.Transparent
                    ) {
                        Text(
                            text = if (locale.language == "cs") "CZ" else "EN",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            color = if (isSelected) selectedTextColor else unselectedTextColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Button(
                onClick = onContinue,
                colors = ButtonDefaults.buttonColors(containerColor = continueBackground),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.continue_button),
                    color = continueTextColor,
                    fontWeight = FontWeight.Bold
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