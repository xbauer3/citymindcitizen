package com.example.projectobcane

import com.example.projectobcane.database.events.Event
import com.example.projectobcane.database.events.IEventLocalRepository
import com.example.projectobcane.database.reports.IReportLocalRepository
import com.example.projectobcane.database.reports.LocationEntity
import com.example.projectobcane.database.reports.Report
import com.example.projectobcane.screens.maps.MapItem
import com.example.projectobcane.screens.maps.MapScreenViewModel
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import kotlinx.coroutines.test.*
import org.junit.After

@OptIn(ExperimentalCoroutinesApi::class)
class MapScreenViewModelTest {

    private lateinit var reportRepo: FakeReportRepository
    private lateinit var eventRepo: FakeEventRepository
    private lateinit var viewModel: MapScreenViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        reportRepo = FakeReportRepository(
            initialReports = listOf(
                Report(
                    id = 1L,
                    title = "Report 1",
                    description = "Test report",
                    category = "TEST",
                    status = "ACTIVE",
                    location = LocationEntity(10.0, 10.0),
                    photoUri = "",
                    createdAt = System.currentTimeMillis()
                )
            )
        )

        eventRepo = FakeEventRepository(
            initialEvents = listOf(
                Event(
                    id = 1L,
                    title = "Event 1",
                    description = "Test event",
                    category = "TEST",
                    status = "UPCOMING",
                    placeName = "Test Place",
                    date = System.currentTimeMillis(),
                    location = LocationEntity(20.0, 20.0),
                    photoUri = "",
                    createdAt = System.currentTimeMillis()
                )
            )
        )


        viewModel = MapScreenViewModel(reportRepo, eventRepo)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    /**
     * Tests that loadMapItems() correctly combines
     * reports and events into map items.
     */
    @Test
    fun loadMapItems_combinesReportsAndEvents() = runTest(testDispatcher) {
        viewModel.loadMapItems()
        testScheduler.advanceUntilIdle() // Wait for coroutine to finish

        val items = viewModel.items.first()
        assertTrue(items.isNotEmpty())
        assertTrue(items.any { it is MapItem.ReportItem })
        assertTrue(items.any { it is MapItem.EventItem })
    }
}










