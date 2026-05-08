package com.example.projectobcane.screens.news.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
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
class NewsDetailViewModel @Inject constructor(
    private val repository: INewsRemoteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewsDetailUIState())
    val uiState = _uiState.asStateFlow()

    private val newsId: Long =
        savedStateHandle["id"] ?: -1L

    init {
        loadNewsDetail()
    }

    private fun loadNewsDetail() {

        viewModelScope.launch {

            when (val result = repository.getAllNews()) {

                is CommunicationResult.Success -> {

                    Log.d("NEWS_DETAIL", "Loaded ${result.data.items.size} items")

                    val item = result.data.items.firstOrNull {
                        it.id == newsId
                    }

                    val localized =
                        item?.localizedAttributes?.get("cz")
                            ?: item?.localizedAttributes?.get("en")

                    val mapped = item?.let {

                        NewsItemUi(
                            id = it.id,
                            title = localized?.title ?: "",
                            text = localized?.text ?: "",
                            imageUrl = it.titleImageUrl,
                            created = it.created,
                            startDate = it.startDate,
                            endDate = it.endDate,
                            category = it.category.firstOrNull()
                                ?.localizedAttributes?.get("cz")
                                ?.name ?: "",
                            faculty = it.faculty.firstOrNull()
                                ?.localizedAttributes?.get("cz")
                                ?.name ?: ""
                        )
                    }

                    Log.d("NEWS_DETAIL", "Selected item = $mapped")

                    _uiState.value = NewsDetailUIState(
                        loading = false,
                        news = mapped
                    )
                }

                else -> {
                    _uiState.value = NewsDetailUIState(
                        loading = false
                    )
                }
            }
        }
    }
}