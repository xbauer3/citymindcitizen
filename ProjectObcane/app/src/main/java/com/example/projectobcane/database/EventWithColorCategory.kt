package com.example.projectobcane.database


import androidx.room.Embedded
import androidx.room.Relation
import com.example.projectobcane.database.color.ColorCategory

data class EventWithColorCategory(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "colorCategoryId",
        entityColumn = "id"
    )
    val colorCategory: ColorCategory?
)