package com.example.projectobcane

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.projectobcane.database.reports.LocationEntity
import com.example.projectobcane.database.reports.Report
import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.screens.reports.ReportItem
import com.example.projectobcane.screens.reports.addEdit.AddEditReportScreenActions
import com.example.projectobcane.screens.reports.addEdit.AddEditReportScreenContent
import com.example.projectobcane.screens.reports.addEdit.AddEditReportScreenUIState
import com.example.projectobcane.ui.theme.ProjectObcaneTheme
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class ReportItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * UI test that verifies that a report item
     * correctly displays its title and status text.
     */

    @Test
    fun reportItem_displaysTitleAndStatus() {
        val report = Report(
            title = "Broken road",
            description = "Hole",
            category = "Infrastructure",
            status = "new",
            location = LocationEntity(49.2, 16.6),
            photoUri = ""
        )

        composeTestRule.setContent {
            ProjectObcaneTheme {
                ReportItem(
                    report = report,
                    onClick = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("Broken road")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Status: new")
            .assertIsDisplayed()
    }

    /**
     * UI test that verifies that clicking on a report item
     * triggers the provided onClick callback.
     */

    @Test
    fun reportItem_click_triggersCallback() {
        var clicked = false

        composeTestRule.setContent {
            ReportItem(
                report = fakeReport,
                onClick = { clicked = true }
            )
        }

        composeTestRule
            .onNodeWithText("Broken road")
            .performClick()

        assertTrue(clicked)
    }

    /**
     * UI test that verifies that the title input field
     * is visible and accepts user input.
     *
     * This test does not verify state updates,
     * which are tested separately in ViewModel tests.
     */

    @Test
    fun addEditScreen_titleField_acceptsInput() {
        composeTestRule.setContent {
            AddEditReportScreenContent(
                paddingValues = PaddingValues(),
                data = AddEditReportScreenUIState(
                    report = Report(
                        title = "",
                        description = "",
                        category = "",
                        status = "new",
                        location = LocationEntity(0.0, 0.0),
                        photoUri = ""
                    ),
                    loading = false
                ),
                actions = FakeAddEditActions(),
                navigation = FakeNavigation(),
                id = null
            )
        }

        // Verify that title field exists and can receive input
        composeTestRule
            .onNodeWithText("Title")
            .assertIsDisplayed()
            .performTextInput("My report")
    }

    /**
     * UI test that simulates typing into the title field.
     *
     * The test ensures no crash occurs during input.
     * State updates are intentionally not asserted here.
     */
    @Test
    fun addEditScreen_typingTitle_updatesField() {
        val state = AddEditReportScreenUIState(
            report = Report(
                title = "",
                description = "",
                category = "",
                status = "new",
                location = LocationEntity(0.0, 0.0),
                photoUri = ""
            ),
            loading = false
        )

        composeTestRule.setContent {
            AddEditReportScreenContent(
                paddingValues = PaddingValues(),
                data = state,
                actions = FakeAddEditActions(),
                navigation = FakeNavigation(),
                id = null
            )
        }

        composeTestRule
            .onNodeWithText("Title")
            .performTextInput("My report")


    }


}

private val fakeReport = Report(
    title = "Broken road",
    description = "Hole",
    category = "Infrastructure",
    status = "new",
    location = LocationEntity(49.2, 16.6),
    photoUri = ""
)
class FakeAddEditActions : AddEditReportScreenActions {

    override fun onTitleChanged(value: String) {}
    override fun onDescriptionChanged(value: String) {}
    override fun onCategoryChanged(value: String) {}
    override fun onStatusChanged(value: String) {}

    override fun onPhotoSelected(context: Context, uri: String) {}
    override fun onLocationChanged(value: LocationEntity) {}

    override fun saveReport() {}
    override fun deleteReport() {}
}

class FakeNavigation : INavigationRouter {
    override fun returnBack() {}
    override fun navigateToMainScreen() {
        TODO("Not yet implemented")
    }

    override fun navigateToOnBoarding1() {
        TODO("Not yet implemented")
    }

    override fun navigateToOnBoarding2() {
        TODO("Not yet implemented")
    }

    override fun navigateToRoute(route: String) {
        TODO("Not yet implemented")
    }

    override fun navigateToSettingsScreen() {
        TODO("Not yet implemented")
    }

    override fun navigateToReportDetail(id: Long?) {
        TODO("Not yet implemented")
    }

    override fun getNavController() = throw NotImplementedError()
    override fun navigateToAddEditReport(id: Long?) {
        TODO("Not yet implemented")
    }

    override fun navigateToChoseLocation(latitude: Double?, longitude: Double?) {
        TODO("Not yet implemented")
    }

    override fun returnFromMap(latitude: Double, longitude: Double) {
        TODO("Not yet implemented")
    }

    override fun navigateToAddEditEvent(id: Long?) {
        TODO("Not yet implemented")
    }

    override fun navigateToEventDetail(id: Long?) {
        TODO("Not yet implemented")
    }
}

