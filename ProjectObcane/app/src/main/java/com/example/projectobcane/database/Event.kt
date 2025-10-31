package com.example.projectobcane.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import androidx.room.ForeignKey
import com.example.projectobcane.database.color.ColorCategory







@JsonClass(generateAdapter = true)
data class LocationEntity(
    @Json(name = "latitude")
    var latitude: Double?,
    @Json(name = "longitude")
    var longitude: Double?)






@Entity(tableName = "events", foreignKeys = [
    ForeignKey(
        entity = ColorCategory::class,
        parentColumns = ["id"],
        childColumns = ["colorCategoryId"],
        onDelete = ForeignKey.SET_NULL
    )
])
data class Event(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,




    var eventName: String,
    var eventDescription: String,
    var startDate: Long?,
    var endDate: Long?,

    var colorCategoryId: Long?,

    var notificationDate: Long?,


    @Embedded val location: LocationEntity


)
