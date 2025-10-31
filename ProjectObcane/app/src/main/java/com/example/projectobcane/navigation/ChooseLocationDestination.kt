package com.example.projectobcane.navigation

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ChooseLocationDestination(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val locations: List<EventLocation> = emptyList()
)

@JsonClass(generateAdapter = true)
data class EventLocation(
    val latitude: Double,
    val longitude: Double,
    val name: String
)