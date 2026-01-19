package com.example.projectobcane.database.events

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.projectobcane.database.reports.LocationEntity


@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Long? = 0,
    val title: String,
    val description: String,

    val category: String,
    val status: String,

    val placeName: String,
    val date: Long?,

    @Embedded val location: LocationEntity,



    val photoUri: String,

    val createdAt: Long = System.currentTimeMillis()


)


enum class EventCategory { MUSIC, SPORT, CULTURE }
enum class EventStatus { UPCOMING, CANCELLED, FINISHED }
