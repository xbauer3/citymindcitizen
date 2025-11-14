package com.example.projectobcane.database.reports

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "reports")
data class Report(
    @PrimaryKey(autoGenerate = true) val id: Long? = 0,
    val title: String,
    val description: String,
    val category: String,
    val status: String,
    val photoUri: String?,
    val createdAt: Long = System.currentTimeMillis()
)
