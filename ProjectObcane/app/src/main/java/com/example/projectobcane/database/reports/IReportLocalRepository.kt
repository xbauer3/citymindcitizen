package com.example.projectobcane.database.reports

import com.example.projectobcane.navigation.EventLocation
import kotlinx.coroutines.flow.Flow


interface IReportLocalRepository {

    fun getAllReports(): Flow<List<Report>>

    suspend fun insert(report: Report)
    suspend fun update(report: Report)
    suspend fun delete(report: Report)
    suspend fun getById(id: Long): Report
}

