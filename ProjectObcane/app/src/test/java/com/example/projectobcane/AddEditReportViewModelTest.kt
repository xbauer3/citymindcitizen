package com.example.projectobcane

import com.example.projectobcane.database.reports.IReportLocalRepository
import com.example.projectobcane.database.reports.Report
import com.example.projectobcane.screens.reports.addEdit.AddEditReportScreenViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AddEditReportViewModelTest {

    private lateinit var repository: FakeReportRepository
    private lateinit var viewModel: AddEditReportScreenViewModel

    @Before
    fun setup() {
        repository = FakeReportRepository()
        viewModel = AddEditReportScreenViewModel(repository)
    }

    /**
     * Tests that changing the title updates the UI state
     * and clears any previous title validation error.
     */

    @Test
    fun onTitleChanged_updatesState() {
        viewModel.onTitleChanged("Test title")

        val state = viewModel.addEditReportUIState.value
        assertEquals("Test title", state.report.title)
        assertNull(state.titleError)
    }


    /**
     * Tests that attempting to save a report with an empty title
     * results in a validation error.
     */
    @Test
    fun saveReport_withEmptyTitle_setsError() {
        viewModel.saveReport()

        val state = viewModel.addEditReportUIState.value
        assertNotNull(state.titleError)
    }
}




class FakeReportRepository : IReportLocalRepository {

    private val reports = mutableListOf<Report>()
    private val flow = MutableStateFlow<List<Report>>(emptyList())

    override fun getAllReports(): Flow<List<Report>> = flow

    override suspend fun insert(report: Report) {
        reports.add(report)
        flow.value = reports
    }

    override suspend fun update(report: Report) {}
    override suspend fun delete(report: Report) {}
    override suspend fun getById(id: Long): Report = reports.first()
}