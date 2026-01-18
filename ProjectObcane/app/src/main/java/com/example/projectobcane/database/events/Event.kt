package com.example.projectobcane.database.events

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Long? = 0,
    val title: String,
    val description: String,
    val category: String,
    val status: String,

    val latitude: Double?,
    val longitude: Double?,

    val photoUri: String,

    val createdAt: Long = System.currentTimeMillis()


)