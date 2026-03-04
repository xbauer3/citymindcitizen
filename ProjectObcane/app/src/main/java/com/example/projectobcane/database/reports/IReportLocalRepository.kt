package com.example.projectobcane.database.reports

import com.example.projectobcane.navigation.EventLocation
import kotlinx.coroutines.flow.Flow


interface IReportLocalRepository {

    fun getAllReports(): Flow<List<ReportWithImages>>

    suspend fun getReportWithImages(id: Long): ReportWithImages?

    suspend fun insertReport(report: ReportEntity): Long

    suspend fun updateReport(report: ReportEntity)

    suspend fun deleteReport(report: ReportEntity)

    suspend fun insertImages(images: List<ReportImageEntity>)

    suspend fun deleteImagesForReport(reportLocalId: Long)
}
