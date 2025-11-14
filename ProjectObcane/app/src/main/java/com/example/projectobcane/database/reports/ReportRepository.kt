package com.example.projectobcane.database.reports

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.text.insert


class ReportRepository @Inject constructor(
    private val dao: ReportDao
) : IReportLocalRepository  {
    override fun getAllReports(): Flow<List<Report>> {
        return dao.getAllReports()
    }


    override suspend fun insert(report: Report) {
        dao.insert(report)
    }

    override suspend fun update(report: Report) {
        dao.update(report)
    }

    override suspend fun delete(report: Report) {
        dao.delete(report)
    }

    override suspend fun getById(id: Long): Report {
        return dao.getById(id)
    }

}
