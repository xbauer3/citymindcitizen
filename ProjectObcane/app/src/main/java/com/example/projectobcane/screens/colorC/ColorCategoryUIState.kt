package com.example.projectobcane.screens.colorC

import com.example.projectobcane.database.EventWithColorCategory
import com.example.projectobcane.database.color.ColorCategory

data class ColorCategoryUIState (
    var colorCategory: ColorCategory = ColorCategory(null, "","",1),

    val colorCategories: List<ColorCategory> = emptyList(),

    var loading: Boolean = true,

    var colorPriorityError: Int? = null,
    var colorNameError: Int? = null,
    var colorHexError: Int? = null,

)