package com.example.projectobcane.screens.maps

import android.content.Context
import com.example.projectobcane.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor

class MapClusterRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<MapItem>
) : DefaultClusterRenderer<MapItem>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(
        item: MapItem,
        markerOptions: MarkerOptions
    ) {
        val iconRes = when (item) {
            is MapItem.ReportItem -> R.drawable.baseline_report_24
            is MapItem.EventItem -> R.drawable.baseline_emoji_events_24
        }

        markerOptions
            .icon(bitmapDescriptorFromVector(context, iconRes))
            .title(item.title)
    }

    override fun shouldRenderAsCluster(cluster: Cluster<MapItem>): Boolean {
        return cluster.size > 1
    }
}





fun bitmapDescriptorFromVector(
    context: Context,
    @DrawableRes vectorResId: Int
): BitmapDescriptor {
    val drawable = ContextCompat.getDrawable(context, vectorResId)
        ?: error("Drawable not found")

    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    drawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}
