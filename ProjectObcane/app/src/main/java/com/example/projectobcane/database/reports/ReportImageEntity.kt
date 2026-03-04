package com.example.projectobcane.database.reports

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "report_images")
data class ReportImageEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val reportLocalId: Long,

    val imageUri: String
)