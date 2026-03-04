package com.example.projectobcane.database.reports

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.text.insert


class ReportRepository @Inject constructor(
    private val dao: ReportDao
) : IReportLocalRepository  {

    override fun getAllReports(): Flow<List<ReportWithImages>> {
        return dao.getAllReports()
    }

    override suspend fun getReportWithImages(id: Long): ReportWithImages? {
        return dao.getReportWithImages(id)
    }

    override suspend fun insertReport(report: ReportEntity): Long {
        return dao.insertReport(report)
    }

    override suspend fun updateReport(report: ReportEntity) {
        dao.updateReport(report)
    }

    override suspend fun deleteReport(report: ReportEntity) {
        dao.deleteReport(report)
        dao.deleteImagesForReport(report.localId!!)
    }

    override suspend fun insertImages(images: List<ReportImageEntity>) {
        dao.insertImages(images)
    }

    override suspend fun deleteImagesForReport(reportLocalId: Long) {
        dao.deleteImagesForReport(reportLocalId)
    }
}