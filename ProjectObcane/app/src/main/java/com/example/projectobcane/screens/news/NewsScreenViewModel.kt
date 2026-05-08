package com.example.projectobcane.screens.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.communication.CommunicationResult
import com.example.projectobcane.communication.news.INewsRemoteRepository
import com.example.projectobcane.models.NewsItemUi
import com.example.projectobcane.screens.settings.LanguageHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsScreenViewModel @Inject constructor(
    private val repository: INewsRemoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewsScreenUIState())
    val uiState = _uiState.asStateFlow()

    // Track which language was used for the last load so we can detect changes
    private var lastLoadedLanguage: String = LanguageHolder.language

    init {
        loadNews()
    }

    fun loadNews() {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(loading = true)

            val lang = when (LanguageHolder.language) {
                "cs" -> "cz"   // i have a missmatch between local cs and api's cz
                "en" -> "en"
                else -> LanguageHolder.language
            }
            lastLoadedLanguage = LanguageHolder.language




            fun <T> Map<String, T>.localized(): T? =
                this[lang] ?: this["en"] ?: this["cz"] ?: values.firstOrNull()

            when (val result = repository.getAllNews()) {

                is CommunicationResult.Success -> {

                    Log.d("NEWS_API", "FULL RESPONSE: ${result.data}")

                    val items = result.data.items.map { item ->

                        val localized = item.localizedAttributes.localized()

                        NewsItemUi(
                            id = item.id,
                            title = localized?.title ?: "No title",
                            text = localized?.text ?: "",
                            imageUrl = item.titleImageUrl,
                            created = item.created,
                            startDate = item.startDate,
                            endDate = item.endDate,
                            category = item.category.firstOrNull()
                                ?.localizedAttributes?.localized()
                                ?.name ?: "",
                            faculty = item.faculty.firstOrNull()
                                ?.localizedAttributes?.localized()
                                ?.name ?: ""
                        )
                    }

                    _uiState.value = NewsScreenUIState(
                        loading = false,
                        news = items
                    )
                }

                is CommunicationResult.Error -> {
                    Log.e("NEWS_API", "API ERROR: ${result.error.message}")
                    _uiState.value = NewsScreenUIState(
                        loading = false,
                        error = result.error.message
                    )
                }

                is CommunicationResult.ConnectionError -> {
                    Log.e("NEWS_API", "CONNECTION ERROR")
                    _uiState.value = NewsScreenUIState(
                        loading = false,
                        error = "Connection error"
                    )
                }

                is CommunicationResult.Exception -> {
                    Log.e("NEWS_API", "EXCEPTION", result.exception)
                    _uiState.value = NewsScreenUIState(
                        loading = false,
                        error = result.exception.message
                    )
                }
            }
        }
    }

    // Call this from the screen when it becomes visible,
    // so that a language change in Settings triggers a reload

    fun reloadIfLanguageChanged() {
        if (LanguageHolder.language != lastLoadedLanguage) {
            loadNews()
        }
    }
}