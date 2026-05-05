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
data class ReportEntity(

    @PrimaryKey(autoGenerate = true)
    val localId: Long? = 0,

    // budoucí ID z API
    val remoteId: Long? = null,

    val title: String,
    val description: String,

    // bude odpovídat reportType z API
    val reportType: String,

    val status: String,

    @Embedded
    val location: LocationEntity?,

    val entityId: Int = 1932,

    val hashedEmail: String? = null,

    val dateAdded: String? = null,

    val createdAt: Long = System.currentTimeMillis()
)