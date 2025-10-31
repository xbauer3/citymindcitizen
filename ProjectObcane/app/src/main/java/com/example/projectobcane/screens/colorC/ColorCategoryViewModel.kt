package com.example.projectobcane.screens.colorC


import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.R

import com.example.projectobcane.database.color.ColorCategory
import com.example.projectobcane.database.color.IColorCategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.text.insert


@HiltViewModel
class ColorCategoryViewModel @Inject constructor(private val repository: IColorCategoryRepository) : ViewModel(), ColorCategoryActions {

    val colorCategories = repository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _colorCategoryUIState: MutableStateFlow<ColorCategoryUIState> =
        MutableStateFlow(value = ColorCategoryUIState())

    val colorCategoryUIState = _colorCategoryUIState.asStateFlow()


    init {
        viewModelScope.launch {
            colorCategories.collect { categories ->
                _colorCategoryUIState.update {
                    it.copy(colorCategories = categories)
                }
            }
        }
    }




    override fun onColorNameChanged(passedName: String) {

        val error = when {
            passedName.isBlank() -> R.string.can_t_be_empty
            passedName.length > 20 -> R.string.cannot_be_more_than_50
            else -> null
        }

        _colorCategoryUIState.value = _colorCategoryUIState.value.copy(colorCategory = _colorCategoryUIState.value.colorCategory.copy(name = passedName),colorNameError = error)

        //_colorCategoryUIState.value = _colorCategoryUIState.value.copy(colorCategory = _colorCategoryUIState.value.colorCategory.copy(name = passedName) , colorNameError = if (passedName.isNotBlank()) null else if (passedName.length > 50) _colorCategoryUIState.value.colorNameError  else _colorCategoryUIState.value.colorNameError)
    }

    override fun onColorHexChanged(passedHex: String) {
        val error = when {
            passedHex.isBlank() -> R.string.can_t_be_empty
            else -> null
        }
        _colorCategoryUIState.value = _colorCategoryUIState.value.copy(colorCategory = _colorCategoryUIState.value.colorCategory.copy(colorHex = passedHex) , colorHexError = error)

       //_colorCategoryUIState.value = _colorCategoryUIState.value.copy(colorCategory = _colorCategoryUIState.value.colorCategory.copy(colorHex = passedHex) , colorHexError = if (passedHex.isNotBlank()) null else _colorCategoryUIState.value.colorHexError)
    }

    override fun onColorPriorityChanged(passedPrio: String) {

        val parsed = passedPrio.toIntOrNull()
        val error = when {
            passedPrio.isBlank() -> R.string.can_t_be_empty
            parsed == null -> R.string.must_be_a_number
            parsed <= 0 -> R.string.must_be_more_than_0
            parsed > 20 -> R.string.cannot_be_more_than_20
            else -> null
        }



        _colorCategoryUIState.value = _colorCategoryUIState.value.copy(colorCategory = _colorCategoryUIState.value.colorCategory.copy(priority = parsed ?: 0) , colorPriorityError = error)


        //_colorCategoryUIState.value = _colorCategoryUIState.value.copy(colorCategory = _colorCategoryUIState.value.colorCategory.copy(priority = passedPrio) , colorPriorityError = if (passedPrio != 0) null else if (passedPrio > 20) _colorCategoryUIState.value.colorPriorityError   else _colorCategoryUIState.value.colorPriorityError)
    }







    fun saveColorCategory() : Boolean {

        val category = _colorCategoryUIState.value.colorCategory
        var hasError = false

        if (category.name.isBlank()) {
            _colorCategoryUIState.value = _colorCategoryUIState.value.copy(colorNameError = R.string.can_t_be_empty)
            hasError = true
        }

        if (category.name.length > 20) {
            _colorCategoryUIState.value = _colorCategoryUIState.value.copy(colorNameError = R.string.cannot_be_more_than_50)
            hasError = true
        }

        if (category.colorHex.isBlank()) {
            _colorCategoryUIState.value = _colorCategoryUIState.value.copy(colorHexError = R.string.can_t_be_empty)
            hasError = true
        }

        if (category.priority <= 0) {
            _colorCategoryUIState.value = _colorCategoryUIState.value.copy(colorPriorityError = R.string.must_be_more_than_0)
            hasError = true
        } else if (category.priority > 20) {
            _colorCategoryUIState.value = _colorCategoryUIState.value.copy(colorPriorityError = R.string.cannot_be_more_than_20)
            hasError = true
        }

        if (hasError) return false

        // Save logic
        viewModelScope.launch {
            if (category.id != null) {
                repository.update(category)
            } else {
                repository.insert(category)
            }
        }



        return true
    }



    fun loadColorCategory(category: ColorCategory) {
        viewModelScope.launch {
            _colorCategoryUIState.value = _colorCategoryUIState.value.copy(colorCategory = category)
        }
    }




    fun deleteColorCategory(category: ColorCategory) {
        viewModelScope.launch {
            repository.delete(category)
        }
    }


}