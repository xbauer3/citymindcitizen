package com.example.projectobcane.database.reports

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class LocationEntity(
    @Json(name = "latitude")
    var latitude: Double?,
    @Json(name = "longitude")
    var longitude: Double?)





@Entity(tableName = "reports")
data class Report(
    @PrimaryKey(autoGenerate = true) val id: Long? = 0,
    val title: String,
    val description: String,
    val category: String,
    val status: String,

    @Embedded val location: LocationEntity,

    val photoUri: String,
    val createdAt: Long = System.currentTimeMillis()


)
