package com.example.projectobcane.database.reports

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity): Long

    @Update
    suspend fun updateReport(report: ReportEntity)

    @Delete
    suspend fun deleteReport(report: ReportEntity)

    @Insert
    suspend fun insertImages(images: List<ReportImageEntity>)

    @Query("DELETE FROM report_images WHERE reportLocalId = :reportId")
    suspend fun deleteImagesForReport(reportId: Long)

    @Transaction
    @Query("SELECT * FROM reports ORDER BY createdAt DESC")
    fun getAllReports(): Flow<List<ReportWithImages>>

    @Transaction
    @Query("SELECT * FROM reports WHERE localId = :id")
    suspend fun getReportWithImages(id: Long): ReportWithImages?
}