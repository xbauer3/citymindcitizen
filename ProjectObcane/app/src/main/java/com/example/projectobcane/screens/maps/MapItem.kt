package com.example.projectobcane.screens.maps

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

sealed class MapItem(
    private val position: LatLng,
    private val title: String,
    private val snippet: String
) : ClusterItem {

    override fun getPosition() = position
    override fun getTitle() = title
    override fun getSnippet() = snippet
    override fun getZIndex(): Float = 0f

    data class ReportItem(
        val reportId: Long,
        val lat: Double,
        val lng: Double,
        val reportTitle: String
    ) : MapItem(
        LatLng(lat, lng),
        reportTitle,
        "Report"
    )

    data class EventItem(
        val eventId: Long,
        val lat: Double,
        val lng: Double,
        val eventTitle: String
    ) : MapItem(
        LatLng(lat, lng),
        eventTitle,
        "Event"
    )
}
