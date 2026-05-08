package com.example.projectobcane.screens.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.communication.CommunicationResult
import com.example.projectobcane.communication.news.INewsRemoteRepository
import com.example.projectobcane.models.NewsItemUi
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

    init {
        loadNews()
    }

    fun loadNews() {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(loading = true)

            when (val result = repository.getAllNews()) {

                is CommunicationResult.Success -> {

                    Log.d("NEWS_API", "FULL RESPONSE: ${result.data}")

                    result.data.items.forEach {
                        Log.d(
                            "NEWS_API_ITEM",
                            """
                        ID: ${it.id}
                        TITLE: ${it.localizedAttributes["cz"]?.title}
                        IMAGE: ${it.titleImageUrl}
                        CATEGORY: ${it.category.firstOrNull()?.localizedAttributes?.get("cz")?.name}
                        FACULTY: ${it.faculty.firstOrNull()?.localizedAttributes?.get("cz")?.name}
                        """.trimIndent()
                        )
                    }

                    val items = result.data.items.map { item ->

                        val localized =
                            item.localizedAttributes["cz"]
                                ?: item.localizedAttributes["en"]

                        NewsItemUi(
                            id = item.id,
                            title = localized?.title ?: "No title",
                            text = localized?.text ?: "",
                            imageUrl = item.titleImageUrl,
                            created = item.created,
                            startDate = item.startDate,
                            endDate = item.endDate,
                            category = item.category.firstOrNull()
                                ?.localizedAttributes?.get("cz")
                                ?.name ?: "",

                            faculty = item.faculty.firstOrNull()
                                ?.localizedAttributes?.get("cz")
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
                }

                is CommunicationResult.Exception -> {
                    Log.e("NEWS_API", "EXCEPTION", result.exception)
                }
            }
        }
    }
}