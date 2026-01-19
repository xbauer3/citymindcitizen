package com.example.projectobcane

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.projectobcane.database.ProjectDatabase
import com.example.projectobcane.database.reports.LocationEntity
import com.example.projectobcane.database.reports.Report
import com.example.projectobcane.database.reports.ReportDao
import com.example.projectobcane.database.reports.ReportRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReportRepositoryTest {

    private lateinit var db: ProjectDatabase
    private lateinit var dao: ReportDao
    private lateinit var repository: ReportRepository

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ProjectDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        dao = db.reportDao()
        repository = ReportRepository(dao)
    }

    @After
    fun tearDown() {
        db.close()
    }


    /**
     * Tests that inserting a report into the repository
     * results in the report being returned from getAllReports().
     */
    @Test
    fun insertReport_andGetAll_returnsInsertedReport() = runTest {
        val report = Report(
            title = "Broken road",
            description = "Hole in road",
            category = "Infrastructure",
            status = "new",
            location = LocationEntity(49.2, 16.6),
            photoUri = ""
        )

        repository.insert(report)

        val reports = repository.getAllReports().first()

        Assert.assertEquals(1, reports.size)
        Assert.assertEquals("Broken road", reports.first().title)
    }

    /**
     * Tests that deleting a report removes it from the database.
     */

    @Test
    fun deleteReport_removesItem() = runTest {
        val report = Report(
            title = "Trash",
            description = "Overflowing bin",
            category = "Waste",
            status = "new",
            location = LocationEntity(49.0, 16.0),
            photoUri = ""
        )

        repository.insert(report)
        repository.delete(report)

        val reports = repository.getAllReports().first()
        assertEquals(0, reports.size)
    }


    /**
     * Tests that updating a report persists the updated values.
     */
    @Test
    fun updateReport_updatesItem() = runTest {
        val report = Report(
            title = "Lamp",
            description = "Broken lamp",
            category = "Lighting",
            status = "new",
            location = LocationEntity(49.1, 16.1),
            photoUri = ""
        )

        repository.insert(report)

        val inserted = repository.getAllReports().first().first()
        val updated = inserted.copy(title = "Lamp fixed")

        repository.update(updated)

        val reports = repository.getAllReports().first()
        assertEquals("Lamp fixed", reports.first().title)
    }

    /**
     * Tests that a report can be retrieved by its ID.
     */
    @Test
    fun getById_returnsCorrectReport() = runTest {
        val report = Report(
            title = "Road",
            description = "Hole",
            category = "Infrastructure",
            status = "new",
            location = LocationEntity(50.0, 15.0),
            photoUri = ""
        )

        repository.insert(report)

        val inserted = repository.getAllReports().first().first()
        val loaded = repository.getById(inserted.id!!)

        assertEquals(inserted.title, loaded.title)
    }

    /**
     * Tests that an empty database returns an empty list.
     */
    @Test
    fun emptyDatabase_returnsEmptyList() = runTest {
        val reports = repository.getAllReports().first()
        assertEquals(0, reports.size)
    }


}