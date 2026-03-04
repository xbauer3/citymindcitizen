package com.example.projectobcane.database.reports

import androidx.room.Embedded
import androidx.room.Relation



data class ReportWithImages(

    @Embedded
    val report: ReportEntity,

    @Relation(
        parentColumn = "localId",
        entityColumn = "reportLocalId"
    )
    val images: List<ReportImageEntity>
)