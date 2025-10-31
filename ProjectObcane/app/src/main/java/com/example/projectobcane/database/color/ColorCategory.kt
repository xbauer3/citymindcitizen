package com.example.projectobcane.database.color

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "color_categories")
data class ColorCategory(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = 0,
    val name: String,
    val colorHex: String,
    val priority: Int
)